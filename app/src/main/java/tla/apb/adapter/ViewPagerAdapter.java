package tla.apb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tla.apb.fragment.ListResultFragment;
import tla.apb.fragment.ListResultTabFragment;
import tla.apb.fragment.MapResultTabFragment;

/**
 * Created by Nafeal on 5/6/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //Names of tabs
    CharSequence Titles[];

    //Number of tabs
    int numbOfTabs;

    //Extras
    Bundle extras;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int numOfTabs, Bundle extras) {
        super(fm);
        this.numbOfTabs = numOfTabs;
        this.Titles = mTitles;
        this.extras = extras;
    }


    //Returns fragment based on position given
    @Override
    public Fragment getItem(int position) {

        //First tab
        if( position == 0) {
//            ListResultTabFragment listResultTabFragment = new ListResultTabFragment();
//            listResultTabFragment.setArguments(extras);
//            return listResultTabFragment;
            ListResultFragment listResultFragment = new ListResultFragment();
            listResultFragment.setArguments(extras);
            return listResultFragment;
        } else {
            MapResultTabFragment mapResultTabFragment = new MapResultTabFragment();
            mapResultTabFragment.setArguments(extras);
            return mapResultTabFragment;
        }
    }


    //Returns titles for Tabs in Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    //Return number of tabs for the strip
    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
