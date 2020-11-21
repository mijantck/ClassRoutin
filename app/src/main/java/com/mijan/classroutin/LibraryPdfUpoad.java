package com.mijan.classroutin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import com.mijan.classroutin.Note.PdfUpload;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LibraryPdfUpoad extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    StorageReference mStorageReference;
    StorageReference mStorageReferenceImage;
    DatabaseReference mDatabaseReference;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser current;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    final static int PICK_PDF_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 1;

    CollectionReference notebookRef = FirebaseFirestore.getInstance()
            .collection("Library PDF");

    private Uri mImageUri;
    private Uri pdf;
    private EditText NewsFeeed,fileName1;
    private TextView viewPdf,News_Added_Button;

    EditText PdfName;
    ImageView PdfImage;
    Button PdfCho,IMageCho;
    TextView Upload;
    String postcretorURL;
    String postcretorName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_pdf_upoad);

        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("PDF UPLOAD");

        mAuth = FirebaseAuth.getInstance();

        current = mAuth.getCurrentUser();
        String user_id = currentUser.getUid();
        postcretorURL = current.getPhotoUrl().toString();
        postcretorName = current.getDisplayName();


        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference().child(user_id).child("LIBRARY_PDF");
        mStorageReferenceImage = FirebaseStorage.getInstance().getReference().child(user_id).child("LIBRARY_IMAGE");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);


        PdfName = findViewById(R.id.Pdf_Name_Edite);
        PdfImage = findViewById(R.id.newsFeedAddedImage);
        PdfCho = findViewById(R.id.PDFChosserLibrary);
        IMageCho =findViewById(R.id.ImageChosserLibrary);
        Upload = findViewById(R.id.Library_Button);

        PdfCho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();

            }
        });
        IMageCho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIMEGE();
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdf == null || mImageUri == null ){
                    Toast.makeText(LibraryPdfUpoad.this, "FILE OR IMAGE IS EMPTY  ", Toast.LENGTH_SHORT).show();
                }
                else if (pdf != null && mImageUri != null) {

                    if (PdfName != null){

                        imagePdfUpload(mImageUri, pdf);
                    }
                    else {
                        Toast.makeText(LibraryPdfUpoad.this, "PDF NAME IS EMPTY ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });






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


   /* private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }*/

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
   /* private void getIMEGE() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                pdf = data.getData();

                Uri uri1 = data.getData();

                getFileName( uri1);
                // viewPdf.setText(pdf + "");
                //  viewPdf.setText((CharSequence) data.getData());
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(PdfImage);

                //  viewPdf.setText((CharSequence) data.getData());
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void imagePdfUpload(final Uri mImageUri, final Uri pdf) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String fileName = System.currentTimeMillis() + "";

        final StorageReference putImage = mStorageReferenceImage.child(fileName);
        final StorageReference putpdf = mStorageReference.child(fileName);


        putpdf.putFile(pdf).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                putImage.putFile(mImageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                putImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri ImageUri) {

                                        putpdf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri PdfUri) {


                                                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


                                                String PdfName1 = PdfName.getText().toString();
                                                String OthorName = current.getDisplayName();
                                                String user_id = currentUser.getUid();
                                                String date = currentDateandTime;
                                                String OthurId = currentUser.getUid();


                                                notebookRef.add(new PdfUpload(PdfName1, OthorName, date,OthurId, ImageUri.toString(), PdfUri.toString()));


                                                progressDialog.dismiss();
                                                Intent intent = new Intent(LibraryPdfUpoad.this,OnlineLibarary.class);
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

                    PdfName = (EditText) findViewById(R.id.Pdf_Name_Edite);
                    PdfName.setText(result);

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

                PdfName = (EditText) findViewById(R.id.Pdf_Name_Edite);
                PdfName.setText(result);
            }
        }
        return result;
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
}
