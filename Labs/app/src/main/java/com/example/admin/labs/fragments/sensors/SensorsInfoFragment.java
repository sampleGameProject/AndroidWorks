package com.example.admin.labs.fragments.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.admin.labs.R;
import com.example.admin.labs.fragments.MainActivityFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SensorsInfoFragment extends MainActivityFragment {

    public SensorsInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensors_info, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        SensorManager mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<HashMap<String, String>> sensorsMapList = new ArrayList<HashMap<String, String>>();

        for (Sensor s : deviceSensors){
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            map.put("Name",s.getName());
            map.put("Type", getSensorTypeString(s.getType()));
            map.put("Vendor", s.getVendor());
            map.put("MaxRange", Float.toString(s.getMaximumRange()));
            map.put("Resolution", Float.toString(s.getResolution()));
            map.put("Power", Float.toString(s.getPower()) + " мА");
            sensorsMapList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), sensorsMapList, R.layout.sensor_list_item,
                new String[] {"Name","Type", "Vendor","MaxRange","Resolution","Power"},
                new int[] { R.id.sensorName,R.id.typeText, R.id.vendorText,R.id.rangeText,R.id.resolutionText,R.id.powerText});

        listView.setAdapter(adapter);

        return view;
    }

    private String getSensorTypeString(int type) {
        switch (type){
            case Sensor.TYPE_ACCELEROMETER:
                return "Акселерометр";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "Датчик окружающей температуры";
            case Sensor.TYPE_GRAVITY:
                return "Датчик гравитации";
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                return "";
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return "";
            case Sensor.TYPE_GYROSCOPE:
                return "Гироскоп";
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                return "";
            case Sensor.TYPE_HEART_RATE:
                return "Датчик пульса";
            case Sensor.TYPE_LIGHT:
                return "Датчик освещенности";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "Датчик магнитного поля";
            case Sensor.TYPE_PRESSURE:
                return "Датчик давления";
            case Sensor.TYPE_PROXIMITY:
                return "Датчик близости";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "Датчик влажности";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "Датчик вращения";
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                return "Датчик движения";
            case Sensor.TYPE_STEP_COUNTER:
                return "Шагометр";
            case Sensor.TYPE_STEP_DETECTOR:
                return "Датчик шагов";
            case Sensor.TYPE_ORIENTATION:
                return "Датчик шагов";

        }
        return "Неизвестный сенсор";
    }


}
