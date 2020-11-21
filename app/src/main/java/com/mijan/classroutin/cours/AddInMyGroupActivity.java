package com.mijan.classroutin.cours;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mijan.classroutin.R;
import com.mijan.classroutin.adapter.GroupSearchAdapter;
import com.mijan.classroutin.onlineexam.Onlne_create_exam_type;

import java.util.ArrayList;
import java.util.List;

public class AddInMyGroupActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference collectiongroup = db.collection("Global Group");


    private EditText searchEdit;
    private Button searchButton, addbutton, canclebutton;

    private RecyclerView recyclerView;

    List<DocumentSnapshot> groupsnapshot;

    GroupSearchAdapter adapter;

    public Button addGroup, canclegroup;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_in_my_group);

        Toolbar toolbar = findViewById(R.id.toolbar_ID_search);
        setSupportActionBar(toolbar);
        setTitle(" Search  ");
        progressDialog = new ProgressDialog(AddInMyGroupActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        searchEdit = findViewById(R.id.sherchEditeText);
        searchButton = findViewById(R.id.shearchButon);
        addbutton = findViewById(R.id.groupAddButton);
        canclebutton = findViewById(R.id.groupCancleButton);
        recyclerView = findViewById(R.id.groupList);

        addGroup = findViewById(R.id.groupAddButton);
        canclegroup = findViewById(R.id.groupCancleButton);


       // setGroupList();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  search(searchEdit.getText().toString());

                String courseCode = searchEdit.getText().toString();
                if (courseCode.isEmpty()){
                    Toast.makeText(AddInMyGroupActivity.this, "Enter code", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();

                setGroupList(courseCode);


            }
        });

    }


    private void setGroupList(String randomcode) {

        groupsnapshot = new ArrayList<>();

        adapter = new GroupSearchAdapter(groupsnapshot);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);


        FirebaseFirestore.getInstance().collection("Global Group").whereEqualTo("randomSerchCode", randomcode)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                progressDialog.dismiss();
                                adapter.setGroupsnapshot(queryDocumentSnapshots.getDocuments());
                                adapter.notifyDataSetChanged();
                            }else {
                                progressDialog.dismiss();
                            }
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_coure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.new_coure:

                Intent intent = new Intent(AddInMyGroupActivity.this, NewCourse.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void search(String query) {

        final String searchEdit1 = searchEdit.getText().toString();

        setGroupList(searchEdit1);
      /*  final Query query1 = collectiongroup.whereGreaterThanOrEqualTo("randomSerchCode", searchEdit1);


        query1.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Course_Note course_note = documentSnapshot.toObject(Course_Note.class);

                            groupsnapshot.clear();

                            adapter.setGroupsnapshot(queryDocumentSnapshots.getDocuments());
                            adapter.notifyDataSetChanged();



                        }


                    }
                });
*/

    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }*/

}

