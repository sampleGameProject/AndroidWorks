package kalashnikov_10po2.labs;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;


public class GyroscopeActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;

    private TextView tvX,tvY,tvZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        tvX = (TextView) findViewById(R.id.textViewX);
        tvY = (TextView) findViewById(R.id.textViewY);
        tvZ = (TextView) findViewById(R.id.textViewZ);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gyroscope, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        Random rand = new Random();

        Sensor sensor = deviceSensors.get(rand.nextInt(deviceSensors.size()));

        Toast.makeText(this,String.format("Select sensor with type : %d",sensor.getType()),Toast.LENGTH_LONG);

        if(sensor != null)
            mSensorManager.registerListener(this, sensor,SettingsActivity.RATE);
        else
            Log.i("SENSOR","Gyroscope is not available");

    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.values.length == 3)
        {
            tvX.setText(String.format("val1 : %0.2f ", sensorEvent.values[0]));
            tvY.setText(String.format("val2 : %0.2f ", sensorEvent.values[1]));
            tvZ.setText(String.format("val3 : %0.2f ", sensorEvent.values[2]));
        }

        if(sensorEvent.values.length == 2)
        {
            tvX.setText(String.format("val1 : %0.2f ", sensorEvent.values[0]));
            tvY.setText(String.format("val2 : %0.2f ", sensorEvent.values[1]));
            tvZ.setText("No value ");
        }

        if(sensorEvent.values.length == 1)
        {
            tvX.setText(String.format("val1 : %0.2f ", sensorEvent.values[0]));
            tvY.setText("No value");
            tvZ.setText("No value");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
