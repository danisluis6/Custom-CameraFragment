package com.example.lorence.camerautils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class SendFaxActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgCameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_fax);
        imgCameraFragment = (ImageView)this.findViewById(R.id.imgCameraFragment);
        imgCameraFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.PERMISSIONS_REQUEST_CAMERA);
                return;
            }
        }
        Intent intent = new Intent(SendFaxActivity.this, CameraUtils.class);
        startActivityForResult(intent, Constants.CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(SendFaxActivity.this, CameraUtils.class);
                    startActivityForResult(intent, Constants.CAMERA_REQUEST);
                } else {
                    LogUtils.log(Log.INFO, "SendFaxActivity","Permission is denied");
                    return;
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        LogUtils.log(Log.INFO, "SendFaxActivity","Data is returned from "+data.toString());
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
