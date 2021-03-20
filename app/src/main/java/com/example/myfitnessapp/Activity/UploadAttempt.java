package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfitnessapp.R;

import java.util.Collections;

public class UploadAttempt extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE =3142325;
    TextureView textureView;
    CameraManager cameraManager;
    Size size;
    CameraDevice cameraDevice;
    String cameraId;
    Handler backgroundHandler;
    HandlerThread handlerThread;


    ImageView imageViewUploadButton;

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice =camera;
            createCaptureSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_attempt);
        //Initialising  the TextureView
        textureView = findViewById(R.id.texture_View);
        imageViewUploadButton = findViewById(R.id.ivUploadBtn);
        imageViewUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadAttempt.this, "Start Recording", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openBackgroundHandler();
                setUpCamera();
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeBackgroundHandler();
    }

    private void openCamera(){
        //to check for the permissions;
        //camera2 api is available is available for lolipop but to check it isnt available and hence we will first check the android version
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
            }
            else{

                try {
                    cameraManager.openCamera(cameraId,stateCallback,backgroundHandler);
                } catch (CameraAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private void openBackgroundHandler(){
        //this func is to avoid the lag in the thread
        handlerThread = new HandlerThread("camera_app");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }
    private void closeBackgroundHandler(){
        handlerThread.quit();
        handlerThread = null;
        backgroundHandler = null;

    }
    private void setUpCamera(){
        //initiallising camera manager
        cameraManager =(CameraManager) getSystemService(CAMERA_SERVICE);
        //this will throw an execption and hence we need to surround it with a try and catch
        try {

            String[] cameraIds = cameraManager.getCameraIdList();

            //use an advanced for loop to loop through the ids and check the selected
            for(String cameraId: cameraIds){
                CameraCharacteristics cameraCharacteristics= cameraManager.getCameraCharacteristics(cameraId);

                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_FRONT) {
                    this.cameraId =cameraId;
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    size = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];

                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void createCaptureSession() {
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
        final Surface surface = new Surface(surfaceTexture);
        try {
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);//here put template Record
                        captureRequestBuilder.addTarget(surface);
                        session.setRepeatingRequest(captureRequestBuilder.build(),null,backgroundHandler);
                    } catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, backgroundHandler);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }
}
