package kalashnikov_10po2.labs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MyActivity extends Activity implements View.OnClickListener {

    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        findViewById(R.id.buttonCamera).setOnClickListener(this);
        findViewById(R.id.buttonSound).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCamera:
                v.vibrate(500);
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.buttonSound:
                v.vibrate(500);
                startActivity(new Intent(this, SoundActivity.class));
                break;
        }
    }
}
