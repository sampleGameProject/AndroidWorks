package pigareva_10moa.lab45;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class ShaderPreferencesActivity extends Activity {

    // работа с SharedPreferences
    // редактирование и сохранение в JSON

    public static final String LAB_PREFERENCES = "LAB_PREFERENCES";
    public static final String PHONE_PREF = "PHONE_PREF";
    public static final String CHECK_BOX_PREF = "CHECK_BOX_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shader_preferences);

        final RadioGroup value1Editor = (RadioGroup) findViewById(R.id.radioGroup);
        final CheckBox value2Editor = (CheckBox) findViewById(R.id.checkBox);

        final SharedPreferences sharedPreferences = getSharedPreferences(LAB_PREFERENCES,Activity.MODE_PRIVATE);

        final int value1 = sharedPreferences.getInt(PHONE_PREF,2);
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

        boolean value2 = sharedPreferences.getBoolean(CHECK_BOX_PREF,false);
        value2Editor.setChecked(value2);


        Button savePreferencesButton = (Button) findViewById(R.id.buttonAdd);
        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor =  sharedPreferences.edit();

                switch (value1Editor.getCheckedRadioButtonId()){
                    case R.id.radioButton:
                        editor.putInt(PHONE_PREF,0);
                        break;
                    case R.id.radioButton2:
                        editor.putInt(PHONE_PREF,1);
                        break;
                    case R.id.radioButton3:
                        editor.putInt(PHONE_PREF,2);
                        break;
                }

                editor.putBoolean(CHECK_BOX_PREF, value2Editor.isChecked());
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
                    valuesJSON.put(PHONE_PREF,sharedPreferences.getInt(PHONE_PREF, 2));
                    valuesJSON.put(CHECK_BOX_PREF,sharedPreferences.getBoolean(CHECK_BOX_PREF, false));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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


        final TextView tv = (TextView) findViewById(R.id.textViewJSON);

        Button openJSON = (Button)findViewById(R.id.buttonJSON);
        openJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // считать JSON файл и показать результат
                String filename = getString(R.string.jsonFileName);
                File file = new File(getApplicationContext().getFilesDir(),filename);

                //Read text from file
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                    tv.setText(e.toString());
                }

                tv.setText(text.toString());
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
