package kalashnikov_10po2.labs2;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
    }


    private static Fragment createAnimationsFragment() {
        return null;
    }

    private static Fragment createStylesFragment() {
        return null;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 0:
                    return createStylesView(inflater);
                case 1:
                    return createAnimationsView(inflater,container);
                case 2:
                    return createDrawableView(inflater);
            }
            return null;
        }

        private View createDrawableView(LayoutInflater inflater) {
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

        private View createAnimationsView(LayoutInflater inflater,ViewGroup container) {
            View view = inflater.inflate(R.layout.fragment_animations, container, false);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);

            Context context = getActivity();
            AnimationSet anim = new AnimationSet(context,null);

            ScaleAnimation sa = new ScaleAnimation(5,1,5,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
            sa.setDuration(5000);
            anim.addAnimation(sa);


            iv.setAnimation(anim);

            return view;
        }

        private View createStylesView(LayoutInflater inflater) {
            Context context = getActivity();

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            linearLayout.setLayoutParams(params);

            LinearLayout layout1 = new LinearLayout(context);
            layout1.setOrientation(LinearLayout.HORIZONTAL);
            layout1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            Button btn1 = (Button) inflater.inflate(R.layout.def_button1,null);
            btn1.setText(getResources().getText(R.string.button1Text));
            EditText ed1 = new EditText(context);
            ed1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1));

            layout1.addView(btn1);
            layout1.addView(ed1);

            linearLayout.addView(layout1);

            LinearLayout layout2 = new LinearLayout(context);
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layout2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            Button btn2 = (Button) inflater.inflate(R.layout.button2,null);
            btn2.setText(getResources().getText(R.string.button2text));
            TextView tv1 = (TextView) inflater.inflate(R.layout.def_text,null);
            tv1.setText(getResources().getText(R.string.button3text));
            layout2.addView(btn2);
            layout2.addView(tv1);

            linearLayout.addView(layout2);

            LinearLayout layout3 = new LinearLayout(context);
            layout3.setOrientation(LinearLayout.HORIZONTAL);
            layout3.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            Button btn3 = (Button) inflater.inflate(R.layout.button1,null);
            btn3.setText(getResources().getText(R.string.button4text));
            Button btn4 = (Button) inflater.inflate(R.layout.button2,null);
            btn4.setText(getResources().getText(R.string.button5text));
            layout3.addView(btn3);
            layout3.addView(btn4);

            linearLayout.addView(layout3);

            RadioGroup rg = new RadioGroup(context);

            RadioButton rb1 = new RadioButton(context);
            rb1.setText(getResources().getText(R.string.radio1));
            RadioButton rb2 = new RadioButton(context);
            rb2.setText(getResources().getText(R.string.radio2));
            RadioButton rb3 = new RadioButton(context);
            rb3.setText(getResources().getText(R.string.radio3));

            rg.addView(rb1);
            rg.addView(rb2);
            rg.addView(rb3);
            linearLayout.addView(rg);

            return linearLayout;
        }

    }


}
