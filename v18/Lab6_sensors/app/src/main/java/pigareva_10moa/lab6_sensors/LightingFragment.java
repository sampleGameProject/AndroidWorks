package pigareva_10moa.lab6_sensors;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LightingFragment extends Fragment implements SensorEventListener{


    private SensorManager sensorManager;
    TextView textView;

    public LightingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_lighting, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor != null)
            sensorManager.registerListener(this, sensor,MyActivity.RATE);
        else
            textView.setText("Light sensor is not available");
    }

    @Override
    public void onPause() {
        super.onPause();

        // to stop the listener and save battery
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float currentReading = sensorEvent.values[0];
        textView.setText(String.format("%.2f °C", currentReading));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
