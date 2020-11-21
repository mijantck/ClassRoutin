package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.adapter.ExamAdapter;
import com.mijan.classroutin.adapter.PerticifentAdapter;

public class Perticifents extends AppCompatActivity {



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher;

    FirebaseAuth mAuth;
    FirebaseUser current;


    private PerticifentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perticifents);


        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        toolbar.setTitle("Participant");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");


        setUpRecyclerViewE();
    }



    private void setUpRecyclerViewE() {

        Collection =  db.collection("Global Group").document(courseId).collection("Participant");

        FirestoreRecyclerOptions<ParticifentNote> options = new FirestoreRecyclerOptions.Builder<ParticifentNote>()
                .setQuery(Collection, ParticifentNote.class)
                .build();

        adapter = new PerticifentAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.particifentRecyclerview);
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
