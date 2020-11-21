package com.mijan.classroutin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mijan.classroutin.Note.NewsAddedNote;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class NewsFeedAddedActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    StorageReference mStorageReference;
    StorageReference mStorageReferenceImage;
    DatabaseReference mDatabaseReference;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAACFk86E0:APA91bGg2pivVBN3n8DjOHgqp5-bHLHwM8WwB4B1Y8pjsvTULqkSU_7th2Vz7FzhWCc0VAUtVcuMIsSKSqBuNREz6m0cK_il_Nea8KRMROoL91TNHDFrehSVWICXyI_SfRaH5X6PQxyD";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    FirebaseFirestore Ref;
    CollectionReference notebookRef;
    CollectionReference indivisulpostCourse;

    private Uri mImageUri;
    private Uri pdf;


    private EditText NewsFeeed,fileName1;
    private Button cancel_Buttton,PDF, ImageChossser;
    private TextView viewPdf,News_Added_Button;
    private ImageView newsFeedAddedImage;
    String courseId, coursecode, courseName,posrtType;

    final static int PICK_PDF_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 1;


    private AdView mAdView;

    String postcretorURL ;
    String postcretorName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_added);

        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("News Update");
        String user_id = currentUser.getUid();

        postcretorURL = currentUser.getPhotoUrl().toString();
        postcretorName = currentUser.getDisplayName();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference().child(user_id).child("PDF_PERSONAL").child(currentUser.getUid());
        mStorageReferenceImage = FirebaseStorage.getInstance().getReference().child(user_id).child("IMAGE_PERSONAL").child(currentUser.getUid());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        Ref = FirebaseFirestore.getInstance();

       notebookRef = Ref.collection("Post");

        NewsFeeed = findViewById(R.id.news_feed_Edite);
        fileName1 = findViewById(R.id.fileName);
        News_Added_Button = findViewById(R.id.News_Add_Button);
        newsFeedAddedImage = findViewById(R.id.newsFeedAddedImage);
        PDF = findViewById(R.id.PDFChosser);
        ImageChossser = findViewById(R.id.ImageChosser);

        viewPdf = findViewById(R.id.pdfView);

        Bundle bundle = getIntent().getExtras();

        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        posrtType = bundle.getString("posrtType");

        if (posrtType != null) {

            indivisulpostCourse = Ref.collection("Global Group").document(courseId).collection(posrtType);
            
        }



        PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });
        ImageChossser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIMEGE();
            }
        });


        mAdView = findViewById(R.id.adViewnewsfeed);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        News_Added_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkIntert()) {


                    if (pdf == null && mImageUri == null) {

                        Added_News();

                    } else if (pdf != null && mImageUri != null) {

                        imagePdfUpload(mImageUri, pdf);

                    } else if (pdf != null) {

                        uploadPDF(pdf);
                    } else if (mImageUri != null) {

                        imageUpload(mImageUri);
                    }
                }else {

                    Toast.makeText(NewsFeedAddedActivity.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();
                }

             //   notificationSend(courseName.toString());

            }
        });

    }

    public void Added_News() {

        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        long start = System.currentTimeMillis();

        String id = courseId;
        String course = coursecode;
        String CourseName = courseName;
        String user_id = currentUser.getUid();
        String date = currentDateandTime;
        long time = start / 1000;

        String news_feed = NewsFeeed.getText().toString();



        if (news_feed != null) {

            if (posrtType !=null){
                indivisulpostCourse.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed,user_id,postcretorURL,postcretorName));
                 finish();
            }else {
                notebookRef.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed,user_id,postcretorURL,postcretorName));
                finish();
                Toast.makeText(this, " Added", Toast.LENGTH_SHORT).show();
            }

            notificationSend(CourseName);

        }else {

            Toast.makeText(this, " Writ Something..", Toast.LENGTH_SHORT).show();

        }
    }


    @AfterPermissionGranted(PICK_IMAGE_REQUEST)
    private void getPDF() {

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {

            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                    PICK_IMAGE_REQUEST , perms);
        }

    }


    @AfterPermissionGranted(PICK_IMAGE_REQUEST)
    private void getIMEGE() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                    PICK_IMAGE_REQUEST , perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                if (data.getData() != null) {
                    pdf = data.getData();
                    Uri uri1 = data.getData();
                    getFileName(uri1);

                } else {
                    Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                if (data.getData() != null) {
                    mImageUri = data.getData();
                    Picasso.get().load(mImageUri).into(newsFeedAddedImage);
                } else {
                    Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                }
            }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void uploadPDF(final Uri pdf) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String fileName = System.currentTimeMillis() + "";

        final StorageReference putpdf = mStorageReference.child(fileName);

        putpdf.putFile(pdf)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        putpdf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                long start = System.currentTimeMillis();
                                String id = courseId;
                                String course = coursecode;
                                String fileName = fileName1.getText().toString();
                                String user_id = currentUser.getUid();

                                String CourseName = courseName;
                                String date = currentDateandTime;
                                long time = start / 1000;
                                String news_feed = NewsFeeed.getText().toString();


                                if (posrtType != null){
                                    indivisulpostCourse.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(),fileName,user_id,postcretorURL,postcretorName));
                                }else {
                                    // notebookRef.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(), "pdf Name"));
                                    notebookRef.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(),fileName,user_id,postcretorURL,postcretorName));
                                }
                                progressDialog.dismiss();

                                notificationSend(CourseName);
                                Intent intent = new Intent(NewsFeedAddedActivity.this,NewsFeedActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int countProgres = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(countProgres);

            }
        });


    }

    public void imageUpload(final Uri mImageUri) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String fileName = System.currentTimeMillis() + "";

        final StorageReference putImage = mStorageReferenceImage.child(fileName);

        putImage.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        putImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                long start = System.currentTimeMillis();

                                String id = courseId;
                                String course = coursecode;
                                String CourseName = courseName;
                                String fileName = fileName1.getText().toString();
                                String user_id = currentUser.getUid();
                                String date = currentDateandTime;
                                long time = start / 1000;
                                String news_feed = NewsFeeed.getText().toString();

                                if (posrtType != null ){
                                    indivisulpostCourse.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(),user_id,postcretorURL,postcretorName));
                                }else {
                                    notebookRef.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(),user_id,postcretorURL,postcretorName));
                                }

                                progressDialog.dismiss();
                                notificationSend(CourseName);
                                Intent intent = new Intent(NewsFeedAddedActivity.this,NewsFeedActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int countProgres = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(countProgres);
            }
        });

    }


    public void imagePdfUpload(final Uri mImageUri1, final Uri pdf) {


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String fileName = System.currentTimeMillis() + "";

        final StorageReference putImage = mStorageReferenceImage.child(fileName);
        final StorageReference putpdf = mStorageReference.child(fileName);


        putpdf.putFile(pdf).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                putImage.putFile(mImageUri1)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                putImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {

                                        putpdf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri1) {


                                                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                                long start = System.currentTimeMillis();


                                                String id = courseId;
                                                String course = coursecode;
                                                String CourseName = courseName;
                                                String user_id = currentUser.getUid();
                                                String fileName = fileName1.getText().toString();
                                                String date = currentDateandTime;
                                                long time = start / 1000;
                                                String news_feed = NewsFeeed.getText().toString();

                                                if (!posrtType.isEmpty()){
                                                    indivisulpostCourse.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(), uri1.toString(), fileName,user_id,postcretorURL,postcretorName));

                                                }else {
                                                    notebookRef.add(new NewsAddedNote(id, course, CourseName, date, time, news_feed, uri.toString(), uri1.toString(), fileName,user_id,postcretorURL,postcretorName));
                                                }

                                                progressDialog.dismiss();

                                                notificationSend(CourseName);

                                                Intent intent = new Intent(NewsFeedAddedActivity.this,NewsFeedActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }
                                });

                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int countProgres = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(countProgres);
            }
        });

    }

    public String getFileName(Uri uri1) {
        String result = null;
        if (uri1.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri1, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    fileName1.setText(result+"");
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri1.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
               viewPdf = (EditText) findViewById(R.id.fileName);
                viewPdf.setText(result);
            }
        }
        return result;
    }

    public  void  notificationSend( String courseName){

        TOPIC = "/topics/"+courseId; //topic must match with what the receiver subscribed to

        String news_feed = NewsFeeed.getText().toString();
        NOTIFICATION_TITLE = courseName;
        NOTIFICATION_MESSAGE = news_feed;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
           // Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    //    Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewsFeedAddedActivity.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
