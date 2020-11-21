package com.mijan.classroutin.adapter;

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
import com.mijan.classroutin.Note.MCQQuastionNote;
import com.mijan.classroutin.R;

public class MCQCerrectAnsAdapter extends FirestoreRecyclerAdapter<MCQQuastionNote, MCQCerrectAnsAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public MCQCerrectAnsAdapter(@NonNull FirestoreRecyclerOptions<MCQQuastionNote> options) {
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
        holder.cerrectAns.setText("Correct answer : "+model.getCurectans());
        if (model.getCurectansSubmitedByStuden() != null){

            holder.submitStudent.setText("Submit answer : "+model.getCurectansSubmitedByStuden());
        }

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcq_ans_layout,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView QNO;
        TextView question;
        TextView questionA;
        TextView questionB;
        TextView questionC;
        TextView questionD;
        TextView cerrectAns;
        TextView submitStudent;

        public NoteHolder(View itemView) {
            super(itemView);
            QNO = itemView.findViewById(R.id.ansQNO);
            question = itemView.findViewById(R.id.Qusntion);
            questionA = itemView.findViewById(R.id.QusntinAnsA);
            questionB = itemView.findViewById(R.id.QusntinAnsB);
            questionC = itemView.findViewById(R.id.QusntinAnsC);
            questionD = itemView.findViewById(R.id.QusntinAnsD);
            cerrectAns = itemView.findViewById(R.id.QusntinCorrectanswer);
            submitStudent = itemView.findViewById(R.id.submitStudent);

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
