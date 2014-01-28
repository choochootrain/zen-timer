package com.choochootrain.zentimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.res.Resources;
import android.content.Intent;

import com.choochootrain.zentimer.adapter.NavigationListAdapter;

public class TimerActivity extends Activity {
    public static long MINUTES = 1000 * 60;

    private boolean running = false;

    private Vibrator vibrator;
    private Resources res;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItems;
    private Chronometer chronometer;
    private Button timerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        setContentView(R.layout.activity_timer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.navigation_drawer);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
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
                if (elapsedTime >= 1 * MINUTES) {
                    chronometer.stop();
                    vibrator.vibrate(new long[] {300L, 200L, 300L, 200L, 1000L}, -1);
                    stopTimer();
                }
            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!running)
                    startTimer();
                else
                    stopTimer();

                running = !running;
            }
        });
    }

    private void startTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        timerButton.setText(R.string.timer_button_stop);
    }

    private void stopTimer() {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        timerButton.setText(R.string.timer_button_start);
    }

    private class NavigationListClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (drawerItems[position].equals(res.getString(R.string.nav_duration)))
            Toast.makeText(this, "Time", Toast.LENGTH_SHORT).show();
        else if (drawerItems[position].equals(res.getString(R.string.nav_sound)))
            Toast.makeText(this, "Sound", Toast.LENGTH_SHORT).show();
        else if (drawerItems[position].equals(res.getString(R.string.nav_vibrate)))
            Toast.makeText(this, "Vibrate", Toast.LENGTH_SHORT).show();
        else if (drawerItems[position].equals(res.getString(R.string.nav_about))) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(drawerList);
        }
    }
}
