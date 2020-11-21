package com.mijan.classroutin;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.cours.AddInMyGroupActivity;
import com.mijan.classroutin.cours.MyCourse;
import com.mijan.classroutin.cours.NewCourse;
import com.mijan.classroutin.onlineexam.ExmActivity;
import com.squareup.picasso.Picasso;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    FirebaseAuth mAuth;
    FirebaseUser current;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    FirebaseAuth.AuthStateListener mAuthLisenar;
    GoogleSignInClient mGoogleSignInClient;
    String uid = FirebaseAuth.getInstance().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();




    private static final int PICK_IMAGE_REQUEST = 1;

    String[] perms = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_NETWORK_STATE
            ,Manifest.permission.ACCESS_WIFI_STATE
            ,Manifest.permission.INTERNET
            ,Manifest.permission.VIBRATE
            ,Manifest.permission.CAMERA};




    BottomNavigationItemView add_navigation, tesk_navigation, exm_navigation;

    private DrawerLayout drawer;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthLisenar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);

        viewpagerAdapter viewpagerAdapter = new viewpagerAdapter(getSupportFragmentManager());

        mAuth = FirebaseAuth.getInstance();

        current = mAuth.getCurrentUser();


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabMode);

        add_navigation = findViewById(R.id.add_navigation);
        tesk_navigation = findViewById(R.id.task_navigation);
        exm_navigation = findViewById(R.id.exm_navigation);

        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        updateVavHeader();


        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.intsial_ads));

        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        mInterstitialAd.setAdListener(new AdListener() {

            public void onAdLoaded() {

             //   showInterstitial();

            }

        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.my_course:

                        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {


                            Intent my_courseIntent = new Intent(MainActivity.this, MyCourse.class);
                            startActivity(my_courseIntent);

                        } else {
                            EasyPermissions.requestPermissions(MainActivity.this, "We need permissions because this and that",
                                    PICK_IMAGE_REQUEST , perms);


                        }

                        break;
                    case R.id.search_course:
                        Intent search_courseIntent = new Intent(MainActivity.this, AddInMyGroupActivity.class);
                        startActivity(search_courseIntent);
                        break;
                    case R.id.new_course:
                        Intent new_courseIntent = new Intent(MainActivity.this, NewCourse.class);
                        startActivity(new_courseIntent);
                        break;
                    case R.id.nav_send:
                        Intent new_aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(new_aboutIntent);
                        break;
                        case R.id.mcpMeeting:
                        Intent mcpMeetingIntent = new Intent(MainActivity.this, MCPMeeting.class);
                        startActivity(mcpMeetingIntent);
                        break;
                    case R.id.loguot:

                        mAuth.signOut();

                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // [START_EXCLUDE]
                                        revokeAccess();
                                        updateUI(null);
                                        // [END_EXCLUDE]
                                    }
                                });

                        Toast.makeText(getApplication(), "Sign Out Complete ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, signIn_activity.class));
                        finish();
                        break;

                    case R.id.libary:


                        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {


                            Intent libarary_courseIntent = new Intent(MainActivity.this, OnlineLibarary.class);
                            startActivity(libarary_courseIntent);

                        } else {
                            EasyPermissions.requestPermissions(MainActivity.this, "We need permissions because this and that",
                                    PICK_IMAGE_REQUEST , perms);



                        }



                        break;
                }

                updateVavHeader();

                drawer.closeDrawer(GravityCompat.START);

                return true;

            }

        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mAuthLisenar = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, signIn_activity.class));
                    finish();
                }
            }
        };

        onclick();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_acivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notif:

                if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {


                    Intent intent_news = new Intent(MainActivity.this, NewsFeedActivity.class);
                    startActivity(intent_news);

                } else {
                    EasyPermissions.requestPermissions(MainActivity.this, "We need permissions because this and that",
                            PICK_IMAGE_REQUEST , perms);



                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onclick() {

        add_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, routin_add_activity.class);
                startActivity(intent);
            }
        });
        tesk_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskActivitiy.class);
                startActivity(intent);
            }
        });
        exm_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExmActivity.class);
                startActivity(intent);
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

    public void updateVavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headeView = navigationView.getHeaderView(0);
        TextView nav_name = headeView.findViewById(R.id.nav_name);
        TextView nav_email = headeView.findViewById(R.id.nav_email);
        ImageView nav_iamge = headeView.findViewById(R.id.nav_image);
        nav_name.setText(current.getDisplayName());
        nav_email.setText(current.getEmail());

        Picasso.get()
                .load(current.getPhotoUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(nav_iamge);


    }

    private void showInterstitial() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {

        } else {

        }
    }

}