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
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnsPerticifentAdapter extends FirestoreRecyclerAdapter<AsingmentSubmitNote, AnsPerticifentAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public AnsPerticifentAdapter(@NonNull FirestoreRecyclerOptions<AsingmentSubmitNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull AsingmentSubmitNote model) {
        holder.perticipentName.setText("Name :"+model.getName());
        holder.perticipentEmail.setText("Rol number :"+model.getRol()+"");

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.participent_single_layout,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image_participent;
        TextView perticipentName;
        TextView perticipentEmail;
        TextView perticipentMark;

        public NoteHolder(View itemView) {
            super(itemView);
            profile_image_participent = itemView.findViewById(R.id.profile_image_participent);
            perticipentName = itemView.findViewById(R.id.perticipentName);
            perticipentEmail = itemView.findViewById(R.id.perticipentEmail);
            perticipentMark = itemView.findViewById(R.id.perticipentMark);

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
