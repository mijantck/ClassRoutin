package com.mijan.classroutin.cours;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mijan.classroutin.AllLecture;
import com.mijan.classroutin.Assiningment;
import com.mijan.classroutin.Attendece;
import com.mijan.classroutin.NewsFeedAddedActivity;
import com.mijan.classroutin.Note.AttendenceNote;
import com.mijan.classroutin.Perticifents;
import com.mijan.classroutin.R;
import com.mijan.classroutin.activity.MainActivity;
import com.mijan.classroutin.adapter.NewsAdapter;
import com.mijan.classroutin.onlineexam.OnlineExam;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupSingleView extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference postCollection =  db.collection("Post");

    public NewsAdapter newsAdapter;

    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher;
    TextView RandomSerchCode,coureName,coureTecherName,crestpost,classCnceleNow;
    ImageView copytext;

    CircleImageView profile_image_post_incrouse;


    RecyclerView recyclerView;
    private InterstitialAd mInterstitialAd;


    private DrawerLayout drawer;
    FirebaseAuth mAuth;
    FirebaseUser current;
    JitsiMeetUserInfo userInfo;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAACFk86E0:APA91bGg2pivVBN3n8DjOHgqp5-bHLHwM8WwB4B1Y8pjsvTULqkSU_7th2Vz7FzhWCc0VAUtVcuMIsSKSqBuNREz6m0cK_il_Nea8KRMROoL91TNHDFrehSVWICXyI_SfRaH5X6PQxyD";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_single_view);

        Toolbar toolbar = findViewById(R.id.toolbar_ID_cours);
        RandomSerchCode  = findViewById(R.id.courseID);

        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        userInfo = new JitsiMeetUserInfo();

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


        recyclerView = findViewById(R.id.recyclearViewGroupSingle);

        coureName = findViewById(R.id.coureName);
        coureTecherName = findViewById(R.id.coureTecherName);
        copytext = findViewById(R.id.copytext);
        profile_image_post_incrouse = findViewById(R.id.profile_image_post_incrouse);
        crestpost = findViewById(R.id.crestpost);

        drawer = findViewById(R.id.drawer_layout_group_view);
        classCnceleNow = findViewById(R.id.classCnceleNow);
        NavigationView navigationView = findViewById(R.id.nav_view_cour_full);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");

        RandomSerchCode.setText("Code: "+RandomSerchCodeS);
        coureName.setText(courseName);
        coureTecherName.setText(CoursThecher);

        Picasso.get().load(current.getPhotoUrl()).into(profile_image_post_incrouse);


        long time = System.currentTimeMillis();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(time);

        if (current.getUid().equals(CoursCretorID)){

            CollectionReference calssCancel = FirebaseFirestore.getInstance()
                    .collection("Global Group").document(courseId).collection("attendence");
            classCnceleNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calssCancel.document(todayString).update("classOnSatrt","stop")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    classCnceleNow.setVisibility(View.GONE);
                                    notificationSend(courseName,"Class end ");

                                }
                            });
                }
            });
        }


        crestpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = courseId;
                String CoureseCode = coursecode;
                String CourseName = courseName;

                Intent newsfeedActivity = new Intent(GroupSingleView.this, NewsFeedAddedActivity.class);

                newsfeedActivity.putExtra("courseId",id);
                newsfeedActivity.putExtra("coursecourse",CoureseCode);
                newsfeedActivity.putExtra("courseName",CourseName);
                startActivity(newsfeedActivity);
            }
        });
        copytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(RandomSerchCodeS);
                    Toast.makeText(GroupSingleView.this, clipboard.getText()+"", Toast.LENGTH_SHORT).show();

                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", RandomSerchCodeS);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(GroupSingleView.this, clipboard.getText()+"", Toast.LENGTH_SHORT).show();


                }

            }


        });


       // setTitle(courseName);



        newsAdapter = new NewsAdapter();

        String id = courseId;
        String course = coursecode;
        String CourseName = courseName;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);
        Query query = postCollection.whereEqualTo("id",id)
        .orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        newsAdapter.addPosts(queryDocumentSnapshots.getDocuments());
                        recyclerView.setAdapter(newsAdapter);
                        newsAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
                Toast.makeText(GroupSingleView.this, "  "+e, Toast.LENGTH_SHORT).show();
            }
        });


        userInfo.setDisplayName(current.getDisplayName());
        userInfo.setEmail(current.getEmail());

        try {
            userInfo.setAvatar(new URL(current.getPhotoUrl().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.all_lecture:
                        Intent allLecturActivity = new Intent(GroupSingleView.this, AllLecture.class);
                        allLecturActivity.putExtra("courseId",courseId);
                        allLecturActivity.putExtra("coursecourse",coursecode);
                        allLecturActivity.putExtra("courseName",courseName);
                        allLecturActivity.putExtra("getRandomSerchCode",RandomSerchCodeS);
                        allLecturActivity.putExtra("CoursCretorID",CoursCretorID);
                        allLecturActivity.putExtra("CoursThecher",CoursThecher);
                        startActivity(allLecturActivity);

                        break;
                    case R.id.course_outline:

                        Toast.makeText(GroupSingleView.this, "It's coming soon.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.assignments:

                        Intent asingmentActivity = new Intent(GroupSingleView.this, Assiningment.class);
                        asingmentActivity.putExtra("courseId",courseId);
                        asingmentActivity.putExtra("coursecourse",coursecode);
                        asingmentActivity.putExtra("courseName",courseName);
                        asingmentActivity.putExtra("getRandomSerchCode",RandomSerchCodeS);
                        asingmentActivity.putExtra("CoursCretorID",CoursCretorID);
                        asingmentActivity.putExtra("CoursThecher",CoursThecher);
                        startActivity(asingmentActivity);

                        break;
                    case R.id.online_exam:

                        Intent onlineActivity = new Intent(GroupSingleView.this, OnlineExam.class);
                        onlineActivity.putExtra("courseId",courseId);
                        onlineActivity.putExtra("coursecourse",coursecode);
                        onlineActivity.putExtra("courseName",courseName);
                        onlineActivity.putExtra("getRandomSerchCode",RandomSerchCodeS);
                        onlineActivity.putExtra("CoursCretorID",CoursCretorID);
                        onlineActivity.putExtra("CoursThecher",CoursThecher);
                        startActivity(onlineActivity);

                        break;
                    case R.id.join_class:

                        long time = System.currentTimeMillis();
                        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                        String todayString = df1.format(time);
                        int date = Integer.parseInt(todayString);
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date dateobj = new Date();
                        String dateS = df.format(dateobj);


                        CollectionReference attendence = FirebaseFirestore.getInstance()
                                .collection("Global Group").document(courseId).collection("attendence");

                        String tearcherID = current.getUid();
                        String name = current.getDisplayName();
                        String email = current.getEmail();
                        String studentURL = current.getPhotoUrl().toString();


                        if (tearcherID.equals(CoursCretorID)){
                            attendence.document(todayString).set(new AttendenceNote(todayString,courseId,name,date,"start",dateS,"teacher"))
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {

                                          Toast.makeText(GroupSingleView.this, "Class is start now ", Toast.LENGTH_SHORT).show();

                                          if(!RandomSerchCodeS.isEmpty()){
                                              JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                                      .setRoom(RandomSerchCodeS)
                                                      .setUserInfo(userInfo)
                                                      .build();
                                              JitsiMeetActivity.launch(GroupSingleView.this,options);
                                          }

                                          notificationSend(courseName,"Class is start now ");


                                      }
                                  });

                        }else {


                            final Dialog dialog = new Dialog(GroupSingleView.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.rol_nmber_enter_layout);
                            TextView text = (TextView) dialog.findViewById(R.id.enterrolnumber);
                            Button dialogButton = (Button) dialog.findViewById(R.id.rolnmberSubmiteButton);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String rol = text.getText().toString().trim();
                                    if (rol.isEmpty()){
                                        text.findFocus();
                                        return;
                                    }
                                    int rolINT = Integer.parseInt(rol);
                                    String customeID = tearcherID+todayString;
                                       Query query1 = attendence.whereEqualTo("date", date).whereEqualTo("classOnSatrt","start");
                            query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    attendence.document(customeID).set(new AttendenceNote(null,courseId,tearcherID,name,date,"student","P",studentURL,dateS,rolINT,email))
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {

                                                   dialog.dismiss();
                                                   if(!RandomSerchCodeS.isEmpty()){
                                                       JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                                               .setRoom(RandomSerchCodeS)
                                                               .setUserInfo(userInfo)
                                                               .build();
                                                       JitsiMeetActivity.launch(GroupSingleView.this,options);
                                                   }

                                                   Toast.makeText(GroupSingleView.this, " Attendance complete ", Toast.LENGTH_SHORT).show();

                                               }
                                           });
                                }
                            });

                                }
                            });

                            dialog.show();

                        }

                        break;

                    case R.id.attendance:

                        Intent attendeceActivity = new Intent(GroupSingleView.this, Attendece.class);
                        attendeceActivity.putExtra("courseId",courseId);
                        attendeceActivity.putExtra("coursecourse",coursecode);
                        attendeceActivity.putExtra("courseName",courseName);
                        attendeceActivity.putExtra("getRandomSerchCode",RandomSerchCodeS);
                        attendeceActivity.putExtra("CoursCretorID",CoursCretorID);
                        attendeceActivity.putExtra("CoursThecher",CoursThecher);
                        startActivity(attendeceActivity);
                        break;

                    case R.id.participants:


                        Intent PActivity = new Intent(GroupSingleView.this, Perticifents.class);

                        PActivity.putExtra("courseId",courseId);
                        PActivity.putExtra("coursecourse",coursecode);
                        PActivity.putExtra("courseName",courseName);
                        PActivity.putExtra("getRandomSerchCode",RandomSerchCodeS);
                        PActivity.putExtra("CoursCretorID",CoursCretorID);
                        PActivity.putExtra("CoursThecher",CoursThecher);
                        startActivity(PActivity);
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }

        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        long time = System.currentTimeMillis();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(time);

        CollectionReference calssCancel = FirebaseFirestore.getInstance()
                .collection("Global Group").document(courseId).collection("attendence");
        calssCancel.document(todayString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.get("classOnSatrt") != null) {
                            if (documentSnapshot.get("classOnSatrt").toString().equals("start")){
                                if (current.getUid().equals(CoursCretorID)){

                                    classCnceleNow.setVisibility(View.VISIBLE);
                                }

                            }


                        }
                    }
                }

            }
        });
        newsAdapter.notifyDataSetChanged();
    }

    public  void  notificationSend( String courseName,String classType){

        TOPIC = "/topics/"+courseId; //topic must match with what the receiver subscribed to

        NOTIFICATION_TITLE = courseName;
        NOTIFICATION_MESSAGE = classType;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            // Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //    Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GroupSingleView.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);

    }

    private void showInterstitial() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(GroupSingleView.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }
}
