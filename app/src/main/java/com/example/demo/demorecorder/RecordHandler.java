package com.example.demo.demorecorder;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tfisher on 27/10/2016.
 * This class is responsible for updating the record button images and functionality, (setting up the microphone and file)
 * enabling/disabling the pause button, and updating the timer with the length of current recording
 */

public class RecordHandler {
    public enum State {
        RECORD(R.drawable.record_button),
        STOP(R.drawable.stop_button),
        RERECORD(R.drawable.rerecord_button);

        int imageId;

        State(int imageId) {
            this.imageId = imageId;
        }
    }

    private State state;
    private MainActivity activity;
    private Microphone microphone;
    private File outputFile;
    private Button microphoneButton;
    private Button playButton;
    private TextView recordLength;
    private Timer timer;
    private int recordingDuration;

    //defaults to RECORD state
    public RecordHandler(MainActivity activity, Button microphoneButton, Button playButton, TextView recordLength) {
        this.activity = activity;
        this.microphoneButton = microphoneButton;
        this.playButton = playButton;
        this.recordLength = recordLength;
        this.recordingDuration = 0;
        this.timer = new Timer();

        setState(State.RECORD);
    }

    public void click() throws Exception {
        switch(this.state) {
            case RECORD: //trigger job to request permission and begin recording
                activity.triggerJob(new RecordAudioJob(activity, this));
                break;
            case STOP:
                microphone.finish();
                pauseTimer();
                setState(State.RERECORD);
                Toast.makeText(activity.getApplicationContext(), "Recording finished", Toast.LENGTH_LONG).show();
                ButtonHelper.enable(playButton);
                break;
            case RERECORD:
                microphone.restart();
                createAndSetOutputFile();
                restartTimer();
                microphone.record();
                setState(State.STOP);
                ButtonHelper.disable(playButton);
                Toast.makeText(activity.getApplicationContext(), "Re-recording started", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void setState(State state) {
        this.state = state;
        microphoneButton.setBackgroundResource(this.state.imageId);
    }

    public void initializeAndRecord() throws IOException {
        if (getMicrophone() == null)
            setMicrophone(new Microphone());
        createAndSetOutputFile();
        getMicrophone().record();
        startTimer();
        setState(State.STOP);
        Toast.makeText(activity.getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
    }

    public Microphone getMicrophone() {
        return microphone;
    }

    public void setMicrophone(Microphone microphone) {
        this.microphone = microphone;
    }

    public TextView getRecordLength() {
        return recordLength;
    }

    public void createAndSetOutputFile() throws IOException {
        if (outputFile != null)
            outputFile.delete();
        outputFile = File.createTempFile("microphone_recording", ".3gp", activity.getCacheDir());
        outputFile.deleteOnExit();
        microphone.setOutputFile(outputFile.getAbsolutePath());
    }

    public String getOutputFilePath() {
        return outputFile.getAbsolutePath();
    }

    public void incrementRecordingDuration() {
        recordingDuration += 1;
    }

    public void resetRecordingDuration() {
        recordingDuration = 0;
    }

    public void pauseTimer() {
        timer.cancel();
    }

    public void restartTimer() {
        resetRecordingDuration();
        timer = new Timer();
        startTimer();
    }

    public void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(Timer_Tick);
            }

        }, 0, 1000);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.
            getRecordLength().setText(Utilities.formatSeconds(recordingDuration));
            incrementRecordingDuration();
        }
    };
}
