package com.mijan.classroutin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mijan.classroutin.Note.MCQQuastionNote;
import com.mijan.classroutin.R;

public class MCQCerrectAns_Examinniter_Adapter extends FirestoreRecyclerAdapter<MCQQuastionNote, MCQCerrectAns_Examinniter_Adapter.NoteHolder> {

    private OnItemClickListener listener;

    public MCQCerrectAns_Examinniter_Adapter(@NonNull FirestoreRecyclerOptions<MCQQuastionNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull MCQQuastionNote model) {

        String qn = model.getQuaNo()+"";
        holder.QNO.setText(qn);
        holder.question.setText(model.getQuastionn());
        holder.questionA.setText(model.getAnsAdiscription());
        holder.questionB.setText(model.getAnsBdiscription());
        holder.questionC.setText(model.getAnsCdiscription());
        holder.questionD.setText(model.getAnsDdiscription());


    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcq_qustion_view_examiner,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView QNO;
        TextView question;
        RadioButton questionA;
        RadioButton questionB;
        RadioButton questionC;
        RadioButton questionD;
        TextView submit_answer_examiner;

        public NoteHolder(View itemView) {
            super(itemView);
            QNO = itemView.findViewById(R.id.ansQNO_examiner);
            question = itemView.findViewById(R.id.Qusntion_examiner);
            questionA = itemView.findViewById(R.id.QusntinAnsA_examiner);
            questionB = itemView.findViewById(R.id.QusntinAnsB_examiner);
            questionC = itemView.findViewById(R.id.QusntinAnsC_examiner);
            questionD = itemView.findViewById(R.id.QusntinAnsD_examiner);
            submit_answer_examiner = itemView.findViewById(R.id.submit_answer_examiner);


             submit_answer_examiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
            questionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAnsAClick(getSnapshots().getSnapshot(position), position);
                        questionA.setChecked(true);
                        questionB.setChecked(false);
                        questionC.setChecked(false);
                        questionD.setChecked(false);
                    }
                }
            });
            questionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAnsBClick(getSnapshots().getSnapshot(position), position);
                        questionA.setChecked(false);
                        questionB.setChecked(true);
                        questionC.setChecked(false);
                        questionD.setChecked(false);
                    }
                }
            });
            questionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAnsCClick(getSnapshots().getSnapshot(position), position);
                        questionA.setChecked(false);
                        questionB.setChecked(false);
                        questionC.setChecked(true);
                        questionD.setChecked(false);
                    }
                }
            });
            questionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAnsDClick(getSnapshots().getSnapshot(position), position);
                        questionA.setChecked(false);
                        questionB.setChecked(false);
                        questionC.setChecked(false);
                        questionD.setChecked(true);
                    }
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onAnsAClick(DocumentSnapshot documentSnapshot, int position);
        void onAnsBClick(DocumentSnapshot documentSnapshot, int position);
        void onAnsCClick(DocumentSnapshot documentSnapshot, int position);
        void onAnsDClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
