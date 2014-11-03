package kalashnikov_10po2.lab45;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import kalashnikov_10po2.lab45.data.CarsListActivity;
import kalashnikov_10po2.lab45.data.EmployeesListActivity;
import kalashnikov_10po2.lab45.data.TasksListActivity;


public class AutoServiceActivity extends Activity implements View.OnClickListener {

    public static final String DATA_PREF = "DATA_PREF";

    public static final String USE_CONTENT_PROVIDER = "USE_CONTENT_PROVIDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoservice);

        findViewById(R.id.buttonEmployees).setOnClickListener(this);
        findViewById(R.id.buttonCars).setOnClickListener(this);
        findViewById(R.id.buttonTasks).setOnClickListener(this);

        final SharedPreferences sharedPreferences = getSharedPreferences(DATA_PREF,MODE_PRIVATE);


        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        checkBox.setChecked(sharedPreferences.getBoolean(USE_CONTENT_PROVIDER,false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                // переключаем режим работы с данными
                editor.putBoolean(USE_CONTENT_PROVIDER,isChecked);
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
            case R.id.buttonEmployees:
                startActivity(new Intent(this, EmployeesListActivity.class));
                break;
            case R.id.buttonCars:
                startActivity( new Intent(this, CarsListActivity.class));
                break;
            case R.id.buttonTasks:
                startActivity( new Intent(this, TasksListActivity.class));
                break;
        }
    }
}
