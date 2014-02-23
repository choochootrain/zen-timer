package com.choochootrain.zentimer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import android.content.Intent;

import com.choochootrain.zentimer.adapter.NavigationListAdapter;
import com.choochootrain.zentimer.pwm.PWMBuilder;

public class TimerActivity extends Activity {
    public static long MINUTES = 1000 * 60;
    public static int MAX_DURATION = 15;

    private int duration_minutes = 1;

    private boolean running = false;

    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private Resources res;
    private SharedPreferences preferences;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItems;
    private Chronometer chronometer;
    private TextView timerDuration;
    private Button timerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        res = getResources();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.bowl);
        preferences = getPreferences(Context.MODE_PRIVATE);

        setContentView(R.layout.activity_timer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.navigation_drawer);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        timerDuration = (TextView) findViewById(R.id.timer_duration);
        timerButton = (Button) findViewById(R.id.timer_button);

        drawerItems = res.getStringArray(R.array.navigation_items);
        drawerList.setOnItemClickListener(new NavigationListClickListener());
        drawerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        NavigationListAdapter adapter = new NavigationListAdapter(this);
        adapter.addItem(drawerItems[0], NavigationListAdapter.TYPE_ITEM);
        adapter.addItem(drawerItems[1], NavigationListAdapter.TYPE_SWITCH);
        adapter.addItem(drawerItems[2], NavigationListAdapter.TYPE_SWITCH);
        adapter.addItem(drawerItems[3], NavigationListAdapter.TYPE_ITEM);
        drawerList.setAdapter(adapter);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                if (elapsedTime >= duration_minutes * MINUTES) {
                    stopTimer();
                }
            }
        });

        updateDuration();

        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!running)
                    startTimer();
                else
                    resetTimer();

                running = !running;
            }
        });
    }

    private void updateDuration() {
        timerDuration.setText(duration_minutes + " minute timer");
    }

    private void startTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        timerButton.setText(R.string.timer_button_stop);
    }

    private void resetTimer() {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        timerButton.setText(R.string.timer_button_start);
    }

    private void stopTimer() {
        chronometer.stop();

        boolean sound = preferences.getBoolean("sound", false);
        boolean vibrate = preferences.getBoolean("vibrate", false);

        if (sound) {
            mediaPlayer.start();
        }

        if (vibrate) {
            vibrator.vibrate(new long[]{0l, 250l, 125l, 250l, 125l, 250l, 125l, 500l}, -1);
        }

        resetTimer();
    }

    private class NavigationListClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(view, position);
        }
    }

    private void selectItem(View view, int position) {
        if (drawerItems[position].equals(res.getString(R.string.nav_duration))) {
            duration_minutes  = (duration_minutes + 1) % MAX_DURATION;
            updateDuration();
        } else if (drawerItems[position].equals(res.getString(R.string.nav_sound))) {
            CheckedTextView soundPreference = (CheckedTextView)view.findViewById(android.R.id.text1);
            preferences.edit().putBoolean("sound", soundPreference.isChecked()).commit();
        } else if (drawerItems[position].equals(res.getString(R.string.nav_vibrate))) {
            CheckedTextView vibratePreference = (CheckedTextView)view.findViewById(android.R.id.text1);
            preferences.edit().putBoolean("vibrate", vibratePreference.isChecked()).commit();
        } else if (drawerItems[position].equals(res.getString(R.string.nav_about))) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(drawerList);
        }
    }
}
