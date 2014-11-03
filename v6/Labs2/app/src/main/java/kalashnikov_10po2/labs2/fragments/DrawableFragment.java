package kalashnikov_10po2.labs2.fragments;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import kalashnikov_10po2.labs2.MainActivity;
import kalashnikov_10po2.labs2.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DrawableFragment extends BaseFragment {


    public DrawableFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        useXML = getArguments().getBoolean(MainActivity.FragmentFactory.USE_XML);

        if(useXML){
            Log.i(FRAGMENT_TAG,"Create drawable from xml");
            return inflater.inflate(R.layout.fragment_drawable, container, false);
        }
        else {
            Log.i(FRAGMENT_TAG,"Create drawable programmatically");

            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.setIntrinsicHeight(100);
            shapeDrawable.setIntrinsicWidth(100);
            shapeDrawable.getPaint().setColor(getResources().getColor(R.color.color));

            RelativeLayout rl = new RelativeLayout(getActivity());

            ImageView iv = new ImageView(getActivity());
            iv.setImageDrawable(shapeDrawable);

            iv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            rl.addView(iv);

            ImageView iv2 = new ImageView(getActivity());
            iv2.setImageResource(R.drawable.sample);
            iv2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            rl.addView(iv2);
            return rl;
        }
    }


}
