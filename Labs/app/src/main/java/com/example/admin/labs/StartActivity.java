package com.example.admin.labs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class StartActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        setContentView(layout, params);

        final TextView tw = new TextView(this);
        tw.setText("Enter message..");

        final EditText et = new EditText(this);

        Button btn = new Button(this);
        btn.setText("Update input");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tw.setText(et.getText());
            }
        });

        layout.addView(tw);
        layout.addView(et);
        layout.addView(btn);

        ImageView im = new ImageView(this);
        im.setImageResource(R.drawable.ic_sample);
        layout.addView(im);

        final TextView tw2 = new TextView(this);
        tw2.setText("Simple textView");
        final EditText et2 = new EditText(this);


        et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
                    handled = true;
                }
                return handled;
            }
        });

        layout.addView(tw2);
        layout.addView(et2);

        btn = new Button(this);
        btn.setText("Start data work");
        btn.setOnClickListener(this);

        layout.addView(btn);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fading);
        animation.setRepeatMode(Animation.INFINITE);
        btn.startAnimation(animation);
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

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, DataWorkActivity.class);
        startActivity(intent);
    }
}
