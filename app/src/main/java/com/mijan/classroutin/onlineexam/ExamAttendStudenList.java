package com.mijan.classroutin.onlineexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.R;
import com.mijan.classroutin.StudentSubmitAnsList;
import com.mijan.classroutin.adapter.PerticifentAdapter;

public class ExamAttendStudenList extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    String courseId, examID,uID;

    FirebaseAuth mAuth;
    FirebaseUser current;
    private PerticifentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_attend_studen_list);

        Toolbar toolbar = findViewById(R.id.toolbar_Student_list);
        toolbar.setTitle(" Student List ");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("couresID");
        examID = bundle.getString("examID");

        setUpRecyclerViewE();

    }



    private void setUpRecyclerViewE() {

        Collection =  db.collection("Global Group").document(courseId).collection("Exam")
        .document(examID).collection("student list");

        FirestoreRecyclerOptions<ParticifentNote> options = new FirestoreRecyclerOptions.Builder<ParticifentNote>()
                .setQuery(Collection, ParticifentNote.class)
                .build();

        adapter = new PerticifentAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.attenstudeninexamRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PerticifentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                ParticifentNote particifentNote = documentSnapshot.toObject(ParticifentNote.class);

                String uID = particifentNote.getuID();

                Intent intent = new Intent(ExamAttendStudenList.this, StudentSubmitAnsList.class);
                intent.putExtra("couresID", courseId);
                intent.putExtra("examID", examID);
                intent.putExtra("uID", uID);
                startActivity(intent);
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
