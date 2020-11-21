package com.mijan.classroutin.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mijan.classroutin.cours.GroupSingleView;
import com.mijan.classroutin.Note.Course_Note;
import com.mijan.classroutin.R;

import java.util.List;

public class CourseAdapter extends FirestoreRecyclerAdapter<Course_Note, CourseAdapter.CourseHOlder> {


    List<DocumentSnapshot> groupsnapshot;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private OnItemClickListener listener;



    public CourseAdapter(@NonNull FirestoreRecyclerOptions<Course_Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CourseHOlder holder, final int position, @NonNull final Course_Note model) {


        holder.courseCode.setText(model.getCourseCode());
        holder.courseName.setText(model.getCourseName());


        holder.section.setText(model.getSection());
        holder.teacherName.setText(model.getTeacher_name());
        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setMessage("Unfollow "+model.getCourseName())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String user_id = currentUser.getUid();
                                String id = getSnapshots().getSnapshot(position).getId();

                                FirebaseMessaging.getInstance().unsubscribeFromTopic(id);

                                FirebaseFirestore docRef = FirebaseFirestore.getInstance();

                                DocumentReference selectedDoc = docRef.collection("Users")
                                        .document(user_id).collection("My Group").document(id);
                                selectedDoc.delete();

                                DocumentReference  PCollection =  docRef.collection("Global Group")
                                        .document(id).collection("Participant").document(user_id);
                                PCollection.delete();


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .show();



            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //how document Id
                DocumentSnapshot snapshot  = getSnapshots().getSnapshot(holder.getAdapterPosition());

                String id = getSnapshots().getSnapshot(position).getId();
                String CoureseCode= model.getCourseCode();
                String CourseName = model.getCourseName();
                String CourseserchID = model.getRandomSerchCode();
                String CoursCretorID = model.getCourseCreator();
                String CoursThecher = model.getTeacher_name();

                Intent newsfeedActivity = new Intent(v.getContext(), GroupSingleView.class);

                        newsfeedActivity.putExtra("courseId",id);
                        newsfeedActivity.putExtra("coursecourse",CoureseCode);
                        newsfeedActivity.putExtra("courseName",CourseName);
                        newsfeedActivity.putExtra("getRandomSerchCode",CourseserchID);
                        newsfeedActivity.putExtra("CoursCretorID",CoursCretorID);
                        newsfeedActivity.putExtra("CoursThecher",CoursThecher);
                v.getContext().startActivity(newsfeedActivity);

            }
        });
    }
    public void setGroupsnapshot(List<DocumentSnapshot> groupsnapshot) {
        this.groupsnapshot = groupsnapshot;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseHOlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.new_course_silge_layout,viewGroup,false);
        return new CourseHOlder(v);
    }
    class CourseHOlder extends RecyclerView.ViewHolder {
        TextView courseCode;
        TextView courseName;
        TextView section;
        TextView teacherName;
      //  CardView cardView;
        TextView  unfollow;
        public CourseHOlder(@NonNull View itemView) {
            super(itemView);
          //  cardView = itemView.findViewById(R.id.coursCardView);
            courseCode = itemView.findViewById(R.id.coursCode);
            courseName= itemView.findViewById(R.id.coursName);
            section = itemView.findViewById(R.id.sectionName);
            unfollow = itemView.findViewById(R.id.unfollowTexView);
            teacherName= itemView.findViewById(R.id.techearName_course);

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
