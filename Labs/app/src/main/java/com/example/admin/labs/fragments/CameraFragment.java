package com.example.admin.labs.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.labs.MainActivity;
import com.example.admin.labs.R;
import com.example.admin.labs.models.AlertDialogHelper;

import java.io.IOException;

public class CameraFragment extends MainActivityFragment implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    Camera camera;
    SurfaceView surfaceView;
    MediaRecorder recorder;

    private static final String FILE_OUTPUT = "";

    private static class CameraHelper{
        public static Camera getCameraInstance(){
            Camera c = null;
            try {
                c = Camera.open(); // attempt to get a Camera instance
            }
            catch (Exception e){
                // Camera is not available (in use or does not exist)
                Log.d(CAMERA_TAG,e.toString());
            }
            return c; // returns null if camera is unavailable
        }
    }

    static final String CAMERA_TAG = "CAMERA";

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();

        recorder = new MediaRecorder();
//        setupCamera();

        return view;
    }


    @Override
    public void onPause(){
        super.onPause();

        if (camera != null){
            camera.release();
            camera = null;
            holder.removeCallback(this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setupCamera();
    }

    private void setupCamera(){
        camera = CameraHelper.getCameraInstance();

        if(camera == null)
            AlertDialogHelper.showAlertView(getActivity(),"Не удалось подключится к камере. Возможно, камера используется другим процессом");
        else
            holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(CAMERA_TAG, "Error setting camera preview: " + e.getMessage());
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e){
            Log.d(CAMERA_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private void startRecording(){
        camera.unlock();

        recorder.setCamera(camera);
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        recorder.setOutputFile(FILE_OUTPUT);

        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialogHelper.showAlertView(getActivity(),e.toString());
            return;
        }

    }

    private void stopRecording(){
        recorder.stop();
        recorder.reset();
        recorder.release();
        camera.lock();
    }


}
