package com.example.demo.demorecorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.job.PermissionActivity;

import java.io.IOException;

public class MainActivity extends PermissionActivity {
    public Button microphoneButton, playButton;
    public TextView recordLength;
    Microphone microphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Utilities.clearCache(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        microphoneButton = (Button) findViewById(R.id.microphoneButton);
        playButton = (Button) findViewById(R.id.playButton);
        recordLength = (TextView) findViewById(R.id.recordLength);
        final RecordHandler recordButtonHandler = new RecordHandler(this, microphoneButton, playButton, recordLength);

        microphoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    recordButtonHandler.click();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), ("Something went wrong. Unable to record audio: " + e.toString()), Toast.LENGTH_LONG).show();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(recordButtonHandler.getOutputFilePath());
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });


        microphoneButton.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Utilities.clearCache(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDebugDialog() {
        AlertDialog.Builder builder = new DebugDialogBuilder(this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug:
                showDebugDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
