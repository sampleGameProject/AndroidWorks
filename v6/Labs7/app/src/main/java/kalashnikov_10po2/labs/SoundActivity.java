package kalashnikov_10po2.labs;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SoundActivity extends Activity implements View.OnClickListener {

    Button button;
    Vibrator v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        button = (Button) findViewById(R.id.buttonPlay);
        button.setOnClickListener(this);

       v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sound, menu);
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

    MediaPlayer mediaPlayer;

    @Override
    public void onClick(View view) {

        v.vibrate(500);

        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.d_m_c_band_ver);
            mediaPlayer.start();
            button.setText("Stop");
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            button.setText("Play");
        }

    }

    @Override
    protected void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
