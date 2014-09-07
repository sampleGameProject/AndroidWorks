package com.example.admin.labs.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.labs.LabsActivity;
import com.example.admin.labs.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DemoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DemoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout rootLayout;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DemoFragment newInstance(int sectionNumber) {
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public DemoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        im.setImageResource(R.drawable.ic_sample);
        layout.addView(im);

        final TextView tw2 = new TextView(activity);
        tw2.setText("Simple textView");
        final EditText et2 = new EditText(activity);


        et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(activity.getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
                    handled = true;
                }
                return handled;
            }
        });

        layout.addView(tw2);
        layout.addView(et2);

        btn = new Button(activity.getApplicationContext());
        btn.setText("Start data work");

        layout.addView(btn);

        Animation animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fading);
        animation.setRepeatMode(Animation.INFINITE);
        btn.startAnimation(animation);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((LabsActivity) activity).onSectionAttached(
                getArguments().getInt("section_number"));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
