package com.choochootrain.zentimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class TimerActivity extends Activity {
    public static long MINUTES = 1000 * 60;

    private boolean running = false;
    private long startTime = 0;

    private Vibrator vibrationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibrationManager = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        setContentView(R.layout.activity_timer);

        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        final Button timerButton = (Button) findViewById(R.id.timer_button);

        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                running = !running;
                if (running) {
                    startTime = SystemClock.elapsedRealtime();
                    chronometer.setBase(startTime);
                    chronometer.start();
                    timerButton.setText(R.string.timer_button_stop);
                } else {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timerButton.setText(R.string.timer_button_start);
                }
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long currentTime = SystemClock.elapsedRealtime();
                if (currentTime - startTime == 1 * MINUTES) {
                    chronometer.stop();
                    vibrationManager.vibrate(new long[] {300L, 200L, 300L, 200L, 1000L}, -1);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
