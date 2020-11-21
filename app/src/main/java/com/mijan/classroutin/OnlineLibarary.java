package com.mijan.classroutin;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mijan.classroutin.adapter.PdfUploadAdapter;

public class OnlineLibarary extends AppCompatActivity {

    RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PdfUploadAdapter pdfUploadAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_libarary);

        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("Online Library");

        recyclerView = findViewById(R.id.libararyRecyclerView);
        pdfUploadAdapter = new PdfUploadAdapter();

        floatingActionButton = findViewById(R.id.floatingActionButton);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(pdfUploadAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flatintent =new Intent(OnlineLibarary.this,LibraryPdfUpoad.class);
                startActivity(flatintent);
            }
        });

        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Library PDF");

        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                       pdfUploadAdapter.addPdfList(queryDocumentSnapshots.getDocuments());
                    }
                });






    }
}
