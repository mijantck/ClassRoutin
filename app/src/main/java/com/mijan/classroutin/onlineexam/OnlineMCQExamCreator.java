package com.mijan.classroutin.onlineexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.mijan.classroutin.R;
import com.mijan.classroutin.adapter.MCQCerrectAnsAdapter;

public class OnlineMCQExamCreator extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    FirebaseAuth mAuth;
    FirebaseUser current;


    TextInputEditText currectAnser,setMCQQNO,setMCQQustion,setanserA,setanserB,setanserC,setanserD;
    Button sunmitButton;
    String id,examid,courCretorid,examType,examName,examNameCatagory,examDuretion,examMarks,examStartDate,examTime,examStopTime;

    long examsartDateLong,examStartTimeLong,examEndTimeLong;


    TextView Update_exam_info,Added_M_C_Q_question,Exam_attend_studint_list;

    LinearLayout M_C_Q_quastionEditor;

    private MCQCerrectAnsAdapter adapter;

    String idupdate;
    boolean update;
    boolean visibilti = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_m_c_q_exam_creator);


        Toolbar toolbar = findViewById(R.id.toolbar_ID_mcq);
        toolbar.setTitle(" M.C.Q Exam ");
        setSupportActionBar(toolbar);


        currectAnser = findViewById(R.id.currectAnser);
        sunmitButton = findViewById(R.id.sunmitButton);
        setMCQQNO = findViewById(R.id.setMCQQNO);
        setMCQQustion = findViewById(R.id.setMCQQustion);
        setanserA = findViewById(R.id.setanserA);
        setanserB = findViewById(R.id.setanserB);
        setanserC = findViewById(R.id.setanserC);
        setanserD = findViewById(R.id.setanserD);
        Update_exam_info = findViewById(R.id.Update_exam_info);
        Added_M_C_Q_question = findViewById(R.id.Added_M_C_Q_question);
        M_C_Q_quastionEditor = findViewById(R.id.M_C_Q_quastionEditor);
        Exam_attend_studint_list = findViewById(R.id.Exam_attend_studint_list);


        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        examid = bundle.getString("examid");
        courCretorid = bundle.getString("courCretorid");
        examType = bundle.getString("examType");
        examName = bundle.getString("examName");
        examNameCatagory = bundle.getString("examNameCatagory");
        examDuretion = bundle.getString("examDuretion");
        examMarks = bundle.getString("examMarks");
        examStartDate = bundle.getString("examStartDate");
        examTime = bundle.getString("examTime");
        examStopTime = bundle.getString("examStopTime");

        examsartDateLong = Long.parseLong(examStartDate);
        examStartTimeLong = Long.parseLong(examTime);
        examEndTimeLong = Long.parseLong(examStopTime);

        Exam_attend_studint_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlineMCQExamCreator.this, ExamAttendStudenList.class);
                intent.putExtra("couresID", examid);
                intent.putExtra("examID", id);
                startActivity(intent);
            }
        });
        Update_exam_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlineMCQExamCreator.this,Onlne_create_exam_type.class);
                intent.putExtra("id", id);
                intent.putExtra("examid", examid);
                intent.putExtra("courCretorid", courCretorid);
                intent.putExtra("examType", examType);
                intent.putExtra("examName", examName);
                intent.putExtra("examNameCatagory", examNameCatagory);
                intent.putExtra("examDuretion", examDuretion);
                intent.putExtra("examMarks", examMarks);
                intent.putExtra("examStartDate", examStartDate);
                intent.putExtra("examTime", examTime);
                intent.putExtra("examStopTime", examStopTime);
                startActivity(intent);

            }
        });
        Added_M_C_Q_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibilti == false){
                    M_C_Q_quastionEditor.setVisibility(View.VISIBLE);

                    visibilti = true;

                }else if (visibilti == true){

                    M_C_Q_quastionEditor.setVisibility(View.GONE);

                    visibilti = false;
                }

            }
        });
        sunmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currectans = currectAnser.getText().toString();
                String MCQQNO = setMCQQNO.getText().toString();
                String MCQQustion = setMCQQustion.getText().toString();
                String anserA = setanserA.getText().toString();
                String anserB = setanserB.getText().toString();
                String anserC = setanserC.getText().toString();
                String anserD = setanserD.getText().toString();
                if (currectans.trim().isEmpty() || MCQQNO.trim().isEmpty() || MCQQustion.trim().isEmpty() || anserA.trim().isEmpty() || anserB.trim().isEmpty() || anserC.trim().isEmpty() || anserD.trim().isEmpty()) {
                    Toast.makeText(OnlineMCQExamCreator.this, "Please insert All Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currectans.length() != 1 ){
                    currectAnser.setError("please enter only one character a,b,c,d");
                    return;
                }
                Integer QN = Integer.parseInt(MCQQNO);

                Collection =  db.collection("Global Group").document(examid).collection("Exam")
                .document(id).collection("question");

                if (update == true){
                    Collection.document(idupdate).set(new  MCQQuastionNote(QN,id,examid,courCretorid,MCQQustion,"A",
                            anserA,"B",anserB,"C",anserC,"D",anserD,
                            currectans.toLowerCase(),0,0) )
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    setMCQQustion.setText("");
                                    setMCQQNO.setText("");
                                    currectAnser.setText("");
                                    setanserA.setText("");
                                    setanserB.setText("");
                                    setanserC.setText("");
                                    setanserD.setText("");
                                }
                            });
                }else {
                    Collection.add(new MCQQuastionNote(QN, null, examid, courCretorid, MCQQustion, "A",
                            anserA, "B", anserB, "C", anserC, "D", anserD,
                            currectans.toLowerCase(), 0, 0))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        String id = task.getResult().getId();
                                        Collection.document(id).update("id", id)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        setMCQQustion.setText("");
                                                        setMCQQNO.setText("");
                                                        currectAnser.setText("");
                                                        setanserA.setText("");
                                                        setanserB.setText("");
                                                        setanserC.setText("");
                                                        setanserD.setText("");

                                                        Toast.makeText(OnlineMCQExamCreator.this, "MCQ add", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });

        setUpRecyclerViewE();

    }


    private void setUpRecyclerViewE() {

        Collection =  db.collection("Global Group").document(examid).collection("Exam")
                .document(id).collection("question");

        Query query = Collection.orderBy("quaNo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MCQQuastionNote> options = new FirestoreRecyclerOptions.Builder<MCQQuastionNote>()
                .setQuery(query, MCQQuastionNote.class)
                .build();

        adapter = new MCQCerrectAnsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.mcqAnsersRecyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MCQCerrectAnsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {


                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(OnlineMCQExamCreator.this);
                String[] option = {"Update", "Delete"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            new android.app.AlertDialog.Builder(OnlineMCQExamCreator.this)
                                    .setMessage("Are you want to Update?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MCQQuastionNote mcqQuastionNote = documentSnapshot.toObject(MCQQuastionNote.class);

                                            setMCQQustion.setText(mcqQuastionNote.getQuastionn());
                                            setMCQQNO.setText(mcqQuastionNote.getQuaNo()+"");
                                            currectAnser.setText(mcqQuastionNote.getCurectans());
                                            setanserA.setText(mcqQuastionNote.getAnsDdiscription());
                                            setanserB.setText(mcqQuastionNote.getAnsBdiscription());
                                            setanserC.setText(mcqQuastionNote.getAnsCdiscription());
                                            setanserD.setText(mcqQuastionNote.getAnsDdiscription());

                                            idupdate = mcqQuastionNote.getId();
                                            update = true;

                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                        if (which == 1) {

                            new AlertDialog.Builder(OnlineMCQExamCreator.this).setTitle("Confirm Delete?")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    MCQQuastionNote mcqQuastionNote = documentSnapshot.toObject(MCQQuastionNote.class);

                                                    Collection.document(mcqQuastionNote.getId()).delete();
                                                }
                                            })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();


                        }
                    }
                }).create().show();

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
