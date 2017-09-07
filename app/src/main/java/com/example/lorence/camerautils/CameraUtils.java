package com.example.lorence.camerautils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.PreviewActivity;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;

import java.io.File;

public class CameraUtils extends AppCompatActivity {

    private FlashSwitchView cameraFlash;
    private CameraSwitchView cameraSwitcher;
    private RecordButton cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_utils);
        initViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.PERMISSIONS_REQUEST_CAMERA);
                return;
            }
        }
        addCamera();
    }

    private void initViews() {
        cameraFlash = (FlashSwitchView) this.findViewById(R.id.camera_flash);
        cameraSwitcher = (CameraSwitchView) this.findViewById(R.id.camera_switcher);
        cameraButton = (RecordButton) this.findViewById(R.id.camera_button);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addCamera();
                } else {
                    LogUtils.log(Log.INFO, "SendFaxActivity","Permission is denied");
                    return;
                }
                break;
        }
    }

    private void addCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Configuration.Builder builder = new Configuration.Builder();
        builder
                .setCamera(Configuration.CAMERA_FACE_REAR)
                .setFlashMode(Configuration.FLASH_MODE_OFF);

        final CameraFragment cameraFragment = CameraFragment.newInstance(builder.build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
        cameraButton.setEnabled(true);

        if (cameraFragment != null) {
            cameraFragment.setResultListener(new CameraFragmentResultListener() {
                @Override
                public void onVideoRecorded(String filePath) {
                }

                @Override
                public void onPhotoTaken(byte[] bytes, String filePath) {
                    try {
                        Intent resultIntent = PreviewActivity.newIntentPhoto(CameraUtils.this, filePath);
                        resultIntent.putExtra(String.valueOf(Constants.CAMERA_FRAGMENT), filePath);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } catch (Exception ex) {
                        LogUtils.log(Log.INFO, "CameraUtils","Error from onPhotoTaken "+ex.getMessage());
                        finish();
                    }
                }
            });

            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {

                @Override
                public void onCurrentCameraBack() {
                    cameraSwitcher.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    cameraSwitcher.displayFrontCamera();
                }

                @Override
                public void onFlashAuto() {
                    cameraFlash.displayFlashOff();
                }

                @Override
                public void onFlashOn() {
                    cameraFlash.displayFlashOn();
                }

                @Override
                public void onFlashOff() {
                    cameraFlash.displayFlashOff();
                }

                @Override
                public void onCameraSetupForPhoto() {
                    cameraButton.displayPhotoState();
                    cameraFlash.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCameraSetupForVideo() {
                }

                @Override
                public void shouldRotateControls(int degrees) {
                    ViewCompat.setRotation(cameraSwitcher, degrees);
                    ViewCompat.setRotation(cameraFlash, degrees);
                }

                @Override
                public void onRecordStateVideoReadyForRecord() {
                }

                @Override
                public void onRecordStateVideoInProgress() {
                }

                @Override
                public void onRecordStatePhoto() {
                    cameraButton.displayPhotoState();
                }

                @Override
                public void onStopVideoRecord() {
                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                }
            });

            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                @Override
                public void lockControls() {
                }

                @Override
                public void unLockControls() {
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                    cameraSwitcher.setVisibility(allow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void allowRecord(boolean allow) {
                    cameraButton.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                }
            });
        }
    }

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public void onBackPressed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CameraUtils.this, SendFaxActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, Constants.DOUBLE_CLICK_TIME_DELTA);
    }

}
