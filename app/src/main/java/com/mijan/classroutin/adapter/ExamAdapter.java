package com.mijan.classroutin.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mijan.classroutin.Note.Exam;
import com.mijan.classroutin.R;

public class ExamAdapter extends FirestoreRecyclerAdapter<Exam, ExamAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public ExamAdapter(@NonNull FirestoreRecyclerOptions<Exam> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Exam model) {
        holder.ExamName.setText(model.getExamName());
        holder.ExamTopic.setText(model.getExamTopic());
        holder.ExamSubject.setText(model.getExamSubject());
        holder.ExamDate.setText(model.getExamDate());
        holder.ExamTime.setText(model.getExamTime());
        holder.ExamCode.setText(model.getExamHoll());
        holder.ExamHoll.setText(model.getExamCode());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_single_item,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView ExamName;
        TextView ExamTopic;
        TextView ExamDate;
        TextView ExamTime;
        TextView ExamSubject;
        TextView ExamHoll;
        TextView ExamCode;

        public NoteHolder(View itemView) {
            super(itemView);
            ExamName = itemView.findViewById(R.id.examName);
            ExamTopic = itemView.findViewById(R.id.examTopic);
            ExamDate = itemView.findViewById(R.id.ExamDate);
            ExamTime = itemView.findViewById(R.id.ExamTime);
            ExamSubject = itemView.findViewById(R.id.examSubject);
            ExamHoll = itemView.findViewById(R.id.ExamHall);
            ExamCode = itemView.findViewById(R.id.curseCode);

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
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
