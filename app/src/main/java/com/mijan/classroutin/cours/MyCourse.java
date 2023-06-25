package com.mijan.classroutin.cours;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.Course_Note;
import com.mijan.classroutin.R;
import com.mijan.classroutin.adapter.CourseAdapter;

import java.util.List;

public class MyCourse extends AppCompatActivity {

    TextView coursCode, coursName, courseTeacher;
    RecyclerView recyclerView;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    String user_id = currentUser.getUid();

    CollectionReference postref = rootRef.collection("Users").document(user_id).collection("My Group");


    private CourseAdapter adapter;

        List<DocumentSnapshot> groupsnapshot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        coursCode     = findViewById(R.id.course_Name_view_id);
        coursName     = findViewById(R.id.course_view_id);
        courseTeacher = findViewById(R.id.Teacher_Name_ID);



        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("My Course ");

        ImageView imageView = findViewById(R.id.sad_imag);

        imageView.setVisibility(View.VISIBLE);
        setUpRecyclerView();


        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.intsial_ads), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("dfd", "onAdLoaded");
                        showInterstitial();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("safas", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        mAdView = findViewById(R.id.adViewCours);
        mAdView.loadAd(adRequest);


    }


    private void setUpRecyclerView() {

        Query query = postref.orderBy("courseCode", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Course_Note> options = new FirestoreRecyclerOptions.Builder<Course_Note>()
                .setQuery(query, Course_Note.class)
                .build();
        adapter = new CourseAdapter(options);
        RecyclerView recyclerView   = findViewById(R.id.recycler_view_MyCours);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(adapter);
        ImageView imageView = findViewById(R.id.sad_imag);
        imageView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });




    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_coures_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search_course:

                Intent addCoursIntent = new Intent(MyCourse.this, AddInMyGroupActivity.class);
                startActivity(addCoursIntent);

                return true;
            case R.id.new_coure:

                Intent intent = new Intent(MyCourse.this, NewCourse.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void showInterstitial() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(MyCourse.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }
}
