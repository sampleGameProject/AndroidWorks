package kalashnikov_10po2.labs;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SensorsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        ListView listView = (ListView) findViewById(R.id.listView);

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<HashMap<String, String>> sensorsMapList = new ArrayList<HashMap<String, String>>();

        for (Sensor s : deviceSensors){
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            map.put("Name",s.getName());
            map.put("Vendor", s.getVendor());
            map.put("MaxRange", Float.toString(s.getMaximumRange()));
            map.put("Resolution", Float.toString(s.getResolution()));
            map.put("Power", Float.toString(s.getPower()) + " мА");
            sensorsMapList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, sensorsMapList, R.layout.list_item,
                new String[] {"Name","Vendor","MaxRange","Resolution","Power"},
                new int[] { R.id.sensorName, R.id.vendorText,R.id.rangeText,R.id.resolutionText,R.id.powerText});

        listView.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sensors, menu);
        return true;
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
}
