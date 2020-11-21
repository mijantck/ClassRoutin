package com.mijan.classroutin.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.Note.PdfUpload;
import com.mijan.classroutin.PdfViewer;
import com.mijan.classroutin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PdfUploadAdapter extends RecyclerView.Adapter<PdfUploadAdapter.ViewHolder> {

    PdfUpload pdfUpload = new PdfUpload();
    private List<DocumentSnapshot> pdfList = new ArrayList<>();


    @NonNull
    @Override
    public PdfUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.libarary_single_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PdfUploadAdapter.ViewHolder viewHolder, final int i) {

        final PdfUpload pdfUpload = pdfList.get(i).toObject(PdfUpload.class);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        viewHolder.pdfName.setText(pdfUpload.getPdfName());
        viewHolder.pdfOthorName.setText(pdfUpload.getPdfOthorName());
        viewHolder.date.setText(pdfUpload.getDate());

        if (pdfUpload.getPdfOthorId().equals(uid)) {

            viewHolder.deletepdf.setVisibility(View.VISIBLE);
            viewHolder.deletepdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new android.app.AlertDialog.Builder(v.getContext())
                            .setTitle("Are You Sure?")
                            .setMessage(pdfUpload.getPdfName() + " Delete")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                          deleteData(i);
                                          notifyItemRemoved(i);
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
        } else {
            viewHolder.deletepdf.setVisibility(View.GONE);

        }

        if (pdfUpload.getImageUrl()!= null) {
            Picasso.get()
                    .load(pdfUpload.getImageUrl())
                    .placeholder(R.drawable.ic_loading)
                   // .fit()
                   // .centerCrop()
                    .into(viewHolder.pdfuploadImage);
        }

        if (pdfUpload.getPdfUrl() != null) {

            viewHolder.pdfUploadlib.setText("Download");

            viewHolder.pdfUploadlib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(" Download ");
                    builder.setIcon(R.drawable.ic_arrow_downward);
                    builder.setItems(new CharSequence[]
                                    {"Pdf View Online ", " Download "},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   switch (which) {
                                        case 0:

                                            String pdfUrl = pdfUpload.getPdfUrl();

                                            Intent pdfIntent = new Intent(v.getContext(), PdfViewer.class);
                                            pdfIntent.putExtra("PDFURL", pdfUrl);

                                            v.getContext().startActivity(pdfIntent);

                                            break;
                                        case 1:

                                            String pdfUrl1 =pdfUpload.getPdfUrl();
                                            dwnld(v.getContext(), pdfUrl1,pdfUpload.getPdfName());
                                            break;

                                    }
                                }
                            });
                    builder.create().show();

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pdfName,pdfOthorName,date;
        ImageView pdfuploadImage;
        Button pdfUploadlib,deletepdf;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfName = itemView.findViewById(R.id.pdfNameupload);
            pdfOthorName = itemView.findViewById(R.id.othore);
            date = itemView.findViewById(R.id.pdf_Date);
            deletepdf = itemView.findViewById(R.id.deletePdf);
            pdfuploadImage = itemView.findViewById(R.id.pdfIameg);
            pdfUploadlib = itemView.findViewById(R.id.libararyPdfButton);



        }
    }

    public void dwnld(Context context, String pdfUrl1,String name) {

        PdfUpload pdfUpload = new PdfUpload();

        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(pdfUrl1);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

// set title and description
        request.setTitle("Downloading...");
        request.setDescription(name+"Downloading");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,  name);
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    public void deleteData(int index) {


        FirebaseFirestore docRef = FirebaseFirestore.getInstance();
        docRef.collection("Library PDF")
                .document(pdfList.get(index).getId())
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


    public void addPdfList(List<DocumentSnapshot> pdfList1) {

        this.pdfList.addAll(pdfList1);
        notifyDataSetChanged();
    }
}
