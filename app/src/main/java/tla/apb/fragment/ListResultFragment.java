package tla.apb.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import tla.apb.R;
import tla.apb.adapter.AnimalArrayAdapter;
import tla.apb.adapter.AnimalRealmAdapter;
import tla.apb.fragment.dummy.DummyContent;
import tla.apb.model.Animal;
import tla.apb.util.Constants;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ListResultFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private Realm realm;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */

    private RealmResults<Animal> animalRealmResults;
    private AnimalRealmAdapter animalRealmAdapter;
//    private AnimalArrayAdapter animalArrayAdapter;
//    private ArrayList<Animal> animalList;
    private Runnable loadData;
    private ProgressDialog progressDialog;
    private String gender;
    private String category;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();

        double longitude = extras.getDouble("longitudeField", Double.MIN_VALUE);
        Log.d("first: ", ""+longitude);

        double latitude = extras.getDouble("latitudeField", Double.MIN_VALUE);
        Log.d("first: ", ""+latitude);
        gender = Constants.gender_choice[extras.getInt("gender")];
        category = Constants.category_choice[extras.getInt("category")];

        populateAnimalBasedOnQueryParams(gender, category);
        animalRealmAdapter  = new AnimalRealmAdapter(getActivity(), animalRealmResults, true, R.layout.fragment_animal_list_cell);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        (mListView).setAdapter(animalRealmAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("hw", "hw");
//        if (null != mListener) {
            Log.d("hw", "sldkjflskdfj");
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(animalRealmResults.get(position).id);
            String url = animalRealmResults.get(position).getImage().getUrl();
            Log.d("URL", url);
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(i);
//        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

//    private Runnable returnRes = new Runnable() {
//
//        @Override
//        public void run() {
//            if(animalList != null && animalList.size() > 0){
//                m_adapter.notifyDataSetChanged();
//                for(int i=0;i<m_orders.size();i++)
//                    m_adapter.add(m_orders.get(i));
//            }
//            m_ProgressDialog.dismiss();
//            m_adapter.notifyDataSetChanged();
//        }
//    };


    private void populateAnimalBasedOnQueryParams(String gender, String category) {
        realm = Realm.getInstance(this.getActivity());
        RealmResults<Animal> results = realm.where(Animal.class).findAll();
        if (!gender.equals("Both")) {
            results = results.where().contains("sex", gender).findAll();
        }
        if (!category.equals("Both")) {
            results = results.where().equalTo("type", category).findAll();
        }
        animalRealmResults = results;
    }
}
