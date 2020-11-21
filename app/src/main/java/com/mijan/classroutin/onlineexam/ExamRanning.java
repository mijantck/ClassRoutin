package com.mijan.classroutin.onlineexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.MCQQuastionNote;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.Note.TheoryExamNote;
import com.mijan.classroutin.R;
import com.mijan.classroutin.adapter.MCQCerrectAns_Examinniter_Adapter;
import com.mijan.classroutin.adapter.TheoryExamQuastonViewrAdapter;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ExamRanning extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();
    String user_URL = currentUser.getPhotoUrl().toString();
    String user_Email = currentUser.getEmail().toString();

    int curentMarks;
    String currectAnswer;


    CardView examMimer_info_input_card,examMimer_info_view_card;
    TextInputEditText examenar_name,examenar_rol_namber,examenar_section;
    Button examenar_info_submit;
    TextView examenar_name_view,examenar_rol_namber_view,examenar_section_view,endTimeViewSecound
            ,endTimeViewHours,endTimeViewMinite,examenar_info_edit,ans_submit_button;
    long examStartTimeLong,examEndTimeLong,TimerstartTime;


    CountDownTimer mCountDownTimer;
    String coursId,examId,examType,examStartTime,examEndTime;

    MCQCerrectAns_Examinniter_Adapter adapter;
    TheoryExamQuastonViewrAdapter Tadapter;

    HashMap<Integer,Integer> storeAnsMarks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_ranning);

        examMimer_info_input_card = findViewById(R.id.examMimer_info_input_card);
        examMimer_info_view_card = findViewById(R.id.examMimer_info_view_card);
        examenar_name = findViewById(R.id.examenar_name);
        examenar_rol_namber = findViewById(R.id.examenar_rol_namber);
        examenar_section = findViewById(R.id.examenar_section);
        examenar_info_submit = findViewById(R.id.examenar_info_submit);
        examenar_name_view = findViewById(R.id.examenar_name_view);
        examenar_rol_namber_view = findViewById(R.id.examenar_rol_namber_view);
        examenar_section_view = findViewById(R.id.examenar_section_view);
        endTimeViewSecound = findViewById(R.id.endTimeViewSecound);
        endTimeViewHours = findViewById(R.id.endTimeViewHours);
        endTimeViewMinite = findViewById(R.id.endTimeViewMinite);
        examenar_info_edit = findViewById(R.id.examenar_info_edit);
        ans_submit_button = findViewById(R.id.ans_submit_button);


        Bundle bundle = getIntent().getExtras();
        coursId = bundle.getString("coursId");
        examId = bundle.getString("examId");
        examType = bundle.getString("examType");
        examStartTime = bundle.getString("examStartTime");
        examEndTime = bundle.getString("examEndTime");
        examStartTimeLong = Long.parseLong(examStartTime);
        examEndTimeLong = Long.parseLong(examEndTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        formatter.setLenient(false);
        TimerstartTime = System.currentTimeMillis();
        mCountDownTimer = new CountDownTimer(examEndTimeLong, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                TimerstartTime=TimerstartTime-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - TimerstartTime) / 1000;

             /*   String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                txtViewDays.setText(daysLeft);*/

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                endTimeViewHours.setText(hoursLeft);

                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                endTimeViewMinite.setText(minutesLeft);

                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                endTimeViewSecound.setText(secondsLeft);
            }
            @Override
            public void onFinish() {
                mCountDownTimer.onFinish();
            }
        }.start();

        examenar_info_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                examenar_name.setText(examenar_name_view.getText().toString());
                examenar_rol_namber.setText(examenar_rol_namber_view.getText().toString());
                examenar_section.setText(examenar_section_view.getText().toString());

                examMimer_info_input_card.setVisibility(View.VISIBLE);
                examMimer_info_view_card.setVisibility(View.GONE);
            }
        });

        examenar_info_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examenar_nameget = examenar_name.getText().toString().trim();
                String examenar_rol_namberget = examenar_rol_namber.getText().toString().trim();
                String examenar_sectionget = examenar_section.getText().toString().trim();

                if (examenar_nameget.trim().isEmpty() || examenar_rol_namberget.trim().isEmpty() || examenar_sectionget.trim().isEmpty()  ) {
                    Toast.makeText(ExamRanning.this, "Please insert All Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Collection =  db.collection("Global Group").document(coursId).collection("Exam")
                        .document(examId).collection("student list");

                Collection.document(user_id).set(new ParticifentNote(coursId,user_id,examenar_nameget,examenar_rol_namberget
                        ,examenar_sectionget,user_Email,user_URL,0))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                examMimer_info_input_card.setVisibility(View.GONE);
                                examenar_name_view.setText(examenar_nameget);
                                examenar_rol_namber_view.setText(examenar_rol_namberget);
                                examenar_section_view.setText(examenar_sectionget);
                                examMimer_info_view_card.setVisibility(View.VISIBLE);
                            }
                        });



            }
        });


        ans_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sum = 0;
                for ( int num : storeAnsMarks.values()){
                    sum = sum+num;
                }
                DocumentReference docRef = db.collection("Global Group").document(coursId).collection("Exam")
                        .document(examId).collection("student list").document(user_id);
                docRef.update("marks",sum)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ExamRanning.this, " Thanks for submit your ans ", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        if (examType.equals("mcq")){
            setUpRecyclerViewE();
        }else {
            setUpRecyclerViewTheory();
        }
    }

    private void setUpRecyclerViewE() {

        CollectionReference examName = FirebaseFirestore.getInstance()
                .collection("Global Group").document(coursId).collection("Exam")
                .document(examId).collection("question");

        Query query = examName.orderBy("quaNo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MCQQuastionNote> options = new FirestoreRecyclerOptions.Builder<MCQQuastionNote>()
                .setQuery(query, MCQQuastionNote.class)
                .build();

        adapter = new MCQCerrectAns_Examinniter_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.exam_quastion_view_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MCQCerrectAns_Examinniter_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {



            }

            @Override
            public void onAnsAClick(DocumentSnapshot documentSnapshot, int position) {
                currectAnswer = "a";
                MCQQuastionNote mcqQuastionNote = documentSnapshot.toObject(MCQQuastionNote.class);
                String currecteAnser2 = mcqQuastionNote.getCurectans();
                String quastionID = mcqQuastionNote.getId();
                String courseID = mcqQuastionNote.getExamid();
                String courseCretorID = mcqQuastionNote.getExamCeatoreID();
                String ansA = mcqQuastionNote.getAnsAdiscription();
                String ansB = mcqQuastionNote.getAnsBdiscription();
                String ansC = mcqQuastionNote.getAnsCdiscription();
                String ansD = mcqQuastionNote.getAnsDdiscription();
                String quastion = mcqQuastionNote.getQuastionn();
                int  ansIndex = mcqQuastionNote.getQuaNo();
                if (currectAnswer.equals(currecteAnser2)){
                    storeAnsMarks.put(ansIndex,1);
                }else {
                    storeAnsMarks.put(ansIndex,0);
                }
                CollectionReference ansShit = FirebaseFirestore.getInstance()
                        .collection("Global Group").document(coursId).collection("Exam")
                        .document(examId).collection("student list").document(user_id).collection("ans shite");
                ansShit.document(quastionID).set(new MCQQuastionNote(ansIndex,quastionID,courseID,courseCretorID,quastion,ansA
                        ,ansB,ansC,ansD,currecteAnser2,currectAnswer))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }

            @Override
            public void onAnsBClick(DocumentSnapshot documentSnapshot, int position) {
                currectAnswer = "b";
                MCQQuastionNote mcqQuastionNote = documentSnapshot.toObject(MCQQuastionNote.class);
                String currecteAnser2 = mcqQuastionNote.getCurectans();
                String quastionID = mcqQuastionNote.getId();
                String courseID = mcqQuastionNote.getExamid();
                String courseCretorID = mcqQuastionNote.getExamCeatoreID();
                String ansA = mcqQuastionNote.getAnsAdiscription();
                String ansB = mcqQuastionNote.getAnsBdiscription();
                String ansC = mcqQuastionNote.getAnsCdiscription();
                String ansD = mcqQuastionNote.getAnsDdiscription();
                String quastion = mcqQuastionNote.getQuastionn();
                int  ansIndex = mcqQuastionNote.getQuaNo();
                if (currectAnswer.equals(currecteAnser2)){
                    storeAnsMarks.put(ansIndex,1);
                }else {
                    storeAnsMarks.put(ansIndex,0);
                }
                CollectionReference ansShit = FirebaseFirestore.getInstance()
                        .collection("Global Group").document(coursId).collection("Exam")
                        .document(examId).collection("student list").document(user_id).collection("ans shite");
                ansShit.document(quastionID).set(new MCQQuastionNote(ansIndex,quastionID,courseID,courseCretorID,quastion,ansA
                        ,ansB,ansC,ansD,currecteAnser2,currectAnswer))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            }
                        });

            }

            @Override
            public void onAnsCClick(DocumentSnapshot documentSnapshot, int position) {
                currectAnswer = "c";
                MCQQuastionNote mcqQuastionNote = documentSnapshot.toObject(MCQQuastionNote.class);
                String currecteAnser2 = mcqQuastionNote.getCurectans();
                String quastionID = mcqQuastionNote.getId();
                String courseID = mcqQuastionNote.getExamid();
                String courseCretorID = mcqQuastionNote.getExamCeatoreID();
                String ansA = mcqQuastionNote.getAnsAdiscription();
                String ansB = mcqQuastionNote.getAnsBdiscription();
                String ansC = mcqQuastionNote.getAnsCdiscription();
                String ansD = mcqQuastionNote.getAnsDdiscription();
                String quastion = mcqQuastionNote.getQuastionn();
                int  ansIndex = mcqQuastionNote.getQuaNo();
                if (currectAnswer.equals(currecteAnser2)){
                    storeAnsMarks.put(ansIndex,1);
                }else {
                    storeAnsMarks.put(ansIndex,0);
                }
                CollectionReference ansShit = FirebaseFirestore.getInstance()
                        .collection("Global Group").document(coursId).collection("Exam")
                        .document(examId).collection("student list").document(user_id).collection("ans shite");
                ansShit.document(quastionID).set(new MCQQuastionNote(ansIndex,quastionID,courseID,courseCretorID,quastion,ansA
                        ,ansB,ansC,ansD,currecteAnser2,currectAnswer))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            }
                        });

            }

            @Override
            public void onAnsDClick(DocumentSnapshot documentSnapshot, int position) {
                currectAnswer = "d";
                MCQQuastionNote mcqQuastionNote = documentSnapshot.toObject(MCQQuastionNote.class);
                String currecteAnser2 = mcqQuastionNote.getCurectans();
                String quastionID = mcqQuastionNote.getId();
                String courseID = mcqQuastionNote.getExamid();
                String courseCretorID = mcqQuastionNote.getExamCeatoreID();
                String ansA = mcqQuastionNote.getAnsAdiscription();
                String ansB = mcqQuastionNote.getAnsBdiscription();
                String ansC = mcqQuastionNote.getAnsCdiscription();
                String ansD = mcqQuastionNote.getAnsDdiscription();
                String quastion = mcqQuastionNote.getQuastionn();
                int  ansIndex = mcqQuastionNote.getQuaNo();
                if (currectAnswer.equals(currecteAnser2)){
                    storeAnsMarks.put(ansIndex,1);
                }else {
                    storeAnsMarks.put(ansIndex,0);
                }
                CollectionReference ansShit = FirebaseFirestore.getInstance()
                        .collection("Global Group").document(coursId).collection("Exam")
                        .document(examId).collection("student list").document(user_id).collection("ans shite");
                ansShit.document(quastionID).set(new MCQQuastionNote(ansIndex,quastionID,courseID,courseCretorID,quastion,ansA
                        ,ansB,ansC,ansD,currecteAnser2,currectAnswer))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            }
                        });

            }
        });
    }

    private void setUpRecyclerViewTheory() {

        Collection =  db.collection("Global Group").document(coursId).collection("Exam")
                .document(examId).collection("question");
        Query query = Collection.orderBy("TheoryExamSubjectPageNumer", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TheoryExamNote> options = new FirestoreRecyclerOptions.Builder<TheoryExamNote>()
                .setQuery(query, TheoryExamNote.class)
                .build();

        Tadapter = new TheoryExamQuastonViewrAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.theoryExamPeparViewRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Tadapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference docRef = db.collection("Global Group").document(coursId).collection("Exam")
                .document(examId).collection("student list").document(user_id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("uID") == null){
                    examMimer_info_input_card.setVisibility(View.VISIBLE);
                    examMimer_info_view_card.setVisibility(View.GONE);
                }else {
                    ParticifentNote particifentNote = documentSnapshot.toObject(ParticifentNote.class);
                    examenar_name_view.setText(particifentNote.getuName());
                    examenar_rol_namber_view.setText(particifentNote.getuRolNumber());
                    examenar_section_view.setText(particifentNote.getUsection());
                    curentMarks = particifentNote.getMarks();
                    examMimer_info_input_card.setVisibility(View.GONE);
                    examMimer_info_view_card.setVisibility(View.VISIBLE);
                }
            }
        });


        if (examType.equals("mcq")){
            adapter.startListening();
        }else {
            Tadapter.startListening();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (examType.equals("mcq")){
            adapter.stopListening();
        }else {
            Tadapter.stopListening();
        }

    }
}
