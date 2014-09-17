package com.example.admin.labs.fragments.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.admin.labs.MainActivity;
import com.example.admin.labs.R;
import com.example.admin.labs.fragments.MainActivityFragment;


public class SensorDemoFragment extends MainActivityFragment implements SensorEventListener{

    SensorManager sensorManager;
    Sensor sensor;
    int currentRate;
    TextView textSensorValue;

    private OnFragmentInteractionListener mListener;

    public static final String SENSOR_TYPE = "sensor_type";

    public SensorDemoFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        int sensorType = getArguments().getInt(SENSOR_TYPE);

        sensor = sensorManager.getDefaultSensor(sensorType);
        currentRate = SensorManager.SENSOR_DELAY_NORMAL;
        sensorManager.registerListener(this, sensor, currentRate );

        View view = inflater.inflate(R.layout.fragment_sensor_demo, container, false);
        RadioGroup group = (RadioGroup) view.findViewById(R.id.sensorRateRadioGroup);
        group.check(R.id.sensorRateMedium);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sensorRateLow:
                        updateSensorDelay(SensorManager.SENSOR_DELAY_UI);
                        break;
                    case R.id.sensorRateMedium:
                        updateSensorDelay(SensorManager.SENSOR_DELAY_NORMAL);
                        break;
                    case R.id.sensorRateHight:
                        updateSensorDelay(SensorManager.SENSOR_DELAY_GAME);
                        break;
                    case R.id.sensorRateRealTime:
                        updateSensorDelay(SensorManager.SENSOR_DELAY_FASTEST);
                        break;
                    default:
                        break;
                }
            }
        });

        TextView textName = (TextView) view.findViewById(R.id.sensorName);
        TextView textVendor = (TextView) view.findViewById(R.id.sensorVendor);
        TextView textVersion = (TextView) view.findViewById(R.id.sensorVersion);

        textName.setText(sensor.getName());
        textVendor.setText(sensor.getVendor());
        textVersion.setText("Версия " + Integer.toString(sensor.getVersion()));

        textSensorValue = (TextView) view.findViewById(R.id.sensorValue);

        switch (sensorType){
            case Sensor.TYPE_LIGHT:
                textSensorValue.setBackgroundColor(getResources().getColor(R.color.lightSensorBackground));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                textSensorValue.setBackgroundColor(getResources().getColor(R.color.ambientTemperatureBackground));
                break;
        }
        return view;
    }

    private void updateSensorDelay(int sensorDelay) {
        currentRate = sensorDelay;
        sensorManager.unregisterListener(this);
        sensorManager.registerListener(this, sensor, currentRate);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
            final float currentReading = event.values[0];
            Log.i("LIGHT SENSOR: ", Float.toString(currentReading));
            textSensorValue.setText(String.format("Показания датчика: %.2f лк", currentReading));
        }
        if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            final float currentReading = event.values[0];
            Log.i("AMBIENT TEMPERATURE SENSOR: ", Float.toString(currentReading));
            textSensorValue.setText(String.format("Показания датчика: %.2f °C", currentReading));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, currentRate);
    }

}
