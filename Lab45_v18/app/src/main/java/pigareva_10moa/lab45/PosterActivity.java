package pigareva_10moa.lab45;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import pigareva_10moa.lab45.data.MoviesListActivity;
import pigareva_10moa.lab45.data.TheatersListActivity;


public class PosterActivity extends Activity implements View.OnClickListener {

    public static final String DATA_PREF = "DATA_PREF";

    public static final String USE_CP = "USE_CP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        findViewById(R.id.buttonTheatres).setOnClickListener(this);
        findViewById(R.id.buttonMovies).setOnClickListener(this);

        final SharedPreferences sharedPreferences = getSharedPreferences(DATA_PREF,MODE_PRIVATE);


        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        checkBox.setChecked(sharedPreferences.getBoolean(USE_CP,false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                // переключаем режим работы с данными
                editor.putBoolean(USE_CP,isChecked);
                editor.commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.poster, menu);
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
            case R.id.buttonTheatres:
                startActivity(new Intent(this, TheatersListActivity.class));
                break;
            case R.id.buttonMovies:
                startActivity( new Intent(this, MoviesListActivity.class));
                break;
        }
    }
}
