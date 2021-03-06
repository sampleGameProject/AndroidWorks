package kalevko_10po2.lab45;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;


public class ShaderPreferencesActivity extends Activity {

    // работа с SharedPreferences
    // редактирование и сохранение в JSON

    public static final String LAB_PREFERENCES = "LAB_PREFERENCES";
    public static final String VALUE_1 = "VALUE_1";
    public static final String VALUE_2 = "VALUE_2";
    public static final String VALUE_3 = "VALUE_3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shader_preferences);

        final RadioGroup value1Editor = (RadioGroup) findViewById(R.id.radioGroup);
        final CheckBox value2Editor = (CheckBox) findViewById(R.id.checkBox);
        final EditText value3Editor = (EditText) findViewById(R.id.editTextName);

        final SharedPreferences sharedPreferences = getSharedPreferences(LAB_PREFERENCES,Activity.MODE_PRIVATE);

        final int value1 = sharedPreferences.getInt(VALUE_1,2);
        switch (value1){
            case 0:
                value1Editor.check(R.id.radioButton);
                break;
            case 1:
                value1Editor.check(R.id.radioButton2);
                break;
            case 2:
                value1Editor.check(R.id.radioButton3);
                break;
        }

        boolean value2 = sharedPreferences.getBoolean(VALUE_2,false);
        value2Editor.setChecked(value2);

        String value3 = sharedPreferences.getString(VALUE_3,getString(R.string.defaultValue3));
        value3Editor.setText(value3);

        Button savePreferencesButton = (Button) findViewById(R.id.buttonAdd);
        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor =  sharedPreferences.edit();

                switch (value1Editor.getCheckedRadioButtonId()){
                    case R.id.radioButton:
                        editor.putInt(VALUE_1,0);
                        break;
                    case R.id.radioButton2:
                        editor.putInt(VALUE_1,1);
                        break;
                    case R.id.radioButton3:
                        editor.putInt(VALUE_1,2);
                        break;
                }

                editor.putBoolean(VALUE_2, value2Editor.isChecked());
                editor.putString(VALUE_3, value3Editor.getText().toString());
                editor.commit();

                Toast.makeText(getApplicationContext(), getString(R.string.preferences_saved_toast), Toast.LENGTH_LONG).show();

            }
        });

        Button saveJSON = (Button) findViewById(R.id.buttonSaveJSON);
        saveJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream outputStream;
                String filename = getString(R.string.jsonFileName);

                JSONObject valuesJSON = new JSONObject();
                try {
                    valuesJSON.put(VALUE_1,sharedPreferences.getInt(VALUE_1,2));
                    valuesJSON.put(VALUE_2,sharedPreferences.getBoolean(VALUE_2,false));
                    valuesJSON.put(VALUE_3,sharedPreferences.getString(VALUE_3,getString(R.string.defaultValue3)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                File file = new File(getApplicationContext().getFilesDir(),filename);
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(valuesJSON.toString().getBytes());
                    outputStream.close();
                    Toast.makeText(getApplicationContext(), getString(R.string.json_success), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.json_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shader_preferences, menu);
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
