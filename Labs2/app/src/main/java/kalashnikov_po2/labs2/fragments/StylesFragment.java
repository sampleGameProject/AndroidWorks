package kalashnikov_po2.labs2.fragments;



import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import kalashnikov_po2.labs2.MainActivity;
import kalashnikov_po2.labs2.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class StylesFragment extends BaseFragment {


    public StylesFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        useXML = getArguments().getBoolean(MainActivity.FragmentFactory.USE_XML);

        if(useXML){
            Log.i(FRAGMENT_TAG, "Create styles with xml");
            return inflater.inflate(R.layout.fragment_styles, container, false);
        }
        else{
            Log.i(FRAGMENT_TAG,"Create styles programmatically");

            // для того, чтобы кнопки были с нужными стилями нужно сделать xml заготовки
            // и использовать их с помощью LayoutInflater

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
