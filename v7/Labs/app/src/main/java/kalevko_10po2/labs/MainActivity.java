package kalevko_10po2.labs;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;

import kalevko_10po2.labs.NavigationDrawerFragment;
import kalevko_10po2.R;
import kalevko_10po2.labs.fragments.SoundFragment;
import kalevko_10po2.labs.fragments.AlertDialogHelper;
import kalevko_10po2.labs.fragments.CameraFragment;
import kalevko_10po2.labs.fragments.sensors.SensorDemoFragment;
import kalevko_10po2.labs.fragments.sensors.SensorsInfoFragment;


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

    private SensorManager sensorManager;

    private static final String LABS_TAG = "LABS_TAG";

    private static final int SENSORS_LIST_POSITION = 0;
    private static final int AMBIENT_TEMPERATURE_SENSOR_POSITION = 1;
    private static final int LIGHT_SENSOR_POSITION = 2;
    private static final int DEMO_SOUND_POSITION = 3;
    private static final int DEMO_CAMERA_POSITION = 4;


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

        Log.i(LABS_TAG,"Select item at " + Integer.toString(position));
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, createFragment(position))
                .commit();
    }



    private Fragment createFragment(int position){
        switch(position){
            case SENSORS_LIST_POSITION:
                return new SensorsInfoFragment();
            case DEMO_CAMERA_POSITION:
                return new CameraFragment();
            case DEMO_SOUND_POSITION:
                return new SoundFragment();
        }

        if(position == AMBIENT_TEMPERATURE_SENSOR_POSITION ||
                position == LIGHT_SENSOR_POSITION) {
            Fragment fragment = new SensorDemoFragment();
            Bundle args = new Bundle();

            switch (position){
                case AMBIENT_TEMPERATURE_SENSOR_POSITION:
                    args.putInt(SensorDemoFragment.SENSOR_TYPE,Sensor.TYPE_AMBIENT_TEMPERATURE);
                    break;
                case LIGHT_SENSOR_POSITION:
                    args.putInt(SensorDemoFragment.SENSOR_TYPE,Sensor.TYPE_LIGHT);
                    break;
            }
            fragment.setArguments(args);
            return fragment;
        }
        return null;
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

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

}
