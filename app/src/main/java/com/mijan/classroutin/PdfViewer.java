package com.mijan.classroutin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

public class PdfViewer extends AppCompatActivity {

    private WebView webView;
    private PDFView pdfView;
    private ProgressBar progressBar;

    String PDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);


        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        setTitle("PDF Viewer");

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progresbar);


        Bundle bundle = getIntent().getExtras();

        PDF = bundle.getString("PDFURL");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        String PDFUrl = "/new/1569251944665.pdf";
        String myurl = "gs://newtest-3e4ca.appspot.com"+PDFUrl;
        String fullPath = PDF;



        String url = "https://firebasestorage.googleapis.com/v0/b/newtest-3e4ca.appspot.com/o/new%2F1569239978928?alt=media&token=07f930ba-e9fa-4ec4-865f-d33a983d9fa1";


        progressBar.setVisibility(View.VISIBLE);

      //  storage.getReferenceFromUrl(url)
        storage.getReferenceFromUrl(fullPath)
                .getBytes(900000000)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        progressBar.setVisibility(View.GONE);
                        pdfView.fromBytes(bytes)

                                .enableSwipe(true) // allows to block changing pages using swipe
                                .swipeHorizontal(false)
                                .enableDoubletap(true)
                                .defaultPage(0)
                                .load();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PdfViewer.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }
}




