package kalevko_10po2.labs2.fragments;



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

import kalevko_10po2.labs2.MainActivity;
import kalevko_10po2.labs2.R;


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

            TranslateAnimation ta = new TranslateAnimation(0,-100,0,0);
            ta.setDuration(5000);
            ta.setFillAfter(true);
            anim.addAnimation(ta);


            iv.setAnimation(anim);

            return view;
        }
    }


}
