package com.mijan.classroutin;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {


    TextView currentTime;
    ImageView alarmStop;
    Ringtone r;

    Vibrator vibrator;
    String CurrentTime ;

    @Override
    public void onStart(){
        super.onStart();





        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        if(!r.isPlaying())
        {
            r.play();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        currentTime = findViewById(R.id.titleAlarm);
        alarmStop = findViewById(R.id.alarmStop);



        currentTime.setText("Class Start Now");

        alarmStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(r.isPlaying())
                {
                    r.stop();
                    Intent intent = new Intent(AlarmActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                r.stop();
                vibrator.cancel();
                Intent intent = new Intent(AlarmActivity.this,MainActivity.class);
                startActivity(intent);
                finish();




            }
        });

    }

}
