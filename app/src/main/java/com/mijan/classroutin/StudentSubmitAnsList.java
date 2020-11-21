package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.MCQQuastionNote;
import com.mijan.classroutin.adapter.MCQCerrectAnsAdapter;
import com.mijan.classroutin.onlineexam.OnlineMCQExamCreator;

public class StudentSubmitAnsList extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    FirebaseAuth mAuth;
    FirebaseUser current;


    String courseId, examID,uID;

    private MCQCerrectAnsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_submit_ans_list);

        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("couresID");
        examID = bundle.getString("examID");
        uID = bundle.getString("uID");

        setUpRecyclerViewE();
    }


    private void setUpRecyclerViewE() {

        Collection =  db.collection("Global Group").document(courseId).collection("Exam")
                .document(examID).collection("student list").document(uID).collection("ans shite");

        Query query = Collection.orderBy("quaNo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MCQQuastionNote> options = new FirestoreRecyclerOptions.Builder<MCQQuastionNote>()
                .setQuery(query, MCQQuastionNote.class)
                .build();

        adapter = new MCQCerrectAnsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.studentAnsSubmiteListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
