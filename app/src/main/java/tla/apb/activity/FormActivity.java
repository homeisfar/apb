package tla.apb.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import tla.apb.R;
import tla.apb.model.Animal;
import tla.apb.model.FoundAddress;
import tla.apb.model.Url;
import tla.apb.util.FoundAddressDeserializer;
import tla.apb.util.UrlDeserializer;


public class FormActivity extends Activity implements ViewSwitcher.ViewFactory, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final boolean DEBUG = true;
    private int downX, upX;

    @InjectView(R.id.imageswither)
    ImageSwitcher categoryImageSwitcher;
    @InjectView(R.id.imageswither2)
    ImageSwitcher genderImageSwitcher;

    @InjectView(R.id.progress_bar)
    SmoothProgressBar progressbar;

    private static int current_choice_dog_cat = 0;
    private static int current_choice_gender = 0;

    private static final String prefName = "jsonValidation";
    private Date lastModifiedDate;

    //Location Variables
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private boolean hasPlayService;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;



    //TAGs
    private static final String TAG_PLAYSERVICE = "GooglePlayService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);

        //Set up UI
        setButtonAnimationForClicking();
        setClickListeners();

        //Location
        createLocationRequest();

        if (isGooglePlayServicesAvailable()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            hasPlayService = true;
            Log.d(TAG_PLAYSERVICE, "GooglePlayService is available");
        } else {
            hasPlayService = false;
            Log.d(TAG_PLAYSERVICE, "GooglePlayService is NOT available");
        }


        SharedPreferences mPref = getSharedPreferences(prefName, MODE_PRIVATE);

        long lastModifiedMSeconds = mPref.getLong("LastModified", -1);
        Log.d("OnCreate", "LastModifiedSeconds: " + lastModifiedMSeconds);

        if (lastModifiedMSeconds > 0) {
            this.lastModifiedDate = new Date(lastModifiedMSeconds);
        }

        if (lastModifiedDate == null) {
            Log.d("onCreate", "Last Modified From Preference is Null");
        } else {
            Log.d("onCreate", "Last Modified From Preference: " + lastModifiedDate.toString());
        }

        //Set up DB
        PostFetcher postFetcher = new PostFetcher();
        postFetcher.execute();

    }


    @Override
    public View makeView() {
        ImageView i = new ImageView(this);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG_PLAYSERVICE, "Location update resumed .....................");
        }
    }

    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        Log.d(TAG_PLAYSERVICE, "Location Service update has stopped.");
    }

    /*========================================== Location Services ===================================================================*/

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            Log.d(TAG_PLAYSERVICE, "GService Status Error: " + status);
//            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG_PLAYSERVICE, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        Log.d(TAG_PLAYSERVICE, "Updated Location: Latitude: " + mCurrentLocation.getLatitude()
                + " Longitude: " + mCurrentLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG_PLAYSERVICE, "Connection failed: " + connectionResult.toString());
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG_PLAYSERVICE, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG_PLAYSERVICE, "onStart fired ..............");
        mGoogleApiClient.connect();
        Log.d(TAG_PLAYSERVICE, "onStart: " + mGoogleApiClient.isConnected() + "  " + mGoogleApiClient.isConnecting());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG_PLAYSERVICE, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG_PLAYSERVICE, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG_PLAYSERVICE, "Location update stopped .......................");
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Log.d(TAG_PLAYSERVICE, "Location update started ..............: ");
    }


    /*========================================== UI Setup ===================================================================*/
    public void setButtonAnimationForClicking() {
        //Animal Picker Animation
        categoryImageSwitcher.setFactory(this);
        categoryImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        categoryImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        //Gender Picker Animation
        genderImageSwitcher.setFactory(this);
        genderImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        genderImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
    }

    public void setClickListeners() {
        // we got a problem of resizing
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream is = getResources().openRawResource(R.raw.cat_icon);
        Bitmap catImg = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.cat_icon), null, opt);
        Bitmap dogImg = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.dog_icon), null, opt);
        Bitmap dogcat = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.dogandcat), null, opt);
        final BitmapDrawable[] imageCategoryList = {
                new BitmapDrawable(getResources(), dogImg),
                new BitmapDrawable(getResources(), catImg),
                new BitmapDrawable(getResources(), dogcat)
        };

        //Image arrays
//        final Drawable[] imageCategoryList = {getResources().getDrawable(R.drawable.dog_icon),
//                getResources().getDrawable(R.drawable.cat_icon),
//                getResources().getDrawable(R.drawable.dogandcat)};
        final Drawable[] imageGenderList = {getResources().getDrawable(R.drawable.female),
                getResources().getDrawable(R.drawable.male),
                getResources().getDrawable(R.drawable.unisex)};

        final Integer[] bg_color = {Color.parseColor("#9b59b6"),//wisteria
                Color.parseColor("#2980b9"), //belized hole
                Color.parseColor("#2c3e50") //Midnight blue
        };

        //Create listeners
        final View.OnTouchListener customizedListener = new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    if (upX - downX > 100) {
                        current_choice_dog_cat = (current_choice_dog_cat - 1 + imageCategoryList.length) % imageCategoryList.length;
                        categoryImageSwitcher.setImageDrawable(imageCategoryList[current_choice_dog_cat]);
                    } else if (upX - downX < -100) {
                        current_choice_dog_cat = (current_choice_dog_cat + 1 + imageCategoryList.length) % imageCategoryList.length;
                        categoryImageSwitcher.setImageDrawable(imageCategoryList[current_choice_dog_cat]);
                    }
                }
                return false;
            }
        };

        final View.OnTouchListener customizedListener1 = new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    if (upX - downX > 100) {
                        current_choice_gender = (current_choice_gender - 1 + imageGenderList.length) % imageGenderList.length;
                        genderImageSwitcher.setImageDrawable(imageGenderList[current_choice_gender]);
                        genderImageSwitcher.setBackgroundColor(bg_color[current_choice_gender]);
                    } else if (upX - downX < -100) {
                        current_choice_gender = (current_choice_gender + 1 + imageGenderList.length) % imageGenderList.length;
                        genderImageSwitcher.setImageDrawable(imageGenderList[current_choice_gender]);
                        genderImageSwitcher.setBackgroundColor(bg_color[current_choice_gender]);
                    }
                }
                return false;
            }
        };

        //Set Listeners

        //Setting Animal Chooser Listeners
        categoryImageSwitcher.setImageDrawable(imageCategoryList[current_choice_dog_cat]);
        categoryImageSwitcher.setOnTouchListener(customizedListener);

        findViewById(R.id.left_category).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                current_choice_dog_cat = (current_choice_dog_cat - 1 + imageCategoryList.length) % imageCategoryList.length;
                categoryImageSwitcher.setImageDrawable(imageCategoryList[current_choice_dog_cat]);
            }
        });

        findViewById(R.id.right_category).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                current_choice_dog_cat = (current_choice_dog_cat + 1 + imageCategoryList.length) % imageCategoryList.length;
                categoryImageSwitcher.setImageDrawable(imageCategoryList[current_choice_dog_cat]);
            }
        });

        //Setting Gender Chooser Listeners
        genderImageSwitcher.setImageDrawable(imageGenderList[current_choice_gender]);
        genderImageSwitcher.setBackgroundColor(bg_color[current_choice_gender]);
        genderImageSwitcher.setOnTouchListener(customizedListener1);

        findViewById(R.id.left_gender).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                current_choice_gender = (current_choice_gender - 1 + imageGenderList.length) % imageGenderList.length;
                genderImageSwitcher.setImageDrawable(imageGenderList[current_choice_gender]);
                genderImageSwitcher.setBackgroundColor(bg_color[current_choice_gender]);
            }
        });

        findViewById(R.id.right_gender).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                current_choice_gender = (current_choice_gender + 1 + imageGenderList.length) % imageGenderList.length;
                genderImageSwitcher.setImageDrawable(imageGenderList[current_choice_gender]);
                genderImageSwitcher.setBackgroundColor(bg_color[current_choice_gender]);
            }
        });





    }


/*====================================  DB Storage  ====================================================================*/

    // Post Fetcher to get URL and load into DB
    private class PostFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG_POST = "POST_FETCHER";

        @Override
        protected String doInBackground(Void... params) {
            HttpsURLConnection httpsUrlConnection = null;
            try {

                URL url = new URL(getString(R.string.soda_endpoint));
                httpsUrlConnection = (HttpsURLConnection) url.openConnection();
                //For request permissions
                httpsUrlConnection.setRequestProperty("X-App-Token", getString(R.string.soda_token));

                Log.d(TAG_POST, "Response Status: " + httpsUrlConnection.getResponseCode());
                if (httpsUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    Date jsonLastModifiedDate = new Date(httpsUrlConnection.getLastModified());
                    Log.d("PostFetcher", "Taken modified date: " + jsonLastModifiedDate.toString());

                    if ((lastModifiedDate == null) || jsonLastModifiedDate.compareTo(lastModifiedDate) > 0) {
                        Log.d("PostFetcher", "Date is either null or Preference Date is old");

                        //set last modified date | save in sharedpreferences
                        SharedPreferences sharedPref = getSharedPreferences(prefName, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong("LastModified", jsonLastModifiedDate.getTime());
                        editor.apply();

                        //Read Content
                        InputStream response = new BufferedInputStream(httpsUrlConnection.getInputStream());

                        try {
                            Log.d(TAG_POST, "Reading InpuStream");
                            Reader content = new InputStreamReader(response);
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            //Adding deserializers to custom tailor unserializing for certain objects
                            gsonBuilder.registerTypeAdapter(FoundAddress.class, new FoundAddressDeserializer());
                            gsonBuilder.registerTypeAdapter(Url.class, new UrlDeserializer());
                            gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                            //Excludes all RealmObjects/Models from Gson
                            //Will cause stackoverflow if not excluded
                            gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
                                @Override
                                public boolean shouldSkipField(FieldAttributes f) {
                                    return f.getDeclaringClass().equals(RealmObject.class);
                                }

                                @Override
                                public boolean shouldSkipClass(Class<?> clazz) {
                                    return false;
                                }
                            });

                            Gson gson = gsonBuilder.create();

                            List<Animal> animals = Arrays.asList(gson.fromJson(content, Animal[].class));
                            response.close();
                            storeAnimalToDB(animals);

                        } catch (Exception ex) {
                            Log.e(TAG_POST, "Failed to parse JSON due to: " + ex);
                            failedLoadingPosts();
                        }
                    } //end if check last modified
                } //end if ok_http
            } catch (IOException e) {
                Log.e(TAG_POST, "Failed to make a Connection due to: " + e);
                failedLoadingPosts();
            } finally {
                if (httpsUrlConnection != null) {
                    httpsUrlConnection.disconnect();
                }
            } //end try catch
            return null;
        }

        @Override
        protected void onPostExecute(String input) {
            progressbar.setVisibility(View.GONE);

            //Setting Search Button Clicker Listener
            findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(FormActivity.this, ResultsActivity.class);
                    myIntent.putExtra("category", current_choice_dog_cat);//0 means dog, 1 means cat and 2 means both
                    myIntent.putExtra("gender", current_choice_gender);// 0 means female, 1 means male, 2 means both
                    myIntent.putExtra("latitudeField", mCurrentLocation.getLatitude());
                    myIntent.putExtra("longitudeField", mCurrentLocation.getLongitude());
                    startActivity(myIntent);
                    Log.d("Search Button", "Search Button has been clicked");
                }
            });
        }
    } //end do in background

    private void storeAnimalToDB(List<Animal> animals) {
        Realm realm = null;
        try {
            //Delete the Realm File
            Realm.deleteRealmFile(getApplicationContext());
            realm = Realm.getInstance(getApplicationContext());

            realm.beginTransaction();

            //Will add all models into their own separate database;
            for (Animal animal : animals) {
//                Log.d("storeAnimalToDB", animal.getAnimalId() + "  " + animal.getFoundLocation().getHumanFoundAddress().getAddress());
                realm.copyToRealm(animal);
            }
            realm.commitTransaction();

            Log.d("storeAnimalToDB", "DB has finished loading.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        } //end finally block and try block


    }

    private void failedLoadingPosts() {
        if (DEBUG) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FormActivity.this, "Failed to load Animals. Have a look at LogCat.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
