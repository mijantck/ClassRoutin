package com.mijan.classroutin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnsforAllAdapter extends FirestoreRecyclerAdapter<AsingmentSubmitNote, AnsforAllAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public AnsforAllAdapter(@NonNull FirestoreRecyclerOptions<AsingmentSubmitNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull AsingmentSubmitNote model) {

        holder.rolviewIn_recyclview.setText("Page : "+model.getRol()+"");

        Picasso.get().load(model.getImageURL()).into(holder.photoview_in_recyclerView);

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ans_view_layot_single,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView rolviewIn_recyclview;
        PhotoView photoview_in_recyclerView;

        public NoteHolder(View itemView) {
            super(itemView);
            rolviewIn_recyclview = itemView.findViewById(R.id.rolviewIn_recyclview);
            photoview_in_recyclerView = itemView.findViewById(R.id.photoview_in_recyclerView);

            photoview_in_recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onImageClick(getSnapshots().getSnapshot(position), position);
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
        void onImageClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
