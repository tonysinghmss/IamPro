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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "Starting Timer Service");
        long timerValue = (Long)intent.getExtras().get(MainActivity.TIMER_VALUE);
        cntTimer = new NotifyCountDownTimer(timerValue,1,startId);
        cntTimer.start();
        return super.onStartCommand(intent,flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class NotifyCountDownTimer extends CountDownTimer {
        private int serviceStartId;
        public NotifyCountDownTimer(long startTime, long interval, int srvStartId) {
            super(startTime*60000, interval*60000);
            Log.i(TAG, "Countdown Timer started with time "+String.valueOf(startTime));
            serviceStartId = srvStartId;
            Log.i(TAG, "Service startId is "+serviceStartId);
        }

        @Override
        public void onFinish() {
            //time_remaining.setText("Congratulations");
            Log.i(TAG, "Timer finished");
            stopSelf(serviceStartId);
            Intent notifyMainAct = new Intent(TIMER_BR);
            notifyMainAct.putExtra("timer_count",String.valueOf(0));
            sendBroadcast(notifyMainAct);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minRemaining = millisUntilFinished/60000;
            Log.i(TAG, "On Tick: "+String.valueOf(minRemaining));
            Intent notifyMainAct = new Intent(TIMER_BR);
            notifyMainAct.putExtra("timer_count",String.valueOf(minRemaining));
            sendBroadcast(notifyMainAct);
        }
    }
}