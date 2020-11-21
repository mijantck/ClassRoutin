package com.mijan.classroutin.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mijan.classroutin.MyAlarm;
import com.mijan.classroutin.Note.NoteRoutin;
import com.mijan.classroutin.R;
import com.mijan.classroutin.routin_add_activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class fragment_fri extends Fragment {

    String uid = FirebaseAuth.getInstance().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Users")
            .document(uid).collection("Routin");

    ProgressDialog progressDialog;

    private View routinView;
    private RecyclerView routinList;
    private ImageView addIamgrVIew;
    private  int request =0;

    AlarmManager alarmManager;

    public fragment_fri() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        routinView = inflater.inflate(R.layout.fri_activity, container, false);

        routinList = routinView.findViewById(R.id.recycler_view_sun);
        routinList.setLayoutManager(new LinearLayoutManager(getContext()));

        addIamgrVIew = routinView.findViewById(R.id.addImage);

        addIamgrVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), routin_add_activity.class);
                startActivity(intent);
            }
        });


        return routinView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = notebookRef.whereArrayContains("day", "FriDay")
                .orderBy("start_time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions options =
                new FirestoreRecyclerOptions.Builder<NoteRoutin>()
                        .setQuery(query, NoteRoutin.class)
                        .build();

        FirestoreRecyclerAdapter<NoteRoutin, RoutinViewHolder> adapter
                = new FirestoreRecyclerAdapter<NoteRoutin, RoutinViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RoutinViewHolder holder, final int position, @NonNull final NoteRoutin model) {


                convertInt( model.getStart_time());

                if(model.getSerial() == request){

                    holder.alarmAdd.setVisibility(View.GONE);
                    holder.alarmOff.setVisibility(View.VISIBLE);

                }
                else {

                    holder.alarmAdd.setVisibility(View.VISIBLE);
                    holder.alarmOff.setVisibility(View.GONE);
                }
              //  holder.serial.setText(model.getSerial());
                holder.subject.setText(model.getSubject());


                if (model.getSubject() == null){
                    addIamgrVIew.setVisibility(View.VISIBLE);
                    routinList.setVisibility(View.GONE);
                }else {
                    addIamgrVIew.setVisibility(View.GONE);
                    routinList.setVisibility(View.VISIBLE);
                }

                holder.room_number.setText(model.getRoome());
                holder.teacher.setText(model.getTecher());
                holder.startTime.setText(getTimee(model.getStart_time()));
                holder.endTime.setText(model.getEnd_time());
               // holder.border.setBackgroundColor(getRandomColorCode());
                //holder.cardView.setCardBackgroundColor(getRandomColorCode());

//                holder.day_view.setText("Friday");
                holder.alarmAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        startAlarm( model.getStart_time());

                        String id = getSnapshots().getSnapshot(position).getId();

                        DocumentReference unotebookRef =FirebaseFirestore.getInstance()
                                .collection("Users").document(uid)
                                .collection("Routin").document(id);
                        unotebookRef
                                .update("serial",request)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        holder.alarmAdd.setVisibility(View.GONE);
                                        holder.alarmOff.setVisibility(View.VISIBLE);

                                        Toast.makeText(getContext(), "Alarm is set", Toast.LENGTH_SHORT).show();

                                        progressDialog = new ProgressDialog(getContext());
                                        // Setting Message
                                        progressDialog.setTitle("Updating"); // Setting Title
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Update is incomplete ", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });

                holder.alarmOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        cancel( model.getStart_time());

                        String id = getSnapshots().getSnapshot(position).getId();

                        DocumentReference unotebookRef =FirebaseFirestore.getInstance()
                                .collection("Users").document(uid)
                                .collection("Routin").document(id);
                        unotebookRef
                                .update("serial",1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {



                                        holder.alarmAdd.setVisibility(View.VISIBLE);
                                        holder.alarmOff.setVisibility(View.GONE);

                                        Toast.makeText(getContext(), "Alarm is cancel", Toast.LENGTH_SHORT).show();

                                        progressDialog = new ProgressDialog(getContext());
                                        // Setting Message
                                        progressDialog.setTitle("Updating"); // Setting Title
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Update is incomplete ", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        String[] option = {"Update", "Delete"};
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db = FirebaseFirestore.getInstance();

                                if (which == 0) {

                                    String id = getSnapshots().getSnapshot(position).getId();
                                    int serial = model.getSerial();
                                    String subject = model.getSubject();
                                    String room_number = model.getRoome();
                                    String teacher = model.getTecher();
                                    long startTime = model.getStart_time();
                                    String endTime = model.getEnd_time();
                                    ArrayList<String> day = (ArrayList<String>) model.getDay();

                                    Intent RoutinIntent = new Intent(getContext(), routin_add_activity.class);

                                    RoutinIntent.putExtra("id", id);
                                    RoutinIntent.putExtra("upserial", serial);
                                    RoutinIntent.putExtra("upsubject", subject);
                                    RoutinIntent.putExtra("uproom_number", room_number);
                                    RoutinIntent.putExtra("upteacher", teacher);
                                    RoutinIntent.putExtra("up_startTime", startTime);
                                    RoutinIntent.putExtra("upendTime", endTime);

                                    RoutinIntent.putExtra("day",day);

                                    startActivity(RoutinIntent);


                                }
                                if (which == 1) {

                                    new AlertDialog.Builder(getContext()).setTitle("Confirm Delete?")
                                            .setMessage("Are you sure?")
                                            .setPositiveButton("YES",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            cancel( model.getStart_time());
                                                            getSnapshots().getSnapshot(position).getReference().delete();

                                                            dialog.dismiss();
                                                        }
                                                    })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create()
                                            .show();


                                }
                            }
                        }).create().show();

                    }
                });
            }

            @NonNull
            @Override
            public RoutinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.all_routin_new_layout_single, viewGroup, false);

                RoutinViewHolder viewHolder = new RoutinViewHolder(view);

                return viewHolder;
            }
        };

        routinList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class RoutinViewHolder extends RecyclerView.ViewHolder {

        TextView serial, subject, room_number, teacher, startTime, endTime, day_view;
        CardView cardView ;
        View border;
        TextView alarmAdd, alarmOff;

        public RoutinViewHolder(@NonNull View itemView) {
            super(itemView);

          //  border = itemView.findViewById(R.id.border);
          //  cardView = itemView.findViewById(R.id.RoutinCardeView);
          //  serial = itemView.findViewById(R.id.serial_Numbar_ID);
            subject = itemView.findViewById(R.id.all_subject_name);
            room_number = itemView.findViewById(R.id.all_rom_number);
            teacher = itemView.findViewById(R.id.all_subject_techear_name);
            startTime = itemView.findViewById(R.id.all_class_start_time);
            endTime = itemView.findViewById(R.id.all_class_end_time);
           // day_view = itemView.findViewById(R.id.Day_TextView);
            alarmAdd = itemView.findViewById(R.id.all_alarm_on);
            alarmOff = itemView.findViewById(R.id.all_alarm_off);

        }
    }

    private void startAlarm(long c) {

        String input1 = c+"";     //input string
        String lastFourDigits = "";     //substring containing last 4 characters

        if (input1.length() > 4)
        {
            lastFourDigits = input1.substring(input1.length() - 4);
        }
        else
        {
            lastFourDigits = input1;
        }
        int fourDigiteNuber = Integer.valueOf(lastFourDigits)+1 ;

        request = fourDigiteNuber;

        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),request,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c,7*24*60*60*1000,pendingIntent);

        Toast.makeText(getContext(), "Alarm is set ", Toast.LENGTH_SHORT).show();

    }

    private void cancel(long c) {


        String input1 = c+"";     //input string
        String lastFourDigits = "";     //substring containing last 4 characters

        if (input1.length() > 4)
        {
            lastFourDigits = input1.substring(input1.length() - 4);
        }
        else
        {
            lastFourDigits = input1;
        }
        int fourDigiteNuber = Integer.valueOf(lastFourDigits)+1 ;

        request = fourDigiteNuber;

        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),request,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Toast.makeText(getContext(), "Alarm is cancel ", Toast.LENGTH_SHORT).show();

        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
        request =0;


        Toast.makeText(getContext(), "Alarm is cancel ", Toast.LENGTH_SHORT).show();

    }
    public int convertInt(long c){

        String input1 = c+"";     //input string
        String lastFourDigits = "";     //substring containing last 4 characters

        if (input1.length() > 4)
        {
            lastFourDigits = input1.substring(input1.length() - 4);
        }
        else
        {
            lastFourDigits = input1;
        }
        int fourDigiteNuber = Integer.valueOf(lastFourDigits)+1 ;

        request = fourDigiteNuber;
        return request;
    }

    public int getRandomColorCode(){

        Random random = new Random();

        return Color.argb(255, random.nextInt(256), random.nextInt(256),     random.nextInt(256));

    }

    private String getTimee(long timestamp) {
        long ts = timestamp * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time = sdf.format(new Date(ts));
        return time;
    }


}
