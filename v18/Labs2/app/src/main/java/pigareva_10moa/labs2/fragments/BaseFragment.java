package pigareva_10moa.labs2.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pigareva_10moa.labs2.MainActivity;
import pigareva_10moa.labs2.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class BaseFragment extends Fragment {

    protected boolean useXML;

    public static final String FRAGMENT_TAG = "Fragment";

    public BaseFragment() {
        // Required empty public constructor
//        useXML = getArguments().getBoolean("useXML");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.FragmentFactory.ARG_SECTION_NUMBER));
    }


}
