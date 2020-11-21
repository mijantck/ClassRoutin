package com.mijan.classroutin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mijan.classroutin.Note.Course_Note;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.R;
import com.mijan.classroutin.cours.AddInMyGroupActivity;
import com.mijan.classroutin.cours.MyCourse;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class GroupSearchAdapter extends RecyclerView.Adapter<GroupSearchAdapter.GroupViewHolder> {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();


    public List<DocumentSnapshot> groupsnapshot;

    public GroupSearchAdapter(List<DocumentSnapshot> groupsnapshot) {
        this.groupsnapshot = groupsnapshot;
        //  notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_group_signle_item, viewGroup, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder groupViewHolder, final int i) {


        final DocumentSnapshot model = groupsnapshot.get(i);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final Course_Note course_note = groupsnapshot.get(i).toObject(Course_Note.class);

        groupViewHolder.course_view_id.setText(course_note.getCourseCode());
        groupViewHolder.course_Name_view_id.setText(course_note.getCourseName());
        groupViewHolder.section.setText(course_note.getSection());
        groupViewHolder.Teacher_Name_ID.setText(course_note.getTeacher_name());


        if (course_note.getCourseCreator().equals(uid)) {
            groupViewHolder.deletePost.setVisibility(VISIBLE);
            groupViewHolder.deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Are you sure?")
                            .setIcon(R.drawable.ic_delete)
                            .setMessage(course_note.getCourseName() + " Delete")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteData(i);
                                    notifyItemRemoved(i);

                                //   notifyDataSetChanged();

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


        }


        groupViewHolder.addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = currentUser.getUid();
                String user_name = currentUser.getDisplayName();
                String user_email = currentUser.getEmail();
                String user_URL = currentUser.getPhotoUrl().toString();
                String id = model.getId();
                String courseCode = course_note.getCourseCode();
                String courseName = course_note.getCourseName();
                String section = course_note.getSection();
                String courseTeacher = course_note.getTeacher_name();
                String RandomSerchCode = course_note.getRandomSerchCode();
                String coursCretor = course_note.getCourseCreator();

                groupViewHolder.addGroup.setVisibility(GONE);
                groupViewHolder.canclegroup.setVisibility(VISIBLE);

                FirebaseMessaging.getInstance().subscribeToTopic(id);

                CollectionReference postref = rootRef.collection("Users").document(user_id).collection("My Group");
                CollectionReference PCollection =  rootRef.collection("Global Group").document(id).collection("Participant");


                postref.document(id).set(new Course_Note(RandomSerchCode,id, courseCode, courseName,section, courseTeacher, coursCretor, true));
                PCollection.document(user_id).set(new ParticifentNote(id,id,user_id,user_name,user_email,user_URL))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent intent = new Intent(groupViewHolder.itemView.getContext(),
                                        MyCourse.class);
                                groupViewHolder.itemView.getContext().startActivity(intent);

                            }
                        });

            }
        });

        groupViewHolder.canclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                groupViewHolder.addGroup.setVisibility(VISIBLE);
                groupViewHolder.canclegroup.setVisibility(GONE);

                String id = model.getId();


                String user_id = currentUser.getUid();

                FirebaseFirestore docRef = FirebaseFirestore.getInstance();

                DocumentReference selectedDoc = docRef.collection("Users")
                        .document(user_id).collection("My Group").document(id);
                selectedDoc.delete();


            }
        });


    }


    @Override
    public int getItemCount() {

        return groupsnapshot.size();

    }

    public void setGroupsnapshot(List<DocumentSnapshot> groupsnapshot) {
        this.groupsnapshot = groupsnapshot;

       notifyDataSetChanged();

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {


        public TextView course_view_id, section, course_Name_view_id, Teacher_Name_ID;
        public Button addGroup, canclegroup, deletePost;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);


            course_view_id = itemView.findViewById(R.id.course_view_id);
            section = itemView.findViewById(R.id.sectionIdS);
            course_Name_view_id = itemView.findViewById(R.id.course_Name_view_id);
            Teacher_Name_ID = itemView.findViewById(R.id.Teacher_Name_ID);
            addGroup = itemView.findViewById(R.id.groupAddButton);
            canclegroup = itemView.findViewById(R.id.groupCancleButton);
            deletePost = itemView.findViewById(R.id.deleteCousre);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != -1) { // Avoid Exception

                    }
                }
            });
        }

    }

    public void deleteData(int index) {


        FirebaseFirestore docRef = FirebaseFirestore.getInstance();
        docRef.collection("Global Group")
                .document(groupsnapshot.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

    }


}
