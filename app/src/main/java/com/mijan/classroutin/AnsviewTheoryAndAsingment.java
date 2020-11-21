package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.adapter.AnsPerticifentAdapter;
import com.mijan.classroutin.adapter.PerticifentAdapter;

public class AnsviewTheoryAndAsingment extends AppCompatActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    String courseId, upID,fromTheory;

    FirebaseAuth mAuth;
    FirebaseUser current;

    AnsPerticifentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ansview_theory_and_asingment);

        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        upID = bundle.getString("upID");
        fromTheory = bundle.getString("fromTheory");

        Toolbar toolbar = findViewById(R.id.toolbar_IDstudentList);
        toolbar.setTitle(" Student List ");
        setSupportActionBar(toolbar);


        if (fromTheory != null){
            RecyclerViewE();


        }else {
            setUpRecyclerViewE();
        }

    }

    private void setUpRecyclerViewE() {
        Collection =  db.collection("Global Group").document(courseId).collection("Assingnment")
        .document(upID).collection("ans");

        FirestoreRecyclerOptions<AsingmentSubmitNote> options = new FirestoreRecyclerOptions.Builder<AsingmentSubmitNote>()
                .setQuery(Collection, AsingmentSubmitNote.class)
                .build();

        adapter = new AnsPerticifentAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.ansVIewReciclerciew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnsPerticifentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                AsingmentSubmitNote asingmentSubmitNote = documentSnapshot.toObject(AsingmentSubmitNote.class);
                String name = asingmentSubmitNote.getName();
                String rol = asingmentSubmitNote.getRol()+"";
                String uID = asingmentSubmitNote.getUid()+"";

                Intent intent = new Intent(AnsviewTheoryAndAsingment.this,ansViewtheory.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("upID",upID);
                intent.putExtra("name",name);
                intent.putExtra("rol",rol);
                intent.putExtra("uID",uID);
                startActivity(intent);




            }
        });


    }

    private void RecyclerViewE() {
        Collection =  db.collection("Global Group").document(courseId).collection("Exam")
                .document(upID).collection("ans");

        FirestoreRecyclerOptions<AsingmentSubmitNote> options = new FirestoreRecyclerOptions.Builder<AsingmentSubmitNote>()
                .setQuery(Collection, AsingmentSubmitNote.class)
                .build();

        adapter = new AnsPerticifentAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.ansVIewReciclerciew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnsPerticifentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                AsingmentSubmitNote asingmentSubmitNote = documentSnapshot.toObject(AsingmentSubmitNote.class);
                String name = asingmentSubmitNote.getName();
                String rol = asingmentSubmitNote.getRol()+"";
                String uID = asingmentSubmitNote.getUid()+"";

                Intent intent = new Intent(AnsviewTheoryAndAsingment.this,ansViewtheory.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("upID",upID);
                intent.putExtra("name",name);
                intent.putExtra("rol",rol);
                intent.putExtra("uID",uID);
                intent.putExtra("fromTheory","jkknkk");
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
