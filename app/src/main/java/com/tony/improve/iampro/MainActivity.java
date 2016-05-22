package com.tony.improve.iampro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String TIMER_VALUE = "com.tony.improve.iampro.timerValue";
    private TextView time_remaining;
    private TextView user_msg;
    private boolean workingTime, relaxTime;
    //For implementing "Click the back button twice"
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //countDownTimer = new NotifyCountDownTimer(52,1);
        user_msg = (TextView) findViewById(R.id.user_msg);
        time_remaining = (TextView) findViewById(R.id.time_remaining);

        if (!workingTime) {
            Log.i( TAG, "Starting main activity" );
            workingTime = true;
            relaxTime = false;
            //start the Timer service which will run the countdown timer
            user_msg.setText("Keep working hard. You can do it.");
            Intent i = new Intent(this, TimerService.class);
            i.putExtra(TIMER_VALUE, 2L);//52
            startService(i);
        }
    }

    private BroadcastReceiver timerCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "On receiving intent");
            String action = intent.getAction();
            if(action.equalsIgnoreCase("com.tony.improve.iampro.timer_count")) {
                String timerCount = intent.getExtras().getString("timer_count");
                Log.i(TAG, " Intent has timer count is "+timerCount);
                if(timerCount.equalsIgnoreCase("0") && workingTime && !relaxTime){
                    //When working time gets over
                    Intent i = new Intent(context,TimerService.class);
                    i.putExtra(TIMER_VALUE,2L);//17
                    user_msg.setText("Easy Tiger. You have given your best.");
                    workingTime = false;
                    relaxTime = true;
                    Log.i(TAG, "Start service when working time is over.");
                    startService(i);
                }
                else if(timerCount.equalsIgnoreCase("0") && !workingTime && relaxTime){
                    //When relaxing time gets over
                    Intent restartIntent = new Intent(context, MainActivity.class);
                    Log.i(TAG, "Start service when relaxing time is over.");
                    startActivity(restartIntent);
                }
                else if(!timerCount.equalsIgnoreCase("0") && (workingTime || relaxTime)){
                    Log.i(TAG, "Set the timer count in view "+timerCount);
                    time_remaining.setText(timerCount);
                }
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
    public void onDestroy() {
        stopService(new Intent(this, TimerService.class));
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 2000);
    }
}
