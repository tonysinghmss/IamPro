package com.tony.improve.iampro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private TextView time_remaining;
    private boolean timerHasStarted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //countDownTimer = new NotifyCountDownTimer(52,1);
        time_remaining = (TextView) findViewById(R.id.time_remaining);
        if (!timerHasStarted) {
            Log.i( TAG, "Starting main activity" );
            timerHasStarted = true;
            //start the Timer service which will run the countdown timer
            time_remaining.setText("52");
            startService(new Intent(this, TimerService.class));
        }
    }

    private BroadcastReceiver timerCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "On receiving intent");
            String action = intent.getAction();
            if(action.equalsIgnoreCase("com.tony.improve.iampro.timer_count")) {
                String timerCount = intent.getExtras().getString("timer_count");
                time_remaining.setText(timerCount);
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        registerReceiver(timerCountReceiver, new IntentFilter( TimerService.TIMER_BR));

    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(timerCountReceiver);
    }

    @Override
    public void onStop(){
        try{
            unregisterReceiver(timerCountReceiver);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onDestroy(){
        stopService(new Intent(this,TimerService.class));
        super.onDestroy();
    }
}
