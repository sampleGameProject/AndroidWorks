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

import org.w3c.dom.Text;


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

        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
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
        tvX.setText(String.format("X : %0.2f rad/s", sensorEvent.values[0]));
        tvY.setText(String.format("Y : %0.2f rad/s", sensorEvent.values[1]));
        tvZ.setText(String.format("Z : %0.2f rad/s", sensorEvent.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
