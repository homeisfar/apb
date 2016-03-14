package tla.apb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tla.apb.R;
import tla.apb.adapter.ViewPagerAdapter;
import tla.apb.externWidget.SlidingTabLayout;

public class ResultsActivity extends ActionBarActivity {

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.pager)
    ViewPager viewPager;

    ViewPagerAdapter viewPagerAdapter;

    @InjectView(R.id.tabs)
    SlidingTabLayout slidingTabLayout;
    private CharSequence tabNames[] = {"Results", "Map"};
    private int numOfTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.inject(this);

        //  Set the toolbar for the activity with custom layout
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        //Instantiate adapter with titles and number of tabs
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabNames, numOfTabs, extras);

        //Attach the custom view pager adapter to the viewpager
        viewPager.setAdapter(viewPagerAdapter);

        //Ensures tabs are evenly divided with equal widths
        slidingTabLayout.setDistributeEvenly(true);

        //Set custom color for scroll of tab scroll bar
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        //Set slidingtablayout's viewpager to defined viewpager
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivityForResult(new Intent(this, AboutActivity.class),0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
