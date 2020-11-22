package com.mijan.classroutin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.AsingmentSubmitNote;
import com.mijan.classroutin.Note.TheoryExamNote;
import com.mijan.classroutin.onlineexam.Onlne_create_exam_type;
import com.mijan.classroutin.onlineexam.TheoryExamCreat;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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

public class AsingmetCreatSubmit extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    ProgressDialog progressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Collection;
    TextView asingment_start_date,asingment_end_time,asingment_submit_Add;
    TextInputEditText asingment_Name,asingment_PageNumber,Student_rol_number;
    Button asingment_ImageChosserQuastion;
    PhotoView asingment_ImageChosserQuastionView;
    TextInputLayout rolnumberInput,crete_assingment_input;


    private static final int PICK_IMAGE_REQUEST = 1;
    private  int IMAGE_CAPTURE_CODE = 999;
    public Uri mImageUri ;

    private int mYear, mMonth, mDay, mHour, mMinute;
    int setyear,setmont,setday;
    boolean select_date = false;
    boolean select_end_time = false;
    long startmili;
    long endmili;
    String asingment_start_dateString;

    String courseId, coursecode, courseName,RandomSerchCodeS,CoursCretorID,CoursThecher,
            submiteAns,currentAssingmentid,update;
    String upName,upDate,upEndTime,upPage,upID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asingmet_creat_submit);

        Toolbar toolbar = findViewById(R.id.toolbar_IDAAssingmentCret);
        toolbar.setTitle(" Assignment Create ");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(AsingmetCreatSubmit.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        Bundle bundle = getIntent().getExtras();
        courseId = bundle.getString("courseId");
        coursecode = bundle.getString("coursecourse");
        courseName = bundle.getString("courseName");
        RandomSerchCodeS = bundle.getString("getRandomSerchCode");
        CoursCretorID = bundle.getString("CoursCretorID");
        CoursThecher = bundle.getString("CoursThecher");
        submiteAns = bundle.getString("submiteAns");
        currentAssingmentid = bundle.getString("currentAssingmentid");
        update = bundle.getString("update");
        upName = bundle.getString("upName");
        upDate = bundle.getString("upDate");
        upEndTime = bundle.getString("upEndTime");
        upPage = bundle.getString("upPage");
        currentAssingmentid = bundle.getString("upID");



        asingment_start_date = findViewById(R.id.asingment_start_date);
        asingment_end_time = findViewById(R.id.asingment_end_time);
        asingment_submit_Add = findViewById(R.id.asingment_submit_Add);
        asingment_Name = findViewById(R.id.asingment_Name);
        asingment_PageNumber = findViewById(R.id.asingment_PageNumber);
        asingment_ImageChosserQuastion = findViewById(R.id.asingment_ImageChosserQuastion);
        asingment_ImageChosserQuastionView = findViewById(R.id.asingment_ImageChosserQuastionView);
        Student_rol_number = findViewById(R.id.Student_rol_number);
        rolnumberInput = findViewById(R.id.rolnumberInput);
        crete_assingment_input = findViewById(R.id.crete_assingment_input);

        if (update != null){
            asingment_Name.setText(upName);
            asingment_PageNumber.setText(upPage);
            endmili = Long.parseLong(upEndTime);
            asingment_start_date.setText(upDate);
            DateFormat startTimesimple = new SimpleDateFormat("HH:mm:ss");
            String Timesample = startTimesimple.format(endmili);
            asingment_end_time.setText(Timesample);
        }

        if (submiteAns != null){
            rolnumberInput.setVisibility(View.VISIBLE);
            crete_assingment_input.setVisibility(View.GONE);
            asingment_start_date.setVisibility(View.GONE);
            asingment_end_time.setVisibility(View.GONE);
        }


        asingment_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AsingmetCreatSubmit.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                setyear = year;
                                setmont = monthOfYear;
                                setday = dayOfMonth;
                                asingment_start_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                asingment_start_dateString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                select_date = true;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        asingment_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_date == true) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AsingmetCreatSubmit.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    // startatime.setText(hourOfDay + ":" + minute);
                                    c.set(setyear, setmont, setday, hourOfDay, minute);
                                    long millis = c.getTimeInMillis();
                                    endmili = millis;
                                    Date date = new Date(millis);
                                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                                    String formatted = df.format(date);
                                    asingment_end_time.setText(formatted);
                                    select_end_time = true;
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }else {
                    Toast.makeText(AsingmetCreatSubmit.this, "select start time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        asingment_submit_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();


                if (upDate.isEmpty()){
                    Toast.makeText(AsingmetCreatSubmit.this, "Select Date ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (upDate != null){
                    if (mImageUri == null){
                        Toast.makeText(AsingmetCreatSubmit.this, "udate", Toast.LENGTH_LONG).show();
                        String asName = asingment_Name.getText().toString();
                        String subPNaber = asingment_PageNumber.getText().toString();
                        int assPNamberInt = Integer.parseInt(subPNaber);

                        DocumentReference update = db.collection("Global Group").document(courseId)
                                .collection("Assingnment").document(currentAssingmentid);
                        update.update("name",asName,"page",assPNamberInt,"date",asingment_start_date.getText().toString(),"endTime",endmili)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        finish();

                                        Toast.makeText(AsingmetCreatSubmit.this, "Update completed ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        uploadImageUri(mImageUri);
                    }
                }
                else if (submiteAns != null){
                    String subPNaber = asingment_PageNumber.getText().toString();
                    String rolPNaber = Student_rol_number.getText().toString();
                    if (rolPNaber.isEmpty() || subPNaber.isEmpty() || mImageUri == null ){
                        //   Toast.makeText(AsingmetCreatSubmit.this, "please all input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressDialog.show();

                    uploadImageUri(mImageUri);
                }else {
                    uploadImageUri(mImageUri);
                }




            }
        });

        asingment_ImageChosserQuastion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIMEGE();

            }
        });


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
            asingment_ImageChosserQuastionView.setImageURI(mImageUri);

        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(asingment_ImageChosserQuastionView);


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

                    String asName = asingment_Name.getText().toString();
                    String subPNaber = asingment_PageNumber.getText().toString();
                    String subRol = Student_rol_number.getText().toString();
                    int assPNamberInt = Integer.parseInt(subPNaber);
                    int assRollInt;
                    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String uName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                    if (submiteAns != null) {
                        assRollInt = Integer.parseInt(subRol);
                        Collection =  db.collection("Global Group").document(courseId)
                                .collection("Assingnment").document(currentAssingmentid).collection("ans");
                        Collection.add(new AsingmentSubmitNote(null,uID,uName,assRollInt,assPNamberInt,downloadLink))
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            Collection.document(task.getResult().getId()).update("id",task.getResult().getId())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();
                                                            finish();

                                                            Toast.makeText(AsingmetCreatSubmit.this, "Page number : "+assPNamberInt+"\n"+"complete", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }else{
                        if (update != null){
                            DocumentReference update = db.collection("Global Group").document(courseId)
                                    .collection("Assingnment") .document(currentAssingmentid);
                            update.update("name",asName,"page",assPNamberInt,"imageURL",downloadLink,"date",asingment_start_date.getText().toString(),"endTime",endmili)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            finish();
                                            Toast.makeText(AsingmetCreatSubmit.this, "Update completed ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            Collection =  db.collection("Global Group").document(courseId).collection("Assingnment");
                            Collection.add(new AsingmentSubmitNote(null,uID,asName,assPNamberInt,downloadLink,asingment_start_dateString,endmili))
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
                                                                finish();
                                                                Toast.makeText(AsingmetCreatSubmit.this, " Assignment added", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }

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
