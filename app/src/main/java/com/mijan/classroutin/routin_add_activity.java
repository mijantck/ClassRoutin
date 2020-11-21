package com.mijan.classroutin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.NoteRoutin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class routin_add_activity extends AppCompatActivity {

    ProgressDialog progressDialog;

    AlarmManager alarmManager;
    private AdView mAdView;

    String Did,  upsuject, uproom_number, upteacher, upendTime;

       int upserial;
       long upstartTime;

    EditText  subject, roome, techer, day_view;
    int serial;
    Button add_button;
    TextView start_time, end_time, idview;

    CheckBox checkBox_sun, checkBox_mon, checkBox_tue,
            checkBox_wed, checkBox_thu, checkBox_fri, checkBox_sat;

    ArrayList<String> day1;
    ArrayList day;

    //Firestore User Find
    String uid = FirebaseAuth.getInstance().getUid();
    CollectionReference notebookRef = FirebaseFirestore.getInstance()
            .collection("Users").document(uid).collection("Routin");
    String id = UUID.randomUUID().toString();

    long time1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routin_add_activity);

        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("Routin Added");

        findid();
        //update data section

        Bundle bundle = getIntent().getExtras();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (bundle != null) {

            setTitle("Routine Update");

            day1 = new ArrayList<>();



            Did = bundle.getString("id");
            upserial = bundle.getInt("upserial");
            upsuject = bundle.getString("upsubject");
            uproom_number = bundle.getString("uproom_number");
            upteacher = bundle.getString("upteacher");
           // upstartTime = bundle.getString("up_startTime");
            upstartTime = bundle.getLong("up_startTime");
            upendTime = bundle.getString("upendTime");
            day1 = bundle.getStringArrayList("day");

            //set data
            idview.setText(Did);
            add_button.setText("Update");

            serial = upserial;
            subject.setText(upsuject);
            roome.setText(uproom_number);
            techer.setText(upteacher);
            start_time.setText(getTimee(upstartTime));
            end_time.setText(upendTime);

            if (day1.contains("SunDay")) {
                checkBox_sun.setChecked(true);
            }else {
                checkBox_mon.setChecked(false);
            }
            if (day1.contains("MonDay")) {
                checkBox_mon.setChecked(true);
            }else {
                checkBox_mon.setChecked(false);
            }if (day1.contains("TuesDay")) {
                checkBox_tue.setChecked(true);
            }else {
                checkBox_tue.setChecked(false);
            }if (day1.contains("WednesDay")) {
                checkBox_wed.setChecked(true);
            }else {
                checkBox_wed.setChecked(false);
            }if (day1.contains("ThursDay")) {
                checkBox_thu.setChecked(true);
            }else {
                checkBox_thu.setChecked(false);
            }if (day1.contains("FriDay")) {
                checkBox_fri.setChecked(true);
            }else {
                checkBox_fri.setChecked(false);
            }if (day1.contains("SaturDay")) {
                checkBox_sat.setChecked(true);
            }else {
                checkBox_sat.setChecked(false);
            }




        } else {

        }


        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stat_time();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_time();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(routin_add_activity.this);
                 // Setting Message
                progressDialog.setTitle("Updating..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                // Progress Dialog Style Spinner
                Bundle bundle1 = getIntent().getExtras();

                if (bundle1 != null) {

                    daylirt();

                    String id = Did;
                    int Serial = 0;
                    String Subject = subject.getText().toString();
                    String Roome = roome.getText().toString();
                    String Techer = techer.getText().toString();
                    long Start_Time = upstartTime;
                    String End_Time = end_time.getText().toString();
                    String Day = day_view.getText().toString();
                    String[] dayArray = Day.split("\\s*,\\s*");
                    List<String> tags = Arrays.asList(dayArray);

                    updateData(id, Serial, Subject, Roome, Techer, Start_Time, End_Time,tags);

                    cancel(Start_Time );

                } else {

                    daylirt();

                    Adde_data();


                }


            }
        });
    }

    private void updateData(String id, int serial, String subject, String roome,
                            String techer, long start_time, String end_time, List<String> day) {



        DocumentReference unotebookRef =FirebaseFirestore.getInstance()
                .collection("Users").document(uid)
                .collection("Routin").document(id);
        unotebookRef
                .update("serial", serial,
                        "subject", subject,
                        "roome", roome,
                        "techer", techer,
                        "start_time",start_time,
                        "end_time",end_time,
                        "day",day)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(routin_add_activity.this, "Update is complete", Toast.LENGTH_SHORT).show();

                        progressDialog = new ProgressDialog(routin_add_activity.this);
                       // Setting Message


                        progressDialog.setTitle("Updating"); // Setting Title
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(routin_add_activity.this, "Update is incomplete ", Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void Adde_data() {



        int Serial = serial;
        String Subject = subject.getText().toString();
        String Roome = roome.getText().toString();
        String Techer = techer.getText().toString();
        long Start_Time = time1/1000;
        String End_Time = end_time.getText().toString();
        String Day = day_view.getText().toString();
        String[] dayArray = Day.split("\\s*,\\s*");
        List<String> tags = Arrays.asList(dayArray);


        if (Day.trim().isEmpty() || Subject.trim().isEmpty() || Roome.trim().isEmpty() || Techer.trim().isEmpty()) {
            Toast.makeText(this, "Please insert All Data", Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.show(); // Display Progress Dialog


        notebookRef.add(new NoteRoutin(Serial, Subject, Roome, Techer, Start_Time, End_Time, tags));
        Toast.makeText(this, "Routine added", Toast.LENGTH_SHORT).show();

        progressDialog.dismiss();

        finish();


    }


    public void findid() {

        add_button = findViewById(R.id.add_button);

        //Edite Text
        idview = findViewById(R.id.idView);
      //  serial = findViewById(R.id.serial);
        subject = findViewById(R.id.subject);
        roome = findViewById(R.id.room);
        techer = findViewById(R.id.techer);
        day_view = findViewById(R.id.day_view);

        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);


        //CheckBox
        checkBox_sun = findViewById(R.id.checkbox_sun);
        checkBox_mon = findViewById(R.id.checkbox_mon);
        checkBox_tue = findViewById(R.id.checkbox_tue);
        checkBox_wed = findViewById(R.id.checkbox_wed);
        checkBox_thu = findViewById(R.id.checkbox_thu);
        checkBox_fri = findViewById(R.id.checkbox_fri);
        checkBox_sat = findViewById(R.id.checkbox_sat);

        day = new ArrayList<>();
    }

    //time
    public void stat_time() {

        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final int secent = mcurrentTime.get(Calendar.SECOND);


        final TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String period;
                int hour = 0;
                String minute;
                if (selectedMinute >= 0 && selectedMinute < 10)
                    minute = "0" + selectedMinute;
                else
                    minute = String.valueOf(selectedMinute);
                if (selectedHour >= 0 && selectedHour < 12)
                    period = "AM";
                else
                    period = "PM";
                if (period.equals("AM")) {
                    if (selectedHour == 0)
                        hour = 12;
                    else if (selectedHour >= 1 && selectedHour < 12)
                        hour = selectedHour;
                } else if (period.equals("PM")) {
                    if (selectedHour == 12)
                        hour = 12;
                    else if (selectedHour >= 13 && selectedHour <= 23)
                        hour = selectedHour - 12;
                }

                String time = hour + ":" + minute + " " + period;

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY,selectedHour);
                cal.set(Calendar.MINUTE,selectedMinute);

                 time1 = cal.getTimeInMillis();
                upstartTime=time1/1000;
                start_time.setText(time);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void end_time() {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String period;
                int hour = 0;
                String minute;
                if (selectedMinute >= 0 && selectedMinute < 10)
                    minute = "0" + selectedMinute;
                else
                    minute = String.valueOf(selectedMinute);
                if (selectedHour >= 0 && selectedHour < 12)
                    period = "AM";
                else
                    period = "PM";
                if (period.equals("AM")) {
                    if (selectedHour == 0)
                        hour = 12;
                    else if (selectedHour >= 1 && selectedHour < 12)
                        hour = selectedHour;
                } else if (period.equals("PM")) {
                    if (selectedHour == 12)
                        hour = 12;
                    else if (selectedHour >= 13 && selectedHour <= 23)
                        hour = selectedHour - 12;
                }
                String time = hour + ":" + minute + " " + period;
                end_time.setText(time);

            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void daylirt() {

        day_view.setText("");
        if (checkBox_sun.isChecked()) {
            day.add("SunDay");
        } else {
            day.remove("SunDay");
        }
        if (checkBox_mon.isChecked()) {
            day.add("MonDay");
        } else {
            day.remove("MonDay");
        }
        if (checkBox_tue.isChecked()) {
            day.add("TuesDay");
        } else {
            day.remove("TuesDay");
        }
        if (checkBox_wed.isChecked()) {
            day.add("WednesDay");
        } else {
            day.remove("WednesDay");
        }
        if (checkBox_thu.isChecked()) {
            day.add("ThursDay");
        } else {
            day.remove("ThursDay");
        }
        if (checkBox_fri.isChecked()) {
            day.add("FriDay");
        } else {
            day.remove("FriDay");
        }
        if (checkBox_sat.isChecked()) {
            day.add("SaturDay");
        } else {
            day.remove("SaturDay");
        }
        for (Object day : day) {

            day_view.append(day + ",");
        }
        day.clear();
    }

    private void cancel(long c) {


        String input1 = c+"";     //input string
        String lastFourDigits = "";     //substring containing last 4 characters

        if (input1.length() > 4)
        {
            lastFourDigits = input1.substring(input1.length() - 4);
        }
        else
        {
            lastFourDigits = input1;
        }
        int fourDigiteNuber = Integer.valueOf(lastFourDigits)+1 ;


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,fourDigiteNuber,intent,PendingIntent.FLAG_CANCEL_CURRENT);



        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);



    }

    private String getTimee(long timestamp) {
        long ts = timestamp * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time = sdf.format(new Date(ts));
        return time;
    }
}
