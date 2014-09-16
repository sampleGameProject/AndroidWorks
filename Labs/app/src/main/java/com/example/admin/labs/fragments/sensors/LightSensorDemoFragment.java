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
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.admin.labs.MainActivity;
import com.example.admin.labs.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LightSensorDemoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LightSensorDemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LightSensorDemoFragment extends Fragment implements SensorEventListener{


    SensorManager sensorManager;
    Sensor light;
    int currentRate;
    ProgressBar lightIndicator;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static LightSensorDemoFragment newInstance(int sectionNumber) {
        LightSensorDemoFragment fragment = new LightSensorDemoFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public LightSensorDemoFragment() {
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
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        currentRate = SensorManager.SENSOR_DELAY_NORMAL;
        sensorManager.registerListener(this, light, currentRate );

        View view = inflater.inflate(R.layout.fragment_light_sensor_demo, container, false);
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

        textName.setText(light.getName());
        textVendor.setText(light.getVendor());
        textVersion.setText(light.getVersion());

        lightIndicator = (ProgressBar) view.findViewById(R.id.lightIndicator);
        return view;
    }

    private void updateSensorDelay(int sensorDelay) {
        currentRate = sensorDelay;
        sensorManager.unregisterListener(this);
        sensorManager.registerListener(this, light, currentRate);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_LIGHT) {
            final float currentReading = event.values[0];
            Log.i("SENSOR", event.toString());
            lightIndicator.setProgress((int) currentReading);
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
        sensorManager.registerListener(this, light, currentRate);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
