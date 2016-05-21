package com.tony.improve.iampro;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service {
    public static final String TAG = "TimerService";
    public static final String TIMER_BR = "com.tony.improve.iampro.timer_count";
    NotifyCountDownTimer cntTimer = null;


    @Override
    public void onCreate(){
        super.onCreate();
        //TODO: Initialize the AsyncTask to start the countdown timer
        Log.i(TAG, "Starting Timer Service");
        cntTimer = new NotifyCountDownTimer(52,1);
        cntTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent,flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class NotifyCountDownTimer extends CountDownTimer {

        public NotifyCountDownTimer(long startTime, long interval) {
            super(startTime*60000, interval*60000);
            Log.i(TAG, "Countdown Timer started with time"+String.valueOf(startTime));

        }

        @Override
        public void onFinish() {
            //time_remaining.setText("Congratulations");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i(TAG, "On Tick: "+String.valueOf(millisUntilFinished/60000));
            Intent notifyMainAct = new Intent(TIMER_BR);
            notifyMainAct.putExtra("timer_count",String.valueOf(millisUntilFinished/60000));
            sendBroadcast(notifyMainAct);
        }
    }
}