package com.mijan.classroutin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.Note.TheoryExamNote;
import com.mijan.classroutin.onlineexam.TheoryExamCreat;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class TheoryAnsView extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;


    private static final int PICK_IMAGE_REQUEST = 1;
    private  int IMAGE_CAPTURE_CODE = 999;
    public Uri mImageUri ;
    PhotoView ImageChosserQuastionViewforStudenttheoryAns;
    Button ImageChosserQuastionforStudenttheoryAns;
    TextView tehoryExamQuasAddforStudenttheoryAns,timecounter;
    TextInputEditText examNameTheoryforStudenttheoryAns,examNameTheoryPageNumberforStudenttheoryAns;
    long examStartTimeLong,examEndTimeLong,TimerstartTime;

    CountDownTimer mCountDownTimer;
    String coursId,examId,examStartTime,examEndTime;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_ans_view);
        Bundle bundle = getIntent().getExtras();
        coursId = bundle.getString("coursId");
        examId = bundle.getString("examId");

        progressDialog = new ProgressDialog(TheoryAnsView.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        Toast.makeText(this, coursId+"\n"+examId, Toast.LENGTH_SHORT).show();
        examStartTime = bundle.getString("examStartTime");
        examEndTime = bundle.getString("examEndTime");
        examStartTimeLong = Long.parseLong(examStartTime);
        examEndTimeLong = Long.parseLong(examEndTime);

        timecounter = findViewById(R.id.timecounter);
        tehoryExamQuasAddforStudenttheoryAns = findViewById(R.id.tehoryExamQuasAddforStudenttheoryAns);
        examNameTheoryforStudenttheoryAns = findViewById(R.id.examNameTheoryforStudenttheoryAns);
        examNameTheoryPageNumberforStudenttheoryAns = findViewById(R.id.examNameTheoryPageNumberforStudenttheoryAns);
        ImageChosserQuastionforStudenttheoryAns = findViewById(R.id.ImageChosserQuastionforStudenttheoryAns);
        ImageChosserQuastionViewforStudenttheoryAns = findViewById(R.id.ImageChosserQuastionViewforStudenttheoryAns);

        tehoryExamQuasAddforStudenttheoryAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String rol = examNameTheoryforStudenttheoryAns.getText().toString();
                String pag = examNameTheoryPageNumberforStudenttheoryAns.getText().toString();
                if (rol.isEmpty() || pag.isEmpty() || mImageUri == null){
                    Toast.makeText(TheoryAnsView.this, " Please  all input ", Toast.LENGTH_SHORT).show();
                    return;

                }
                progressDialog.show();

                uploadImageUri(mImageUri);

            }
        });
        ImageChosserQuastionforStudenttheoryAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(TheoryAnsView.this);
                String[] option = {"CAMERA", "GALLERY"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                            mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                            startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
                        }
                        if (which == 1) {
                            getIMEGE();
                        }
                    }
                }).create().show();
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        formatter.setLenient(false);
        TimerstartTime = System.currentTimeMillis();
        mCountDownTimer = new CountDownTimer(examEndTimeLong, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                TimerstartTime=TimerstartTime-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - TimerstartTime) / 1000;

             /*   String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                txtViewDays.setText(daysLeft);*/

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);

                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);


                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);


                timecounter.setText(hoursLeft+"h"+minutesLeft+"m"+secondsLeft+"s");
            }
            @Override
            public void onFinish() {
                mCountDownTimer.onFinish();
            }
        }.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterPermissionGranted(PICK_IMAGE_REQUEST)
    private void getIMEGE() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<Integer> lPermission = new ArrayList<>();
            List<String> stringPermissionList1 = getPermissionList();
            for (int i = 0; i < stringPermissionList1.size(); i++) {
                lPermission.add(ContextCompat.checkSelfPermission(getApplicationContext(), stringPermissionList1.get(i)));
            }
            boolean bPermissionDenied = false;
            for (int i = 0; i < lPermission.size(); i++) {
                int a = lPermission.get(i);
                if (PackageManager.PERMISSION_DENIED == a) {
                    bPermissionDenied = true;
                    break;
                }
            }

            if (bPermissionDenied) {
                String sMessage = "Please allow all permissions shown in upcoming dialog boxes, so that app functions properly";
                //make request to the user
                List<String> stringPermissionList = getPermissionList();
                String[] sPermissions = stringPermissionList.toArray(new String[stringPermissionList.size()]);

                //request the permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(sPermissions, PICK_IMAGE_REQUEST);
                }
            } else {


            }


        } else {

        }

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Image Add "), PICK_IMAGE_REQUEST);

        } else {
           /* EasyPermissions.requestPermissions(this, "Permission  ",
                    PICK_IMAGE_REQUEST , perms);*/
        }
    }

    private List<String> getPermissionList(){
        List<String> stringPermissionList=new ArrayList<>();

        stringPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        stringPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return  stringPermissionList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isAllPermissionGranted = true;

        for (int i = 0; i < grantResults.length; i++) {
            int iPermission = grantResults[i];
            if (iPermission == PackageManager.PERMISSION_DENIED) {
                isAllPermissionGranted = false;
                break;
            }
        }
        if (isAllPermissionGranted) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Image Add "), PICK_IMAGE_REQUEST);
        } else {
            // Prompt the user to grant all permissions
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            ImageChosserQuastionViewforStudenttheoryAns.setImageURI(mImageUri);

        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(ImageChosserQuastionViewforStudenttheoryAns);


            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }}

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    private void uploadImageUri(Uri imageUri ){

        try {
            File file = new File(Environment.getExternalStorageDirectory(), "profilePicTemp");

            InputStream in = getContentResolver().openInputStream(imageUri);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();

            upload(file, new UploadCallback() {
                @Override
                public void onSuccess(final String downloadLink) {

                    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String uName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                    String rolNumber = examNameTheoryforStudenttheoryAns.getText().toString();
                    String subPNaber = examNameTheoryPageNumberforStudenttheoryAns.getText().toString();
                    int subPNaberInt = Integer.parseInt(subPNaber);
                    int rolNumberInt = Integer.parseInt(rolNumber);

                    Collection =  db.collection("Global Group").document(coursId).collection("Exam")
                            .document(examId).collection("ans");

                    Collection.add(new AsingmentSubmitNote(null,uID,uName,rolNumberInt,subPNaberInt,downloadLink))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        String id = task.getResult().getId();
                                        Collection.document(id).update("id",id)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(TheoryAnsView.this, "Your ans is submitted Page number : "+subPNaber, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }

                                }
                            });
                }


                @Override
                public void onFailed(String message) {
                }
            });

        } catch (Exception e){
            e.printStackTrace();

        }

    }

    private void upload(File file, final UploadCallback uploadCallback) {

        //this is for log message
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        //create file to request body and request
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), saveBitmapToFile(file));
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "filename.png", requestBody)
                .build();

        //request create for imgur
        final Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Client-ID 2f4dd94e6dbf1f1")
                .build();

        //okhttp client create
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .build();

        //network request so we need to run on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = client.newCall(request).execute();

                    if(response.isSuccessful()){
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        final String link =  jsonObject.getJSONObject("data").getString("link");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadCallback.onSuccess(link);
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadCallback.onFailed("Error message: " + response.message());
                            }
                        });

                    }

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadCallback.onFailed("Io Exception");
                        }
                    });

                    e.printStackTrace();
                }
            }
        }).start();

    }

    interface UploadCallback{
        void onSuccess(String downloadLink);
        void onFailed(String message);
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
