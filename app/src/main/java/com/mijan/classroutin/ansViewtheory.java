package com.mijan.classroutin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.adapter.AnsPerticifentAdapter;
import com.mijan.classroutin.adapter.AnsforAllAdapter;

public class ansViewtheory extends AppCompatActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;

    FirebaseAuth mAuth;
    FirebaseUser current;

    AnsforAllAdapter adapter;
    String courseId,upID,name,rol,uID,fromTheory;
    TextView NameviewForeAns,rolviewForeAns;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ans_viewtheory);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_ID_stu);
        toolbar.setTitle(" Answer list ");
        setSupportActionBar(toolbar);



        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        upID = bundle.getString("upID");
        name = bundle.getString("name");
        rol = bundle.getString("rol");
        uID = bundle.getString("uID");
        fromTheory = bundle.getString("fromTheory");



        rolviewForeAns = findViewById(R.id.rolviewForeAns);
        NameviewForeAns = findViewById(R.id.NameviewForeAns);
        NameviewForeAns.setText("Name: "+name);
        rolviewForeAns.setText("Rol number: "+rol);

        if (fromTheory != null){
            UpRecyclerViewE();
        }else {
            setUpRecyclerViewE();
        }


    }
    private void setUpRecyclerViewE() {

        Collection =  db.collection("Global Group").document(courseId).collection("Assingnment")
                .document(upID).collection("ans");

        Query query = Collection.whereEqualTo("uid",uID).orderBy("page", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<AsingmentSubmitNote> options = new FirestoreRecyclerOptions.Builder<AsingmentSubmitNote>()
                .setQuery(query, AsingmentSubmitNote.class)
                .build();

        adapter = new AnsforAllAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.ansViewforallRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnsforAllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                AsingmentSubmitNote asingmentSubmitNote = documentSnapshot.toObject(AsingmentSubmitNote.class);
                Intent intent = new Intent(ansViewtheory.this,ImageViewActivity.class);
                intent.putExtra("URL",asingmentSubmitNote.getImageURL());
                startActivity(intent);
            }
        });


    }
    private void UpRecyclerViewE() {

        Collection =  db.collection("Global Group").document(courseId).collection("Exam")
                .document(upID).collection("ans");
        Query query = Collection.whereEqualTo("uid",uID).orderBy("page", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<AsingmentSubmitNote> options = new FirestoreRecyclerOptions.Builder<AsingmentSubmitNote>()
                .setQuery(query, AsingmentSubmitNote.class)
                .build();

        adapter = new AnsforAllAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.ansViewforallRecyclerview);
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
