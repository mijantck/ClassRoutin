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
import com.mijan.classroutin.Note.AttendenceNote;
import com.mijan.classroutin.R;

public class RolNumberAttendeceDateAdapter extends FirestoreRecyclerAdapter<AttendenceNote, RolNumberAttendeceDateAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public RolNumberAttendeceDateAdapter(@NonNull FirestoreRecyclerOptions<AttendenceNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull AttendenceNote model) {

        holder.rolNumerViewLayout.setText(model.getRol()+"");
        holder.attentViewLayout.setText(model.getAttend());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rol_number_attent_layout_single,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView rolNumerViewLayout;
        TextView attentViewLayout;

        public NoteHolder(View itemView) {
            super(itemView);
            rolNumerViewLayout = itemView.findViewById(R.id.rolNumerViewLayout);
            attentViewLayout = itemView.findViewById(R.id.attentViewLayout);

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
