package com.mijan.classroutin.onlineexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Assiningment;
import com.mijan.classroutin.Note.OnlineExamViewNote;
import com.mijan.classroutin.R;
import com.mijan.classroutin.TheoryAnsView;
import com.mijan.classroutin.adapter.OnlineExamViewAdapter;

public class OnlineExam extends AppCompatActivity {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();
    Button Create_M_C_Q_exam,Create_theory_exam;

    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher;


    OnlineExamViewAdapter  adapter;
    String date ;

    LinearLayout  examCreate;


    private int mYear, mMonth, mDay, mHour, mMinute;
    int setyear,setmont,setday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_exam);

        examCreate = findViewById(R.id.examCreate);

        Toolbar toolbar = findViewById(R.id.toolbar_ID_online_exam);
        toolbar.setTitle(" Online Exam ");
        setSupportActionBar(toolbar);



        Create_M_C_Q_exam = findViewById(R.id.Create_M_C_Q_exam);
        Create_theory_exam = findViewById(R.id.Create_theory_exam);


        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");

        if (CoursCretorID.equals(user_id)){

            examCreate.setVisibility(View.VISIBLE);
        }

        Create_M_C_Q_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OnlineExam.this,Onlne_create_exam_type.class);
                intent.putExtra("examid",courseId);
                intent.putExtra("coursecourse",coursecode);
                intent.putExtra("courseName",courseName);
                intent.putExtra("getRandomSerchCode",RandomSerchCodeS);
                intent.putExtra("courCretorid",CoursCretorID);
                intent.putExtra("CoursThecher",CoursThecher);
                intent.putExtra("examtype","mcq");

              //  Toast.makeText(OnlineExam.this, courseId+"", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
        Create_theory_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlineExam.this,Onlne_create_exam_type.class);
                intent.putExtra("examid",courseId);
                intent.putExtra("coursecourse",coursecode);
                intent.putExtra("courseName",courseName);
                intent.putExtra("getRandomSerchCode",RandomSerchCodeS);
                intent.putExtra("courCretorid",CoursCretorID);
                intent.putExtra("CoursThecher",CoursThecher);
                intent.putExtra("examtype","theory");
                //  Toast.makeText(OnlineExam.this, courseId+"", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        setUpRecyclerViewE();
    }

    private void setUpRecyclerViewE() {

        CollectionReference examName = FirebaseFirestore.getInstance()
                .collection("Global Group").document(courseId).collection("Exam");

        Query query = examName.orderBy("examStartDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<OnlineExamViewNote> options = new FirestoreRecyclerOptions.Builder<OnlineExamViewNote>()
                .setQuery(query, OnlineExamViewNote.class)
                .build();

        adapter = new OnlineExamViewAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.onlineExamListRecyclerVIew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnlineExamViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                OnlineExamViewNote onlineExamViewNote = documentSnapshot.toObject(OnlineExamViewNote.class);
                String id = onlineExamViewNote.getId();
                String examid = onlineExamViewNote.getExamid();
                String courCretorid =onlineExamViewNote.getCourCretorid();
                String examType = onlineExamViewNote.getExamType();
                String examName = onlineExamViewNote.getExamName();
                String examNameCatagory = onlineExamViewNote.getExamNameCatagory();
                String examDuretion = onlineExamViewNote.getExamDuretion();
                String examMarks = onlineExamViewNote.getExamMarks();
                long examStartDate = onlineExamViewNote.getExamStartDate();
                long examTime = onlineExamViewNote.getExamStartTime();
                long examStopTime = onlineExamViewNote.getExamStopTime();
                long currectTime = System.currentTimeMillis();

                if(user_id.equals(courCretorid)){

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OnlineExam.this);
                    String[] option = {" View ","Delete"};

                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){

                                if (examType.equals("mcq")){
                                    Intent onlineMcqQuaCrate = new Intent(OnlineExam.this, OnlineMCQExamCreator.class);
                                    onlineMcqQuaCrate.putExtra("id", id);
                                    onlineMcqQuaCrate.putExtra("examid", examid);
                                    onlineMcqQuaCrate.putExtra("courCretorid", courCretorid);
                                    onlineMcqQuaCrate.putExtra("examType", examType);
                                    onlineMcqQuaCrate.putExtra("examName", examName);
                                    onlineMcqQuaCrate.putExtra("examNameCatagory", examNameCatagory);
                                    onlineMcqQuaCrate.putExtra("examDuretion", examDuretion);
                                    onlineMcqQuaCrate.putExtra("examMarks", examMarks);
                                    onlineMcqQuaCrate.putExtra("examStartDate", examStartDate + "");
                                    onlineMcqQuaCrate.putExtra("examTime", examTime + "");
                                    onlineMcqQuaCrate.putExtra("examStopTime", examStopTime + "");
                                    startActivity(onlineMcqQuaCrate);
                                }else {
                                    Intent onlineMcqQuaCrate = new Intent(OnlineExam.this, TheoryExamCreat.class);
                                    onlineMcqQuaCrate.putExtra("id", id);
                                    onlineMcqQuaCrate.putExtra("examid", examid);
                                    onlineMcqQuaCrate.putExtra("courCretorid", courCretorid);
                                    onlineMcqQuaCrate.putExtra("examType", examType);
                                    onlineMcqQuaCrate.putExtra("examName", examName);
                                    onlineMcqQuaCrate.putExtra("examNameCatagory", examNameCatagory);
                                    onlineMcqQuaCrate.putExtra("examDuretion", examDuretion);
                                    onlineMcqQuaCrate.putExtra("examMarks", examMarks);
                                    onlineMcqQuaCrate.putExtra("examStartDate", examStartDate + "");
                                    onlineMcqQuaCrate.putExtra("examTime", examTime + "");
                                    onlineMcqQuaCrate.putExtra("examStopTime", examStopTime + "");
                                    startActivity(onlineMcqQuaCrate);
                                }
                            }else if (which == 1){

                                adapter.deleteItem(position);
                            }
                        }
                    }).create().show();



                }else {
                    if (currectTime >= examTime && currectTime <= examStopTime) {
                        if (examType.equals("mcq")) {
                            Intent onlineExamStart = new Intent(OnlineExam.this, ExamRanning.class);
                            onlineExamStart.putExtra("coursId", examid);
                            onlineExamStart.putExtra("examId", id);
                            onlineExamStart.putExtra("examType", examType);
                            onlineExamStart.putExtra("examStartTime", examTime + "");
                            onlineExamStart.putExtra("examEndTime", examStopTime + "");
                            startActivity(onlineExamStart);
                        }else {

                            Intent onlineExamStart = new Intent(OnlineExam.this, TheoryAnsView.class);
                            onlineExamStart.putExtra("coursId", examid);
                            onlineExamStart.putExtra("examId", id);
                            onlineExamStart.putExtra("examType", examType);
                            onlineExamStart.putExtra("examStartTime", examTime + "");
                            onlineExamStart.putExtra("examEndTime", examStopTime + "");
                            startActivity(onlineExamStart);

                        }
                    } else if (currectTime >= examStopTime ){
                        Toast.makeText(OnlineExam.this, "Exam time is end  ", Toast.LENGTH_SHORT).show();
                    } else if (currectTime <= examTime ){
                        Toast.makeText(OnlineExam.this, "Exam will start ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onStarExamClick(DocumentSnapshot documentSnapshot, int position) {


            }

        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
