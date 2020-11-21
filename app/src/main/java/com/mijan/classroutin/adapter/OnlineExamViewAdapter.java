package com.mijan.classroutin.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.Note.OnlineExamViewNote;
import com.mijan.classroutin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnlineExamViewAdapter extends FirestoreRecyclerAdapter<OnlineExamViewNote, OnlineExamViewAdapter.NoteHolder> {

    private OnItemClickListener listener;
    CountDownTimer mCountDownTimer;
    long examStartTimeLong,examEndTimeLong,TimerstartTime;


    public OnlineExamViewAdapter(@NonNull FirestoreRecyclerOptions<OnlineExamViewNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull OnlineExamViewNote model) {
        holder.examViewTitle.setText(model.getExamName());
        holder.examViewCatagory.setText(model.getExamNameCatagory());
        holder.examViewDuration.setText(model.getExamDuretion());
        holder.examViewMark.setText(model.getExamMarks());

        long date = model.getExamStartDate();
        long startTime = model.getExamStartTime();
        long stopTime = model.getExamStopTime();

        examEndTimeLong = startTime;
        examStartTimeLong = stopTime;


        DateFormat startTimesimple = new SimpleDateFormat("HH:mm:ss");
        DateFormat datesimple = new SimpleDateFormat("yyyy-MM-dd");

        String datesample = datesimple.format(startTime);
        String startsample = startTimesimple.format(startTime);
        String endsample = startTimesimple.format(stopTime);

        holder.examViewDate.setText(datesample);
        holder.examViewStartTime.setText(startsample);

        TimerstartTime = System.currentTimeMillis();

        if (TimerstartTime < examEndTimeLong) {
            holder.startExam.setTextSize(10);
            holder.startExam.setTextColor(Color.parseColor("#F3E50C"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
            formatter.setLenient(false);

            mCountDownTimer = new CountDownTimer(examEndTimeLong, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    TimerstartTime = TimerstartTime - 1;
                    Long serverUptimeSeconds =
                            (millisUntilFinished - TimerstartTime) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
               // txtViewDays.setText(daysLeft);

                    String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                    // endTimeViewHours.setText(hoursLeft);

                    String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                    // endTimeViewMinite.setText(minutesLeft);

                    String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                    // endTimeViewSecound.setText(secondsLeft);

                    String leftTime = daysLeft +"d " + hoursLeft + ":" + minutesLeft + ":" + secondsLeft;
                    holder.startExam.setText(leftTime);
                }

                @Override
                public void onFinish() {

                    mCountDownTimer.onFinish();
                }
            }.start();
        }else if (TimerstartTime < examStartTimeLong && TimerstartTime > examEndTimeLong ){
            holder.startExam.setText("Exam running");
            holder.startExam.setTextSize(10);
            holder.startExam.setTextColor(Color.parseColor("#38CA18"));
        }
        else {
            holder.startExam.setText("exam end");
            holder.startExam.setTextSize(10);
            holder.startExam.setTextColor(Color.parseColor("#D83833"));
        }
       // holder.examViewDate.setText(model.getExamName());

      //  holder.examViewStartTime.setText();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_exam_view_layout,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView examViewTitle;
        TextView examViewCatagory;
        TextView examViewDuration;
        TextView examViewMark;
        TextView examViewDate;
        TextView startExam;
        TextView examViewStartTime;

        public NoteHolder(View itemView) {
            super(itemView);
            examViewTitle = itemView.findViewById(R.id.examViewTitle);
            examViewCatagory = itemView.findViewById(R.id.examViewCatagory);
            examViewDuration = itemView.findViewById(R.id.examViewDuration);
            examViewMark = itemView.findViewById(R.id.examViewMark);
            examViewDate = itemView.findViewById(R.id.examViewDate);
            examViewStartTime = itemView.findViewById(R.id.examViewStartTime);
            startExam = itemView.findViewById(R.id.startExam);


            startExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onStarExamClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onStarExamClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
