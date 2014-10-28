package com.example.admin.labs;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.admin.labs.fragments.CameraFragment;
import com.example.admin.labs.fragments.DemoInterfaceFragment;
import com.example.admin.labs.fragments.MainActivityFragment;
import com.example.admin.labs.fragments.ProjectsListFragment;
import com.example.admin.labs.fragments.SimpleFragment;
import com.example.admin.labs.fragments.sensors.SensorDemoFragment;
import com.example.admin.labs.fragments.sensors.SensorsInfoFragment;
import com.example.admin.labs.models.helpers.AlertDialogHelper;
import com.example.admin.labs.models.helpers.SectionHelper;


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
    private SectionHelper sectionsManager;
    private SensorManager sensorManager;

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LABS_TAG = "LABS_TAG";

    private static final int DEMO_INTERFACE_POSITION = 1;
    private static final int SIMPLE_INTERFACE_POSITION = 2;
    private static final int PROJECTS_MANAGER_POSITION = 4;
    private static final int SENSORS_LIST_POSITION = 6;
    private static final int AMBIENT_TEMPERATURE_SENSOR_POSITION = 7;
    private static final int LIGHT_SENSOR_POSITION = 8;
    private static final int DEMO_SOUND_POSITION = 9;
    private static final int DEMO_CAMERA_POSITION = 10;

    public static final String LAB_PREFERENCES = "lab_preferences";
    public static final String SELECTED_POSITION = "selected_postion";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(LAB_PREFERENCES,MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        sectionsManager = new SectionHelper(this);
        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);

  }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(!canSelectItemFragment(position)){
            String error = getErrorMessage(position);
            Log.i(LABS_TAG,error);
            AlertDialogHelper.showAlertView(this, error);
            return;
        }

        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putInt(SELECTED_POSITION, position);
        editor.commit();

        Log.i(LABS_TAG,"Select item at " + Integer.toString(position));
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, createFragment(position))
                .commit();
    }



    private Fragment createFragment(int position){
        switch(position){
            case DEMO_INTERFACE_POSITION:
                return FragmentFactory.createFragment(DemoInterfaceFragment.class,position);
            case SIMPLE_INTERFACE_POSITION:
                return FragmentFactory.createFragment(SimpleFragment.class,position);
            case PROJECTS_MANAGER_POSITION:
                return FragmentFactory.createFragment(ProjectsListFragment.class,position);
            case SENSORS_LIST_POSITION:
                return FragmentFactory.createFragment(SensorsInfoFragment.class,position);
            case AMBIENT_TEMPERATURE_SENSOR_POSITION:
                return FragmentFactory.createFragment(SensorDemoFragment.class,position,Sensor.TYPE_AMBIENT_TEMPERATURE);
            case LIGHT_SENSOR_POSITION:
                return FragmentFactory.createFragment(SensorDemoFragment.class,position,Sensor.TYPE_LIGHT);
            case DEMO_CAMERA_POSITION:
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
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean canSelectItemFragment(int position) {
        if (position == LIGHT_SENSOR_POSITION) {
            return sensorIsAvailable(Sensor.TYPE_LIGHT);
        }
        if(position == AMBIENT_TEMPERATURE_SENSOR_POSITION){
            return sensorIsAvailable(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        if(position == DEMO_CAMERA_POSITION){
            return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }

        return true;
    }

    private boolean sensorIsAvailable(int type){
        return sensorManager.getDefaultSensor(type) != null;
    }

    private String getErrorMessage(int position){
        if(position == LIGHT_SENSOR_POSITION){
            return "Датчик освещения недоступен!";
        }
        if(position == AMBIENT_TEMPERATURE_SENSOR_POSITION){
            return "Датчик температуры недоступен!";
        }
        if(position == DEMO_SOUND_POSITION){
            return "звк ндстпн!";
        }
        if(position == DEMO_CAMERA_POSITION){
            return "Видео камера недоступна!";
        }
        return "Ошибка!";
    }


    public static class PlaceholderFragment extends MainActivityFragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            Animation animRotate = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.infinite_rotation);

            imageView.setAnimation(animRotate);
            return rootView;
        }

    }


}
