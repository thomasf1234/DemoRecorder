package com.example.demo.demorecorder;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by tfisher on 26/10/2016.
 */

public class Microphone extends MediaRecorder {
    enum State {
        NOT_RECORDING, RECORDING
    }

    private State state;

    public Microphone() throws IOException {
        initialize();
    }

    public void record() throws IOException {
        setState(State.RECORDING);
        prepare();
        start();
    }

    public void finish() {
        stop();
        setState(State.NOT_RECORDING);
    }

    public boolean isRecording(){
        return this.state == State.RECORDING;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void restart() {
        reset();
        initialize();
    }

    private void initialize() {
        setAudioSource(MediaRecorder.AudioSource.MIC);
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        this.state = State.NOT_RECORDING;
    }

//    public URI getOutputFileURI() {
//        return this.outputFile.toURI();
//    }
}
