package pigareva_10moa.lab6_sensors;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SensorsFragment extends Fragment {


    public SensorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sensors, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        SensorManager mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<HashMap<String, String>> sensorsMapList = new ArrayList<HashMap<String, String>>();

        for (Sensor s : deviceSensors){
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            map.put("Name",s.getName());
            map.put("Type", Integer.toString(s.getType()));
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


}
