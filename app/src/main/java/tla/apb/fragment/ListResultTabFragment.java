package tla.apb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tla.apb.R;

/**
 * Created by Nafeal on 5/6/2015.
 */
public class ListResultTabFragment extends Fragment {


    private static final String TAG = "ListResultFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        Bundle extras = getArguments();
        Log.d(TAG, "LONGITUDE: " + extras.getDouble("longitudeField"));

        // TODO: Change Adapter to display your content
//        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.list_tab,container,false);
        Log.d(TAG, "In here");
//        Bundle bundle =
        return v;
    }



}
