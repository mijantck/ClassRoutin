package com.mijan.classroutin.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.Note.TheoryExamNote;
import com.mijan.classroutin.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AssingmentViewrAdapter extends FirestoreRecyclerAdapter<AsingmentSubmitNote, AssingmentViewrAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public AssingmentViewrAdapter(@NonNull FirestoreRecyclerOptions<AsingmentSubmitNote> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull AsingmentSubmitNote model) {

        long TimerstartTime = System.currentTimeMillis();
        DateFormat startTimesimple = new SimpleDateFormat("HH:mm:ss");
        DateFormat datesimple = new SimpleDateFormat("yyyy-MM-dd");
        String datesample = datesimple.format(TimerstartTime);
        String startsample = startTimesimple.format(model.getEndTime());

        holder.assingment_Title_view.setText(model.getName());
        holder.assingment_date_view.setText(model.getDate()+"\n"+startsample);

       if (TimerstartTime <model.getEndTime()){
           holder.assingment_Stutas_view.setText("Running");
           holder.assingment_Stutas_view.setTextColor(Color.GREEN);

       }else {
           holder.assingment_Stutas_view.setText("late");
           holder.assingment_Stutas_view.setTextColor(Color.RED);

       }


       // holder.examPepar_number.setText(model.getPage()+"");
      //  Picasso.get().load(model.getImageURL()).into(holder.examPepar_imageView);

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assingment_silgel_layout,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView assingment_date_view;
        TextView assingment_Title_view;
        TextView assingment_Attachment_view;
        TextView assingment_Stutas_view;


        public NoteHolder(View itemView) {
            super(itemView);
            assingment_date_view = itemView.findViewById(R.id.assingment_date_view);
            assingment_Title_view = itemView.findViewById(R.id.assingment_Title_view);
            assingment_Attachment_view = itemView.findViewById(R.id.assingment_Attachment_view);
            assingment_Stutas_view = itemView.findViewById(R.id.assingment_Stutas_view);

            assingment_Attachment_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onImageItemClick(getSnapshots().getSnapshot(position), position);
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
        void onImageItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
