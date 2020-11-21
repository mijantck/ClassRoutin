package com.mijan.classroutin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mijan.classroutin.adapter.NewsAdapter;
import com.mijan.classroutin.cours.MyCourse;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notebookRef = FirebaseFirestore.getInstance()
            .collection("Post");


     public NewsAdapter newsAdapter;

    RecyclerView recyclerView;


    ImageView noNews;
    ProgressBar progressBar;

    private List<DocumentSnapshot> newsfeed ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("My News ");

        recyclerView = findViewById(R.id.recycler_view_News_feed);
        progressBar= findViewById(R.id.progresbar);
        noNews = findViewById(R.id.NoNews);


        newsfeed = new ArrayList<>();


        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();


        setnewsfeed();

    }




    private void setnewsfeed() {

       newsAdapter = new NewsAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(newsAdapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference usergroup = db.collection("Users")
                .document(uid).collection("My Group");
        usergroup.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> myGroups = queryDocumentSnapshots.getDocuments();
                        CollectionReference postCollection =  db.collection("Post");
                        for (DocumentSnapshot documentSnapshot : myGroups) {

                            postCollection.whereEqualTo("id", documentSnapshot.get("courseID"))
                                    .orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING)

                            //  .orderBy("date", Query.Direction.ASCENDING)
                                      .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        newsfeed = new ArrayList<>();

                                            newsAdapter.addPosts(queryDocumentSnapshots.getDocuments());
                                            recyclerView.setAdapter(newsAdapter);
                                            newsAdapter.notifyDataSetChanged();
                                            recyclerView.getRecycledViewPool().clear();
                                            recyclerView.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                            noNews.setVisibility(View.GONE);

                                        }
                                    });
                        }

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addedNewPost:

                Intent intent_news = new Intent(NewsFeedActivity.this, MyCourse.class);
                startActivity(intent_news);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        newsAdapter.notifyDataSetChanged();
    }


}
