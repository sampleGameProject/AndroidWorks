package kalevko_10po2.labs2;

import android.app.Activity;
import android.util.Log;

/**
 * Created by admin on 29.10.2014.
 */
public class BaseActivity extends Activity {

    private final String TAG = "TAG";

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG,"onStart");
    }


    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG,"onStop");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG,"onRestart");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"onPause");
    }
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}
