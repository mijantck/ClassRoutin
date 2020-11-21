package com.mijan.classroutin.adapter;

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
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.Note.TheoryExamNote;
import com.mijan.classroutin.R;
import com.squareup.picasso.Picasso;

public class TheoryExamQuastonViewrAdapter extends FirestoreRecyclerAdapter<TheoryExamNote, TheoryExamQuastonViewrAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public TheoryExamQuastonViewrAdapter(@NonNull FirestoreRecyclerOptions<TheoryExamNote> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull TheoryExamNote model) {

        holder.examPepar_subjectName.setText(model.getTheoryExamSubjectName());
        holder.examPepar_number.setText(model.getTheoryExamSubjectPageNumer()+"");
        Picasso.get().load(model.getTheoryExamSubjectImageURL()).into(holder.examPepar_imageView);

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.theory_exam_quastion_view_single_layout,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView examPepar_subjectName;
        TextView examPepar_number;
        ImageView examPepar_imageView;


        public NoteHolder(View itemView) {
            super(itemView);
            examPepar_subjectName = itemView.findViewById(R.id.examPepar_subjectName);
            examPepar_number = itemView.findViewById(R.id.examPepar_number);
            examPepar_imageView = itemView.findViewById(R.id.examPepar_imageView);

            examPepar_imageView.setOnClickListener(new View.OnClickListener() {
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
