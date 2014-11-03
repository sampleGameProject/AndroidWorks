package pigareva_10moa.labs2.fragments;



import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import pigareva_10moa.labs2.MainActivity;
import pigareva_10moa.labs2.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AnimationsFragment extends BaseFragment {


    public AnimationsFragment() {

        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        useXML = getArguments().getBoolean(MainActivity.FragmentFactory.USE_XML);

        if(useXML) {
            Log.i(FRAGMENT_TAG, "Create animation from xml");
            View view = inflater.inflate(R.layout.fragment_animations, container, false);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim);
            iv.setAnimation(anim);
            return view;
        }
        else {
            Log.i(FRAGMENT_TAG,"Create animation programmatically");

            View view = inflater.inflate(R.layout.fragment_animations, container, false);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);

            Context context = getActivity();
            AnimationSet anim = new AnimationSet(context,null);

            ScaleAnimation sa = new ScaleAnimation(1,5,1,5,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
            sa.setDuration(5000);
            anim.addAnimation(sa);


            iv.setAnimation(anim);

            return view;
        }
    }


}
