package kalashnikov_10po2.labs;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;


public class SettingsActivity extends Activity {

    public static int RATE = SensorManager.SENSOR_DELAY_UI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.check(R.id.radioButton);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.radioButton:
                        RATE = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case R.id.radioButton2:
                        RATE = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case R.id.radioButton3:
                        RATE = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case R.id.radioButton4:
                        RATE = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    default:
                        break;
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
