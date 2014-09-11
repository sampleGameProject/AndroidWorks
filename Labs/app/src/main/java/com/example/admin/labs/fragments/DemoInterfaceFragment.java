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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DemoInterfaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DemoInterfaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DemoInterfaceFragment extends Fragment {


    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static DemoInterfaceFragment newInstance(int sectionNumber) {
        DemoInterfaceFragment fragment = new DemoInterfaceFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public DemoInterfaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
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
