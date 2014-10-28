package kalashnikov_po2.labs2.fragments;



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

import kalashnikov_po2.labs2.MainActivity;
import kalashnikov_po2.labs2.R;


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

            ScaleAnimation sa = new ScaleAnimation(1,2,1,2,0.5f,0.5f);
            sa.setDuration(2500);
            anim.addAnimation(sa);

            RotateAnimation ra = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(5000);
            anim.addAnimation(ra);

            TranslateAnimation ta = new TranslateAnimation(0,0,0,-100);
            ta.setDuration(2500);
            anim.addAnimation(ta);

            ScaleAnimation sa2 = new ScaleAnimation(1,0.5f,1,0.5f,0.5f,0.5f);
            sa2.setDuration(2500);
            sa2.setStartOffset(2500);
            anim.addAnimation(sa2);

            TranslateAnimation ta2 = new TranslateAnimation(0,0,0,100);
            ta2.setStartOffset(2500);
            ta2.setDuration(2500);
            anim.addAnimation(ta2);

            iv.setAnimation(anim);

            return view;
        }
    }


}
