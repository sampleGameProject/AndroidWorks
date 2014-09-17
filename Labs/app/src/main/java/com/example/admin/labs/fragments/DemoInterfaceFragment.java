package com.example.admin.labs.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.labs.MainActivity;
import com.example.admin.labs.R;

public class DemoInterfaceFragment extends MainActivityFragment {

    public DemoInterfaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Activity activity = getActivity();

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        final TextView tw = new TextView(activity);
        tw.setText("Enter message..");

        final EditText et = new EditText(activity);

        Button btn = new Button(activity);
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

        ImageView im = new ImageView(activity);
//        im.setImageResource(R.drawable.ic_sample);
        layout.addView(im);

        final TextView tw2 = new TextView(activity);
        tw2.setText("Simple textView");
        final EditText et2 = new EditText(activity);


//        et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    Toast.makeText(activity.getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
//                    handled = true;
//                }
//                return handled;
//            }
//        });

        layout.addView(tw2);
        layout.addView(et2);

        btn = new Button(activity.getApplicationContext());
        btn.setText("Start data work");

        layout.addView(btn);

//        Animation animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fading);
//        animation.setRepeatMode(Animation.INFINITE);
//        btn.startAnimation(animation);

        return layout;

//        return inflater.inflate(R.layout.fragment_demo_interface, container, false);
    }

}
