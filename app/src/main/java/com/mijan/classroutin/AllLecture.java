package com.mijan.classroutin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mijan.classroutin.adapter.NewsAdapter;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllLecture extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference postCollection;

    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher;

    FirebaseAuth mAuth;
    FirebaseUser current;
    public NewsAdapter newsAdapter;



    CardView alllecturePotOption;
    CircleImageView profile_image_alllecture;
    TextView createalllecture;
    RecyclerView recyclearallLectre;

    String userID ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lecture);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        userID = current.getUid();


        newsAdapter = new NewsAdapter();

        Toolbar toolbar = findViewById(R.id.toolbar_ID_cours);
        toolbar.setTitle("All lecture");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");


        alllecturePotOption = findViewById(R.id.alllecturePotOption);
        profile_image_alllecture = findViewById(R.id.profile_image_alllecture);
        createalllecture = findViewById(R.id.createalllecture);
        recyclearallLectre = findViewById(R.id.recyclearallLectre);



        if (CoursCretorID.equals(userID) ){
            alllecturePotOption.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(current.getPhotoUrl()).into(profile_image_alllecture);
        createalllecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newsfeedActivity = new Intent(AllLecture.this, NewsFeedAddedActivity.class);

                newsfeedActivity.putExtra("courseId",courseId);
                newsfeedActivity.putExtra("coursecourse",coursecode);
                newsfeedActivity.putExtra("courseName",courseName);
                newsfeedActivity.putExtra("posrtType","all Lecture");

                startActivity(newsfeedActivity);

            }
        });

        recyclearallLectre.setLayoutManager(new LinearLayoutManager(this));
        recyclearallLectre.setAdapter(newsAdapter);

        postCollection =  db.collection("Global Group").document(courseId).collection("all Lecture");

        Query query = postCollection.orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        newsAdapter.addPosts(queryDocumentSnapshots.getDocuments());
                        recyclearallLectre.setAdapter(newsAdapter);
                        newsAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
