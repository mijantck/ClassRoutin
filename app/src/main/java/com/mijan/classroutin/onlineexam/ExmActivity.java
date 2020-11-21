package com.mijan.classroutin.onlineexam;

import android.content.DialogInterface;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.R;
import com.mijan.classroutin.adapter.ExamAdapter;

public class ExmActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;

    String uid = FirebaseAuth.getInstance().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Users")
            .document(uid).collection("Exam");

    private ExamAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exm);
        floatingActionButton = findViewById(R.id.examAddedButon);
      //  recyclerView = findViewById(R.id.recycler_view);


        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("Exam  ");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExmActivity.this, ExamAdded.class);
                startActivity(intent);
            }
        });

        setUpRecyclerViewE();


    }

    private void setUpRecyclerViewE() {
        Query query = notebookRef.orderBy("examDate", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Exam> options = new FirestoreRecyclerOptions.Builder<Exam>()
                .setQuery(query, Exam.class)
                .build();

        adapter = new ExamAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_viewExam);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ExamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, final int position) {
                new android.app.AlertDialog.Builder(ExmActivity.this)
                        .setMessage("Are You Want to Delete ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(position);

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

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
