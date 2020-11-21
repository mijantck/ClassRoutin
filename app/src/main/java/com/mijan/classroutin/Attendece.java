package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.Note.AttendenceNote;
import com.mijan.classroutin.adapter.AttendeceDateAdapter;
import com.mijan.classroutin.adapter.RolNumberAttendeceDateAdapter;
import com.mijan.classroutin.cours.GroupSingleView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Attendece extends AppCompatActivity {

    AttendeceDateAdapter attendeceDateAdapter;
    RecyclerView dateRecycleraview;
    RolNumberAttendeceDateAdapter rolNumberAttendeceDateAdapter;



    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendece);

        Toolbar toolbar = findViewById(R.id.toolbar_ID_online_exam_attendens);
        toolbar.setTitle(" Attendance  ");
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");

        setUpRecyclerViewE();

        long time = System.currentTimeMillis();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(time);
        int date = Integer.parseInt(todayString);

        AttentUpRecyclerViewE(date);

    }

    private void setUpRecyclerViewE() {
        CollectionReference attendence = FirebaseFirestore.getInstance()
                .collection("Global Group").document(courseId).collection("attendence");
        Query query = attendence.whereEqualTo("student","teacher").orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AttendenceNote> options = new FirestoreRecyclerOptions.Builder<AttendenceNote>()
                .setQuery(query, AttendenceNote.class)
                .build();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dateRecycleraview);
        recyclerView.setLayoutManager(linearLayoutManager);

        attendeceDateAdapter = new AttendeceDateAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(attendeceDateAdapter);
        attendeceDateAdapter.setOnItemClickListener(new AttendeceDateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                AttendenceNote attendenceNote = documentSnapshot.toObject(AttendenceNote.class);

                AttentUpRecyclerViewE(attendenceNote.getDate());
            }
        });

    }

    private void AttentUpRecyclerViewE(int date) {
        CollectionReference attendence = FirebaseFirestore.getInstance()
                .collection("Global Group").document(courseId).collection("attendence");
        Query query = attendence.whereEqualTo("student","student").whereEqualTo("date", date)
                .orderBy("rol", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<AttendenceNote> options = new FirestoreRecyclerOptions.Builder<AttendenceNote>()
                .setQuery(query, AttendenceNote.class)
                .build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.attentdenViewRecylerView);
        recyclerView.setLayoutManager(linearLayoutManager);


        rolNumberAttendeceDateAdapter = new RolNumberAttendeceDateAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(rolNumberAttendeceDateAdapter);
        rolNumberAttendeceDateAdapter.startListening();


        rolNumberAttendeceDateAdapter.setOnItemClickListener(new RolNumberAttendeceDateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                AttendenceNote attendenceNote = documentSnapshot.toObject(AttendenceNote.class);

                Toast.makeText(Attendece.this, attendenceNote.getName()+"", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(Attendece.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.attendence_info_view_dialog);
                TextView name_attendence_view_info = (TextView) dialog.findViewById(R.id.name_attendence_view_info);
                TextView rol_attendence_view_info = (TextView) dialog.findViewById(R.id.rol_attendence_view_info);
                TextView email_attendence_view_info = (TextView) dialog.findViewById(R.id.email_attendence_view_info);
                Button ok_attendece_button = (Button) dialog.findViewById(R.id.ok_attendece_button);
                name_attendence_view_info.setText(" NAME : "+attendenceNote.getName() );
                rol_attendence_view_info.setText(" ROL : "+attendenceNote.getRol() );
                email_attendence_view_info.setText(" EMAIL : "+attendenceNote.getEmail() );
                ok_attendece_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        attendeceDateAdapter.startListening();
        rolNumberAttendeceDateAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attendeceDateAdapter.stopListening();
        rolNumberAttendeceDateAdapter.stopListening();
    }
}
