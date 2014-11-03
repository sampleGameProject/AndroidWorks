package pigareva_10moa.lab45;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import pigareva_10moa.lab45.data.TelephonySMSActivity;


public class StartActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ((Button)findViewById(R.id.buttonShowPreferences)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonOpenJSON)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonOpenCSV)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonPoster)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonTelephony)).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // показать экран настроек
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonShowPreferences:
                startActivity(new Intent(this,ShaderPreferencesActivity.class));
                break;
            case R.id.buttonOpenJSON:
                startActivity(new Intent(this,JSONActivity.class));
                break;
            case R.id.buttonOpenCSV:
                startActivity(new Intent(this,CSVActivity.class));
                break;
            case R.id.buttonPoster:
                startActivity(new Intent(this,PosterActivity.class));
                break;
            case R.id.buttonTelephony:
                startActivity(new Intent(this,TelephonySMSActivity.class));
                break;

        }
    }
}
