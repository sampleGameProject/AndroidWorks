package pigareva_10moa.lab6_sensors;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class GyroscopeFragment extends Fragment implements SensorEventListener {


    SensorManager sensorManager;
    OpenGLRenderer renderer;

    public GyroscopeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        GLSurfaceView glView = new GLSurfaceView(getActivity());
        renderer = new OpenGLRenderer();
        glView.setRenderer(renderer);
        return glView;
    }


    @Override
    public void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(sensor != null)
            sensorManager.registerListener(this, sensor,MyActivity.RATE);
        else
            Log.i("SENSOR", "Gyroscope is not available");
    }

    @Override
    public void onPause() {
        super.onPause();

        // to stop the listener and save battery
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        renderer.setCubeRotation(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
