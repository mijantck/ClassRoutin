package com.mijan.classroutin.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.mijan.classroutin.MyAlarm;
import com.mijan.classroutin.Note.Course_Note;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.Note.NoteRoutin;
import com.mijan.classroutin.Note.OnlineExamViewNote;
import com.mijan.classroutin.OnlineLibarary;
import com.mijan.classroutin.R;
import com.mijan.classroutin.TaskActivitiy;
import com.mijan.classroutin.cours.MyCourse;
import com.mijan.classroutin.onlineexam.ExmActivity;
import com.mijan.classroutin.routin_add_activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class fragment_home extends Fragment  {

    String uid = FirebaseAuth.getInstance().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Users")
            .document(uid).collection("Routin");


    private View routinView;
    private RecyclerView routinList;
    private LinearLayout add_rotin, exam_home, course_home, task_home,laybery,spp,history,event;
    private ImageView addIamgrVIew,todayimage;
    ArrayList<String> GroupsCode = new ArrayList<>();
    String today;
    String quarryDay;
    long datelong;
    TextView Fhome_eamType, Fhome_exam_tital, Fstart_time_home, Fhome_exam_catagory,
            Fhome_exam_start_time_cownd, allExamShow,weaterView;

    private AdView mAdView;
    LinearLayout examviewLayoutView;



    public fragment_home() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        routinView = inflater.inflate(R.layout.home_one, container, false);
        addIamgrVIew = routinView.findViewById(R.id.addImage);
        add_rotin = routinView.findViewById(R.id.add_rotin);
        exam_home = routinView.findViewById(R.id.exam_home);
        course_home = routinView.findViewById(R.id.course_home);
        task_home = routinView.findViewById(R.id.task_home);
        laybery = routinView.findViewById(R.id.laybery);
        spp = routinView.findViewById(R.id.spp);
        history = routinView.findViewById(R.id.history);
        event = routinView.findViewById(R.id.event);
        todayimage = routinView.findViewById(R.id.todayimage);


        Fhome_eamType = routinView.findViewById(R.id.Fhome_eamType);
        Fhome_exam_tital = routinView.findViewById(R.id.Fhome_exam_tital);
        Fstart_time_home = routinView.findViewById(R.id.Fstart_time_home);
        Fhome_exam_catagory = routinView.findViewById(R.id.Fhome_exam_catagory);
        Fhome_exam_start_time_cownd = routinView.findViewById(R.id.Fhome_exam_start_time_cownd);
        allExamShow = routinView.findViewById(R.id.allExamShow);
        weaterView = routinView.findViewById(R.id.weaterView);
        examviewLayoutView = routinView.findViewById(R.id.examviewLayoutView);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        routinList = routinView.findViewById(R.id.home_page_routine_show);
        routinList.setLayoutManager(layoutManager);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        long time = System.currentTimeMillis();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(time);
        datelong = Long.parseLong(todayString);


        today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        if (today.equals("Sunday")) {
            quarryDay = "SunDay";
        } else if (today.equals("Monday")) {
            quarryDay = "MonDay";
        } else if (today.equals("Tuesday")) {
            quarryDay = "TuesDay";
        } else if (today.equals("Wednesday")) {
            quarryDay = "WednesDay";
        } else if (today.equals("Thursday")) {
            quarryDay = "ThursDay";
        } else if (today.equals("Friday")) {
            quarryDay = "FriDay";
        } else if (today.equals("Saturday")) {
            quarryDay = "SaturDay";
        }

        allExamShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyCourse.class);
                startActivity(intent);

            }
        });

        mAdView = routinView.findViewById(R.id.adViewHomeF);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        add_rotin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(fragment_home.this.getActivity(),routin_add_activity.class);
              startActivity(intent);

            }
        });

        exam_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment_home.this.getActivity(), ExmActivity.class);
                startActivity(intent);

            }
        });
        course_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment_home.this.getActivity(), MyCourse.class);
                startActivity(intent);

            }
        });
        task_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment_home.this.getActivity(),TaskActivitiy.class);
                startActivity(intent);

            }
        });
        laybery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment_home.this.getActivity(), OnlineLibarary.class);
                startActivity(intent);

            }
        });
        spp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "it's coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "it's coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "it's coming soon", Toast.LENGTH_SHORT).show();

            }
        });

        return routinView;

    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = notebookRef.whereArrayContains("day", quarryDay)
                .orderBy("start_time", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions options =
                new FirestoreRecyclerOptions.Builder<NoteRoutin>()
                        .setQuery(query, NoteRoutin.class)
                        .build();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirestoreRecyclerAdapter<NoteRoutin, RoutinViewHolder> adapter = new FirestoreRecyclerAdapter<NoteRoutin, RoutinViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RoutinViewHolder holder, final int position, @NonNull final NoteRoutin model) {
                holder.subject_name.setText(model.getSubject());
                holder.rom_number.setText(model.getRoome());
                holder.subject_techear_name.setText(model.getTecher());
                holder.class_start_time.setText(getTimee(model.getStart_time()));
                holder.class_end_time.setText(model.getEnd_time());

                Toast.makeText(getContext(), model.getTecher()+"", Toast.LENGTH_SHORT).show();


            }

            @NonNull
            @Override
            public RoutinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.routin_new_layout_single, viewGroup, false);
                RoutinViewHolder viewHolder = new RoutinViewHolder(view);
                return viewHolder;
            }
        };
        routinList.setAdapter(adapter);
        adapter.startListening();

        CollectionReference usergroup = db.collection("Users")
                .document(uid).collection("My Group");
        usergroup.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> myGroups = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot documentSnapshot : myGroups) {
                            String getcorseName = documentSnapshot.get("courseName") + "";
                            String getId = documentSnapshot.get("courseID") + "";
                            GroupsCode.add(getId);

                            CollectionReference getTodayExam = db.collection("Global Group")
                                    .document(getId).collection("Exam");
                            Query query1 = getTodayExam.whereEqualTo("examStartDate", datelong);

                            query1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> TodayExam = queryDocumentSnapshots.getDocuments();

                                    if (!TodayExam.isEmpty()){
                                        examviewLayoutView.setVisibility(View.VISIBLE);
                                    }
                                    for (DocumentSnapshot documentSnapshot : TodayExam) {
                                        String ExamName = documentSnapshot.get("examName") + "";
                                        String ExamType = documentSnapshot.get("examType") + "";
                                        String ExamDate = documentSnapshot.get("examStartDate") + "";
                                        String ExamCatagory = documentSnapshot.get("examNameCatagory") + "";
                                        String ExamTime = documentSnapshot.get("examStartTime") + "";
                                        long startTime = Long.parseLong(ExamTime);

                                        DateFormat startTimesimple = new SimpleDateFormat("HH:mm:ss");
                                        String startsample = startTimesimple.format(startTime);

                                        Fhome_exam_tital.setText(ExamName);
                                        Fhome_eamType.setText(ExamType);
                                        Fhome_exam_catagory.setText(ExamCatagory);
                                        Fhome_exam_start_time_cownd.setText(startsample);
                                    }

                                }
                            });

                        }

                    }
                });

    }

    public static class RoutinViewHolder extends RecyclerView.ViewHolder {
        TextView subject_name, subject_techear_name, class_start_time, class_end_time, rom_number, today_date, day_view;
        public RoutinViewHolder(@NonNull View itemView) {
            super(itemView);
            subject_name = itemView.findViewById(R.id.subject_name);
            subject_techear_name = itemView.findViewById(R.id.subject_techear_name);
            class_start_time = itemView.findViewById(R.id.class_start_time);
            class_end_time = itemView.findViewById(R.id.class_end_time);
            rom_number = itemView.findViewById(R.id.rom_number);
        }
    }

    private String getTimee(long timestamp) {
        long ts = timestamp * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time = sdf.format(new Date(ts));
        return time;
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h,m,s);
    }


}
