package com.example.admin.labs;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.example.admin.labs.fragments.CameraFragment;
import com.example.admin.labs.fragments.DemoInterfaceFragment;
import com.example.admin.labs.fragments.MainActivityFragment;
import com.example.admin.labs.fragments.ProjectsListFragment;
import com.example.admin.labs.fragments.SimpleFragment;
import com.example.admin.labs.fragments.sensors.SensorDemoFragment;
import com.example.admin.labs.models.SectionsManager;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private SectionsManager sectionsManager;
    private SensorManager sensorManager;

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LABS_TAG = "LABS_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        sectionsManager = new SectionsManager(this);
        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
  }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        if(!canSelectItemFragment(position)){
            String error = getErrorMessage(position);
            Log.i(LABS_TAG,error);
            showAlertView(error);
            return;
        }

        Log.i(LABS_TAG,"Select item at " + Integer.toString(position));
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, createFragment(position))
                .commit();
    }

    private void showAlertView(String error) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Сообщение")
                .setMessage(error)
                .setNegativeButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private Fragment createFragment(int position){
        switch(position){
            case 1:
                return FragmentFactory.createFragment(DemoInterfaceFragment.class,position);
            case 2:
                return FragmentFactory.createFragment(SimpleFragment.class,position);
            case 5:
                return FragmentFactory.createFragment(ProjectsListFragment.class,position);
            case 8:
                return FragmentFactory.createFragment(SensorDemoFragment.class,position,Sensor.TYPE_AMBIENT_TEMPERATURE);
            case 9:
                return FragmentFactory.createFragment(SensorDemoFragment.class,position,Sensor.TYPE_LIGHT);
            case 11:
                return FragmentFactory.createFragment(CameraFragment.class,position);
        }
        return FragmentFactory.createFragment(PlaceholderFragment.class,position);
    }

    private static class FragmentFactory {

        public static <T extends MainActivityFragment> T createFragment(Class<T> mainActivityFragment, int sectionNumber) {

            T fragment = createFragment(mainActivityFragment);
            if (fragment == null)
                return null;

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public static <T extends MainActivityFragment> T createFragment(Class<T> mainActivityFragment, int sectionNumber,int sensorType) {

            T fragment = createFragment(mainActivityFragment);
            if (fragment == null)
                return null;

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(SensorDemoFragment.SENSOR_TYPE,sensorType);
            fragment.setArguments(args);

            return fragment;
        }

        private static <T extends MainActivityFragment> T createFragment(Class<T> mainActivityFragment){
            try {
                return mainActivityFragment.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void onSectionAttached(int number) {
        mTitle = sectionsManager.getItemTitle(number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean canSelectItemFragment(int position) {
        if (position == 9) {
            return sensorIsAvailable(Sensor.TYPE_LIGHT);
        }
        if(position == 8){
            return sensorIsAvailable(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        return true;
    }

    private boolean sensorIsAvailable(int type){
        return sensorManager.getDefaultSensor(type) != null;
    }

    private String getErrorMessage(int position){
        if(position == 9){
            return "Датчик освещения недоступен!";
        }
        if(position == 8){
            return "Датчик температуры недоступен!";
        }
        if(position == 10){
            return "Запись звука недоступна!";
        }
        if(position == 11){
            return "Запись видео недоступна!";
        }
        return "Ошибка!";
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends MainActivityFragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    }


}
