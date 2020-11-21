package com.mijan.classroutin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mijan.classroutin.Note.AttendenceNote;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AttendeceDateAdapter extends FirestoreRecyclerAdapter<AttendenceNote, AttendeceDateAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public AttendeceDateAdapter(@NonNull FirestoreRecyclerOptions<AttendenceNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull AttendenceNote model) {
        long time = System.currentTimeMillis();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(time);
        int date = Integer.parseInt(todayString);

        if (date== model.getDate()){
            holder.dateViewAttendece.setText("Today");
        }else {
            holder.dateViewAttendece.setText(model.getDateS());
        }

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_view_layout_attendece,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView dateViewAttendece;

        public NoteHolder(View itemView) {
            super(itemView);
            dateViewAttendece = itemView.findViewById(R.id.dateViewAttendece);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {

                        try {
                            listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
