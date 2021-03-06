package com.infostructure.mybigbro.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.ui.OnFragmentInteractionListener;
import com.infostructure.mybigbro.ui.fragments.MainFragment;
import com.infostructure.mybigbro.ui.fragments.MapAPIv2Fragment;
import com.infostructure.mybigbro.ui.fragments.MapFragment;
import com.infostructure.mybigbro.ui.fragments.NavigationDrawerFragment;
import com.infostructure.mybigbro.ui.fragments.NearestWebCamsFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnFragmentInteractionListener {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set up the
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance(position + 1))
                        .commit();
                getSupportActionBar().setTitle(R.string.title_location);
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NearestWebCamsFragment.newInstance(position + 1))
                        .commit();
                getSupportActionBar().setTitle(R.string.title_nearestcams);
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MapAPIv2Fragment.newInstance(position + 1))
                        .commit();
                getSupportActionBar().setTitle(R.string.title_map);
                break;
            default:
                getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_location);
                break;
            case 2:
                mTitle = getString(R.string.title_nearestcams);
                break;
            case 3:
                mTitle = getString(R.string.title_map);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        // Comment this so we only show the current fragement's name.
        //actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Not implemented yet - not needed.
     */
    @Override
    public void onFragmentInteraction(String id) {

    }
}
