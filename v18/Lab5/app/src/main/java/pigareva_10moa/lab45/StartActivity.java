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

        ((Button)findViewById(R.id.buttonPoster)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonTelephony)).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonPoster:
                startActivity(new Intent(this,PosterActivity.class));
                break;
            case R.id.buttonTelephony:
                startActivity(new Intent(this,TelephonySMSActivity.class));
                break;

        }
    }
}
