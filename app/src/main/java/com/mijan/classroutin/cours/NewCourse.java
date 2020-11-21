package com.mijan.classroutin.cours;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.Course_Note;
import com.mijan.classroutin.Note.ParticifentNote;
import com.mijan.classroutin.R;

import java.util.Random;

public class NewCourse extends AppCompatActivity {


    TextInputEditText courseCode, courseName,section, coursTeacher;

    ProgressDialog progressDialog;

    TextView Add;

    String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    String randomSerchCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);


        Add = findViewById(R.id.save_Course);
        courseCode = findViewById(R.id.my_course_code_edite);
        courseName = findViewById(R.id.my_course_name_edite);
        section = findViewById(R.id.my_course_section_edite);
        coursTeacher = findViewById(R.id.my_course_teacher_edite);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(NewCourse.this);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("ProgressDialog"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner


              randomSerchCode =   generateRandom(candidateChars);


                final String courseCode1 = courseCode.getText().toString();
                final String courseName1 = courseName.getText().toString();
                final String section1 = section.getText().toString();
                final String coursTeacher1 = coursTeacher.getText().toString();

                if (courseCode1.trim().isEmpty() || courseName1.trim().isEmpty() || coursTeacher1.trim().isEmpty()) {
                    Toast.makeText(NewCourse.this, "Please insert All Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();



                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                boolean access = false;


                final CollectionReference notebookRef = FirebaseFirestore.getInstance()
                        .collection("Global Group");
                notebookRef.add(new Course_Note(randomSerchCode,null,courseCode1, courseName1,section1, coursTeacher1,uid,false)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){

                            final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            final CollectionReference postref = rootRef.collection("Users").document(currentUser.getUid().toString()).collection("My Group");
                            final String idnew = task.getResult().getId();


                            notebookRef.document(idnew).update("CourseID",idnew)

                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    postref.document(idnew).set(new Course_Note(randomSerchCode,idnew,courseCode1, courseName1,section1, coursTeacher1,uid,true))

                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    String  id = idnew;
                                                    String user_id = currentUser.getUid();
                                                    String user_name = currentUser.getDisplayName();
                                                    String user_email = currentUser.getEmail();
                                                    String user_URL = currentUser.getPhotoUrl().toString();
                                                    CollectionReference PCollection =  rootRef.collection("Global Group").document(id).collection("Participant");

                                                    PCollection.document(user_id).set(new ParticifentNote(id,id,user_id,user_name,user_email,user_URL))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    startActivity(new Intent(NewCourse.this,MyCourse.class));
                                                                    finish();

                                                                }
                                                            });
                                                    Toast.makeText(NewCourse.this, "Course added", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });


                           /* //user add
                            final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            final String courseCode1 = courseCode.getText().toString();
                            final String courseName1 = courseName.getText().toString();
                            final String section1 = section.getText().toString();
                            final String coursTeacher1 = coursTeacher.getText().toString();
                            boolean access = false;
                            final String user_id = currentUser.getUid();

                            CollectionReference postref = rootRef.collection("Users").document(user_id).collection("My Group");

                            FirebaseMessaging.getInstance().subscribeToTopic(idnew.toString());

                            postref.document(idnew).set(new Course_Note(randomSerchCode,idnew,courseCode1, courseName1,section1, coursTeacher1,uid,true))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    String user_id = currentUser.getUid();

                                    CollectionReference postref = rootRef.collection("Users").document(user_id).collection("My Group");

                                    postref.document(idnew).set(new Course_Note(randomSerchCode,idnew,courseCode1, courseName1,section1, coursTeacher1,uid,true))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                        }
                                    });
                                }
                            });*/


                        }
                    }
                });

                progressDialog.dismiss();

                Intent search_courseIntent = new Intent(NewCourse.this, AddInMyGroupActivity.class);
                startActivity(search_courseIntent);
                finish();

                Toast.makeText(NewCourse.this, "Course added", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private static String generateRandom(String aToZ) {
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 8 ; i++) {
            int randIndex=rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }
}
