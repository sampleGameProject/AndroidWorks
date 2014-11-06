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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        findViewById(R.id.buttonTheatres).setOnClickListener(this);
        findViewById(R.id.buttonMovies).setOnClickListener(this);

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
