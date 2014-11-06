package pigareva_10moa.lab6_sensors;



import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroup);
        group.check(R.id.radioButton);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.radioButton:
                        MyActivity.RATE = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case R.id.radioButton2:
                        MyActivity.RATE = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case R.id.radioButton3:
                        MyActivity.RATE = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case R.id.radioButton4:
                        MyActivity.RATE = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    default:
                        break;
                }
            }
        });


        return view;
    }


}
