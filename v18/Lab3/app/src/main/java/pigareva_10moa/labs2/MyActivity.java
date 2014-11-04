package pigareva_10moa.labs2;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;


public class MyActivity extends BaseActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());


        View toastView = getLayoutInflater().inflate(R.layout.toast_layout,null);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
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
                    return createAnimationsView(inflater);
                case 2:
                    return createDrawableView(inflater);
            }
            return null;
        }

        private View createAnimationsView(LayoutInflater inflater) {
            View rootView = inflater.inflate(R.layout.fragment_animations, null, false);
            ImageView iv = (ImageView) rootView.findViewById(R.id.imageView);

            Context context = getActivity();
            AnimationSet anim = new AnimationSet(context,null);

            ScaleAnimation sa = new ScaleAnimation(1,5,1,5, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
            sa.setDuration(5000);
            anim.addAnimation(sa);
            anim.setRepeatMode(Animation.INFINITE);

            iv.setAnimation(anim);
            return rootView;
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
