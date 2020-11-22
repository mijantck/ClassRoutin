package com.mijan.classroutin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.Note.AttendenceNote;
import com.mijan.classroutin.adapter.AssingmentViewrAdapter;
import com.mijan.classroutin.adapter.AttendeceDateAdapter;
import com.mijan.classroutin.cours.AddInMyGroupActivity;
import com.mijan.classroutin.cours.GroupSingleView;
import com.squareup.picasso.Picasso;

public class Assiningment extends AppCompatActivity {

    TextView Make_Assignments_texview_button;
    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher;

    AssingmentViewrAdapter assingmentViewrAdapter;
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asiningment);

        Toolbar toolbar = findViewById(R.id.toolbar_ID_assimget);
        toolbar.setTitle(" Assignment ");
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        progressDialog = new ProgressDialog(Assiningment.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");


        Make_Assignments_texview_button = findViewById(R.id.Make_Assignments_texview_button);



        if (!currentUser.getUid().equals(CoursCretorID)){

            Make_Assignments_texview_button.setVisibility(View.GONE);

        }
        Make_Assignments_texview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = courseId;
                String CoureseCode = coursecode;
                String CourseName = courseName;
                Intent AsingmetCreatSubmitActivity = new Intent(Assiningment.this, AsingmetCreatSubmit.class);
                AsingmetCreatSubmitActivity.putExtra("courseId",courseId);
                AsingmetCreatSubmitActivity.putExtra("coursecourse",coursecode);
                AsingmetCreatSubmitActivity.putExtra("courseName",courseName);
                AsingmetCreatSubmitActivity.putExtra("getRandomSerchCode",RandomSerchCodeS);
                AsingmetCreatSubmitActivity.putExtra("CoursCretorID",CoursCretorID);
                AsingmetCreatSubmitActivity.putExtra("CoursThecher",CoursThecher);
                startActivity(AsingmetCreatSubmitActivity);
            }
        });

        setUpRecyclerViewE();

    }

    private void setUpRecyclerViewE() {

        CollectionReference attendence = FirebaseFirestore.getInstance()
       .collection("Global Group").document(courseId).collection("Assingnment");


        Query query = attendence.orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AsingmentSubmitNote> options = new FirestoreRecyclerOptions.Builder<AsingmentSubmitNote>()
                .setQuery(query, AsingmentSubmitNote.class)
                .build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.asingmentViewRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        assingmentViewrAdapter = new AssingmentViewrAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(assingmentViewrAdapter);

      assingmentViewrAdapter.setOnItemClickListener(new AssingmentViewrAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

              String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

              AsingmentSubmitNote asingmentSubmitNote = documentSnapshot.toObject(AsingmentSubmitNote.class);
              String assingmetCretID = asingmentSubmitNote.getUid();
              String coureId = courseId;
              String id = asingmentSubmitNote.getId();
              String name = asingmentSubmitNote.getName();
              String page = asingmentSubmitNote.getPage()+"";
              String date = asingmentSubmitNote.getDate();
              String endTime = asingmentSubmitNote.getEndTime()+"";



              if (uID.equals(assingmetCretID)){
                  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Assiningment.this);
                  String[] option = {"Update", "Submitted ans view ","Delete"};

                  builder.setItems(option, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          if ( which == 0){
                              Intent AsingmetCreatSubmitActivity = new Intent(Assiningment.this, AsingmetCreatSubmit.class);
                              AsingmetCreatSubmitActivity.putExtra("courseId",courseId);
                              AsingmetCreatSubmitActivity.putExtra("update","update");
                              AsingmetCreatSubmitActivity.putExtra("upName",name);
                              AsingmetCreatSubmitActivity.putExtra("upDate",date);
                              AsingmetCreatSubmitActivity.putExtra("upEndTime",endTime);
                              AsingmetCreatSubmitActivity.putExtra("upPage",page);
                              AsingmetCreatSubmitActivity.putExtra("upID",id);
                              startActivity(AsingmetCreatSubmitActivity);

                          }else if(which == 1){{

                              Intent intent = new Intent(Assiningment.this,AnsviewTheoryAndAsingment.class);
                              intent.putExtra("courseId",courseId);
                              intent.putExtra("upID",id);
                              startActivity(intent);


                          }

                          }else if (which == 2){
                              new AlertDialog.Builder(Assiningment.this)
                                      .setIcon(R.drawable.ic_delete)
                                      .setMessage("Confirm Cancel?")
                                      .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              assingmentViewrAdapter.deleteItem(position);
                                          }
                                      })
                                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {

                                          }
                                      }).create().show();
                          }
                      }
                  }).create().show();

              }else {
                  Intent AsingmetCreatSubmitActivity = new Intent(Assiningment.this, AsingmetCreatSubmit.class);
                  AsingmetCreatSubmitActivity.putExtra("courseId",courseId);
                  AsingmetCreatSubmitActivity.putExtra("upID",id);
                  AsingmetCreatSubmitActivity.putExtra("submiteAns","submiteAns");
                  startActivity(AsingmetCreatSubmitActivity);
              }


          }

          @Override
          public void onImageItemClick(DocumentSnapshot documentSnapshot, int position) {
              AsingmentSubmitNote asingmentSubmitNote = documentSnapshot.toObject(AsingmentSubmitNote.class);
              Intent intent = new Intent(Assiningment.this, ImageViewActivity.class);
              intent.putExtra("URL",asingmentSubmitNote.getImageURL());
              startActivity(intent);
          }
      });
    }

    @Override
    protected void onStart() {
        super.onStart();
        assingmentViewrAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        assingmentViewrAdapter.stopListening();
    }
}
