package com.mijan.classroutin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


      /* MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);

        mediaPlayer.start();*/

        intent = new Intent();
        intent.setClass(context, AlarmActivity.class); //Test is a dummy class name where to redirect
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);







    }
}
