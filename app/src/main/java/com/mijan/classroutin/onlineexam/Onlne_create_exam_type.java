package com.mijan.classroutin.onlineexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.OnlineExamViewNote;
import com.mijan.classroutin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Onlne_create_exam_type extends AppCompatActivity {


    TextView start_date,start_time,end_time;

    private int mYear, mMonth, mDay, mHour, mMinute;
    int setyear,setmont,setday;


    Button examcreateButton;
    String id, courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher,examtype;

    String examid,courCretorid,examType,examName,examNameCatagory,examDuretion,examMarks,examStartDate,examTime,examStopTime;
    long examsartDateLong,examStartTimeLong,examEndTimeLong;


    boolean select_date = false;
    boolean select_end_time = false;
    boolean select_start_time = false;
    TextInputEditText createexamMarks,createexamCatagory,createexamName;


    long startmili;
    long endmili;
    long duration;

    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlne_create_exam_type);

        Toolbar toolbar = findViewById(R.id.toolbar_ID_crete_exam);
        toolbar.setTitle(" Crete Exam ");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(Onlne_create_exam_type.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        start_date = findViewById(R.id.start_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        examcreateButton = findViewById(R.id.examcreateButton);
        createexamMarks = findViewById(R.id.createexamMarks);
        createexamCatagory = findViewById(R.id.createexamCatagory);
        createexamName = findViewById(R.id.createexamName);


        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");


        if ( id != null){
            id = bundle.getString("id");
            courseId = bundle.getString("examid");
            CoursCretorID = bundle.getString("courCretorid");
            examtype = bundle.getString("examType");
            examName = bundle.getString("examName");
            examNameCatagory = bundle.getString("examNameCatagory");
            examDuretion = bundle.getString("examDuretion");
            examMarks = bundle.getString("examMarks");
            examStartDate = bundle.getString("examStartDate");
            examTime = bundle.getString("examTime");
            examStopTime = bundle.getString("examStopTime");

            Toast.makeText(this, examtype+"\n", Toast.LENGTH_SHORT).show();

            DateFormat startTimesimple = new SimpleDateFormat("HH:mm:ss");
            DateFormat datesimple = new SimpleDateFormat("dd MM yy");

            long date = Long.parseLong(examStartDate);
            startmili = date;
            long Stime = Long.parseLong(examTime);
            startmili = Stime;

            long Etime = Long.parseLong(examStopTime);

            endmili = Etime;
            String datesample = datesimple.format(date);
            String startsample = startTimesimple.format(Stime);
            String endsample = startTimesimple.format(Etime);


            start_date.setText(datesample);
            start_time.setText(startsample);
            end_time.setText(endsample);

            examcreateButton.setText("Update");
            createexamMarks.setText(examMarks);
            createexamCatagory.setText(examNameCatagory);
            createexamName.setText(examName);

        }else {
            id = bundle.getString("id");
            courseId = bundle.getString("examid");
            coursecode = bundle.getString("coursecourse");
            courseName = bundle.getString("courseName");
            RandomSerchCodeS = bundle.getString("getRandomSerchCode");
            CoursCretorID = bundle.getString("courCretorid");
            CoursThecher = bundle.getString("CoursThecher");
            examtype = bundle.getString("examtype");
        }




        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Onlne_create_exam_type.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                setyear = year;
                                setmont = monthOfYear;
                                setday = dayOfMonth;
                                start_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                select_date = true;


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( select_date == true) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Onlne_create_exam_type.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    // startatime.setText(hourOfDay + ":" + minute);

                                    c.set(setyear, setmont, setday, hourOfDay, minute);

                                    long millis = c.getTimeInMillis();

                                    startmili = millis;

                                    long time = System.currentTimeMillis();

                                    // New date object from millis
                                    Date date = new Date(millis);

// formattter
                                    DateFormat df = new SimpleDateFormat("HH:mm:ss");

                                    String formatted = df.format(date);
                                    start_time.setText(formatted);

                                    select_start_time = true;

                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }else {
                    Toast.makeText(Onlne_create_exam_type.this, " Select date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_start_time == true) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Onlne_create_exam_type.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    // startatime.setText(hourOfDay + ":" + minute);
                                    c.set(setyear, setmont, setday, hourOfDay, minute);
                                    long millis = c.getTimeInMillis();
                                    endmili = millis;
                                    duration = endmili - startmili;
                                    // New date object from
                                    // millis
                                    Date date = new Date(millis);

                                    DateFormat df = new SimpleDateFormat("HH:mm:ss");

                                    String formatted = df.format(date);

                                    end_time.setText(formatted);

                                    select_end_time = true;
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }else {
                    Toast.makeText(Onlne_create_exam_type.this, "select start time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        examcreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference examName = FirebaseFirestore.getInstance()
                        .collection("Global Group").document(courseId).collection("Exam");


                String exam_name = createexamName.getText().toString();
                String exam_cataory = createexamCatagory.getText().toString();
                String exam_Marks = createexamMarks.getText().toString();
                String exam_duration;

                DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                String todayString = df1.format(startmili);
                long date  = Long.parseLong(todayString);

                if (id == null) {
                    if (exam_name.trim().isEmpty() || exam_cataory.trim().isEmpty() || exam_Marks.trim().isEmpty() || select_date == false || select_start_time == false || select_end_time == false) {
                        Toast.makeText(Onlne_create_exam_type.this, "Please insert All Data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                progressDialog.show();

                long diffMinutes = duration / (60 * 1000);
                if (diffMinutes >= 60) {
                    long exam_durationhours = duration / (60 * 60 * 1000);
                    exam_duration = Long.toString(exam_durationhours) + " :H";
                } else {
                    exam_duration = Long.toString(diffMinutes) + " :M";
                }

                if (id != null) {
                examName.document(id).update("id", id, "examid", courseId, "courCretorid", CoursCretorID
                        , "examType", examtype, "examName", exam_name, "examNameCatagory", exam_cataory, "examDuretion", exam_duration, "examMarks", exam_Marks
                        , "examStartDate", date, "examStartTime", startmili, "examStopTime", endmili)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();

                                if (examtype.equals("mcq")){
                                    Intent intent = new Intent(Onlne_create_exam_type.this, OnlineMCQExamCreator.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("examid",courseId);
                                    intent.putExtra("courCretorid",CoursCretorID);
                                    intent.putExtra("examType",examtype);
                                    intent.putExtra("examName",exam_name);
                                    intent.putExtra("examNameCatagory",exam_cataory);
                                    intent.putExtra("examDuretion",exam_duration);
                                    intent.putExtra("examMarks",exam_Marks);
                                    intent.putExtra("examStartDate",startmili +"");
                                    intent.putExtra("examTime",startmili +"");
                                    intent.putExtra("examStopTime",endmili +"");
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(Onlne_create_exam_type.this, TheoryExamCreat.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("examid",courseId);
                                    intent.putExtra("courCretorid",CoursCretorID);
                                    intent.putExtra("examType",examtype);
                                    intent.putExtra("examName",exam_name);
                                    intent.putExtra("examNameCatagory",exam_cataory);
                                    intent.putExtra("examDuretion",exam_duration);
                                    intent.putExtra("examMarks",exam_Marks);
                                    intent.putExtra("examStartDate",startmili +"");
                                    intent.putExtra("examTime",startmili +"");
                                    intent.putExtra("examStopTime",endmili +"");
                                    startActivity(intent);
                                }
                                Toast.makeText(Onlne_create_exam_type.this, "Exam is updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                        }else {
                    examName.add(new OnlineExamViewNote(null,courseId,CoursCretorID,examtype,exam_name,exam_cataory,exam_duration,
                            exam_Marks,date,startmili,endmili))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){

                                        progressDialog.dismiss();

                                        String id = task.getResult().getId();
                                        examName.document(id).update("id",id)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (examtype.equals("mcq")){
                                                            Intent intent = new Intent(Onlne_create_exam_type.this, OnlineMCQExamCreator.class);
                                                            intent.putExtra("id",id);
                                                            intent.putExtra("examid",courseId);
                                                            intent.putExtra("courCretorid",CoursCretorID);
                                                            intent.putExtra("examType",examtype);
                                                            intent.putExtra("examName",exam_name);
                                                            intent.putExtra("examNameCatagory",exam_cataory);
                                                            intent.putExtra("examDuretion",exam_duration);
                                                            intent.putExtra("examMarks",exam_Marks);
                                                            intent.putExtra("examStartDate",startmili +"");
                                                            intent.putExtra("examTime",startmili +"");
                                                            intent.putExtra("examStopTime",endmili +"");
                                                            startActivity(intent);
                                                        }else {
                                                            Intent intent = new Intent(Onlne_create_exam_type.this, TheoryExamCreat.class);
                                                            intent.putExtra("id",id);
                                                            intent.putExtra("examid",courseId);
                                                            intent.putExtra("courCretorid",CoursCretorID);
                                                            intent.putExtra("examType",examtype);
                                                            intent.putExtra("examName",exam_name);
                                                            intent.putExtra("examNameCatagory",exam_cataory);
                                                            intent.putExtra("examDuretion",exam_duration);
                                                            intent.putExtra("examMarks",exam_Marks);
                                                            intent.putExtra("examStartDate",startmili +"");
                                                            intent.putExtra("examTime",startmili +"");
                                                            intent.putExtra("examStopTime",endmili +"");
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });

                }

            }
        });

    }

}
