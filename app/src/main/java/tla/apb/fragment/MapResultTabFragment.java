package tla.apb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.security.acl.AclNotFoundException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import tla.apb.R;
import tla.apb.model.Animal;
import tla.apb.util.Constants;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Nafeal on 5/6/2015.
 */
public class MapResultTabFragment extends Fragment {
    Realm realm;
    ArrayList<Animal> animals = new ArrayList<>();
    double longitude;
    double latitude;

    private GoogleMap googleMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    private String gender;
    private String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.map_tab,container,false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        return v;
    }

    public void loadData(){
        realm = Realm.getInstance(this.getActivity());
        RealmResults<Animal> results = realm.where(Animal.class).findAll();
        if (!gender.equals("Both")) {
            results = results.where().contains("sex", gender).findAll();
        }
        if (!category.equals("Both")) {
            results = results.where().equalTo("type", category).findAll();
        }
        for (Animal a: results) {
            animals.add(a);
            Log.d("LOADDATA: ", "" +a.getFoundLocation().getLongitude() + " " + a.getFoundLocation().getLatitude());
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            MapsInitializer.initialize(getActivity());


        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        initializeMap();

        LatLng coordinate = new LatLng(latitude, longitude);
        Log.d("this is update: ", ""+latitude);
        Log.d("this is update: ", ""+longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
        googleMap.animateCamera(yourLocation);

    }

    private void initializeMap() {
        if (googleMap == null && mapsSupported) {
            mapView = (MapView) getActivity().findViewById(R.id.mapview);
            googleMap = mapView.getMap();

            Bundle extras = getArguments();
            longitude = extras.getDouble("longitudeField", Double.MIN_VALUE);
            latitude = extras.getDouble("latitudeField", Double.MIN_VALUE);
            gender = Constants.gender_choice[extras.getInt("gender")];
            category = Constants.category_choice[extras.getInt("category")];
            Log.d("E", "what" + gender);
            loadData();
            for (Animal a: animals) {
                if (a == null || a.getFoundLocation() == null) continue;
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(a.getFoundLocation().getLatitude(), a.getFoundLocation().getLongitude()))
                        .title("" + a.getAnimalId()))
                ;
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        initializeMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
