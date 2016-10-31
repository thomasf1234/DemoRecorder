package com.example.demo.demorecorder;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.demo.job.PermissionJob;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tfisher on 25/10/2016.
 */

public class RecordAudioJob extends PermissionJob {
    public static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO};
    public static final int PERMISSION_REQUEST_ID = 0;

    private RecordHandler recordButtonHandler;

    public RecordAudioJob(AppCompatActivity appCompatActivity, RecordHandler recordButtonHandler) {
        super(appCompatActivity);
        this.recordButtonHandler = recordButtonHandler;
    }

    @Override
    public void perform() throws Exception {
        recordButtonHandler.initializeAndRecord();
    }

    @Override
    public int getPermissionRequestId() {
        return PERMISSION_REQUEST_ID;
    }

    @Override
    public String[] getPermissions() {
        return PERMISSIONS;
    }
}






