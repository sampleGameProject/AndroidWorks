package kalevko_10po2.labs.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kalevko_10po2.R;


public class SoundFragment extends Fragment implements View.OnClickListener{

    Button button;
    Vibrator v;

    public SoundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sound, container, false);

        button = (Button) view.findViewById(R.id.buttonPlay);
        button.setOnClickListener(this);

        v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        return view;
    }

    MediaPlayer mediaPlayer;

    @Override
    public void onClick(View view) {

        v.vibrate(500);

        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.pobediteley_ne_sudyat);
            mediaPlayer.start();
            button.setText("Stop");
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            button.setText("Play");
        }

    }

    @Override
    public void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
