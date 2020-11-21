package com.mijan.classroutin.onlineexam;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.R;

import java.util.Calendar;

public class ExamAdded extends AppCompatActivity {


    String uid = FirebaseAuth.getInstance().getUid();




    EditText ExamName, ExamTopic, ExamSubject, ExamHoll, ExamCode;
    TextView ExamDatePiker,ExamTimePiker;
    ImageView save_exam;

    Calendar calendar;
    int hour,min;
    TimePicker picker;



    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_added);

        ExamName = findViewById(R.id.EditeExamName);
        ExamTopic= findViewById(R.id.EditeexamTopic);
        ExamDatePiker= findViewById(R.id.ExamDatePiker);
        ExamTimePiker= findViewById(R.id.ExamTimePiker);
        ExamSubject= findViewById(R.id.EditeexamSubject);
        ExamHoll= findViewById(R.id.EditeExamHall);
        ExamCode =findViewById(R.id.EditecurseCode);
        save_exam =findViewById(R.id.save_exam);




        ExamDatePiker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ExamAdded.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                ExamDatePiker.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        ExamTimePiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTimePickerDialog();
            }
        });

        save_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ExamName1 = ExamName.getText().toString();
                String ExamTopic1 = ExamTopic.getText().toString();
                String ExamDatePiker1 = ExamDatePiker.getText().toString();
                String ExamTimePiker1 = ExamTimePiker.getText().toString();
                String ExamSubject1 = ExamSubject.getText().toString();
                String ExamHoll1 = ExamHoll.getText().toString();
                String ExamCode1 = ExamCode.getText().toString();


                      if(ExamName1.isEmpty() || ExamCode1.isEmpty()||ExamDatePiker1.isEmpty()){
                          Toast.makeText(ExamAdded.this, " Any one is empty", Toast.LENGTH_SHORT).show();
                          return;
                      }

                CollectionReference notebookRef = FirebaseFirestore.getInstance()
                        .collection("Users").document(uid).collection("Exam");
                notebookRef.add(new Exam(ExamName1, ExamTopic1, ExamDatePiker1,ExamTimePiker1,ExamSubject1,ExamHoll1,ExamCode1));


                Toast.makeText(ExamAdded.this, "Exam added", Toast.LENGTH_SHORT).show();
                finish();

            }
        });



    }

    private void openTimePickerDialog()
    {
        TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                //set selected time to textview
                ExamTimePiker.setText(updateTime(hour,min));
            }
        },hour,min,false);
        dialog.show();
    }
    // function to get am and pm from time
    private String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }
}
