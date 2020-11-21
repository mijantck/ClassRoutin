package com.mijan.classroutin.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mijan.classroutin.ImageViewActivity;
import com.mijan.classroutin.Note.NewsAddedNote;
import com.mijan.classroutin.PdfViewer;
import com.mijan.classroutin.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    private List<DocumentSnapshot> newsfeed = new ArrayList<>();



    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_feed_singrl_item, viewGroup, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder newsViewHolder, final int i) {

        final NewsAddedNote newsAddedNote = newsfeed.get(i).toObject(NewsAddedNote.class);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (newsAddedNote.getPostOwnerName() != null){
            newsViewHolder.courseCode.setText(newsAddedNote.getPostOwnerName());
        }
        newsViewHolder.courseName.setText(newsAddedNote.getCourse_Name());
        newsViewHolder.date.setText(newsAddedNote.getDate());
        newsViewHolder.time.setText(getTimee(newsAddedNote.getTime()));
        if (newsAddedNote.getPostOwnerURL() != null) {
            Picasso.get().load(newsAddedNote.getPostOwnerURL()).into(newsViewHolder.profile_image_post_cretore);
        }
        if (newsAddedNote.getPostOwner().equals(uid)) {

            newsViewHolder.deletePost.setVisibility(View.VISIBLE);
            newsViewHolder.deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new android.app.AlertDialog.Builder(v.getContext())
                            .setMessage("Are you sure?")
                            .setMessage(newsAddedNote.getCourse_Name() + "Post Delete")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    deleteData(i);
                                    notifyItemRemoved(i);
                                   // notifyItemChanged(i);
                                    //notifyItemRangeChanged(i,getItemCount());

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
            newsViewHolder.deletePost.setVisibility(View.GONE);

        }


        if (newsAddedNote.getNews_Feed() != null) {
            newsViewHolder.newsFeedView.setVisibility(View.VISIBLE);
            newsViewHolder.newsFeedView.setText(newsAddedNote.getNews_Feed());

        } else {
            newsViewHolder.newsFeedView.setVisibility(View.GONE);

        }

        if (newsAddedNote.getmImageUrl() != null) {


            newsViewHolder.newsFeedImage.setVisibility(View.VISIBLE);
          //  newsViewHolder.newsFeedImage.setPhotoUri(Uri.parse(newsAddedNote.getmImageUrl()));
            Picasso.get()
                    .load(newsAddedNote.getmImageUrl())
                    .placeholder(R.drawable.ic_loading)
                    //  .fit()
                   // .centerCrop()
                    .into(newsViewHolder.newsFeedImage);

            newsViewHolder.newsFeedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ImageViewActivity.class);
                    intent.putExtra("URL",newsAddedNote.getmImageUrl());

                    v.getContext().startActivity(intent);
                  /*  AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    ViewGroup viewGroup = v.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.zoom_in, viewGroup, false);
                    builder.setView(dialogView);
                    final PhotoView photoView = dialogView.findViewById(R.id.photo_view);

                    Picasso.get()
                            .load(newsAddedNote.getmImageUrl())
                            .placeholder(R.drawable.ic_loading)
                            //  .fit()
                            // .centerCrop()
                            .into(photoView);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();*/
                }
            });

        } else {

            newsViewHolder.newsFeedImage.setVisibility(View.GONE);
        }

        if (newsAddedNote.getPdfUrl() != null) {

            newsViewHolder.newsFeedPdfButton.setVisibility(View.VISIBLE);
            newsViewHolder.newsFeedPdfButton.setText(" "+newsAddedNote.getPdfName());
            newsViewHolder.newsFeedPdfButton.setOnClickListener(new View.OnClickListener() {
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

                                            String pdfUrl = newsAddedNote.getPdfUrl();

                                            Intent pdfIntent = new Intent(v.getContext(), PdfViewer.class);
                                            pdfIntent.putExtra("PDFURL", pdfUrl);
                                            v.getContext().startActivity(pdfIntent);
                                            break;
                                        case 1:

                                            String pdfUrl1 = newsAddedNote.getPdfUrl();
                                            dwnld(v.getContext(), pdfUrl1,newsAddedNote.getPdfName());
                                            break;

                                    }
                                }
                            });
                    builder.create().show();

                }
            });
        } else {
            newsViewHolder.newsFeedPdfButton.setVisibility(View.GONE);
        }


        newsViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharebleText = newsAddedNote.getNews_Feed() + "\n \n"
                        +"Course Name\n"
                        + newsAddedNote.getCourse_Name()
                        +"\n Pdf Download Url \n"
                        +newsAddedNote.getPdfUrl()
                        +" \n Image Download Url \n"
                        +newsAddedNote.getmImageUrl();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("Text/Plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, sharebleText);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share On "));
            }
        });


    }



    @Override
    public int getItemCount() {

        return newsfeed.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView courseCode;
        TextView courseName,newsFeedPdfButton;
        TextView date;
        TextView time;
        Button  deletePost;
        ImageView newsFeedImage;
        TextView newsFeedView;
        ImageButton share;
        CardView cardView;
        CircleImageView profile_image_post_cretore;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.news_feed_card);
            courseCode = itemView.findViewById(R.id.news_course_core);
            deletePost = itemView.findViewById(R.id.deletePost);
            courseName = itemView.findViewById(R.id.news_course_name);
            date = itemView.findViewById(R.id.news_course_Date);
            time = itemView.findViewById(R.id.news_course_Time);
            newsFeedView = itemView.findViewById(R.id.news_feed_textView);
            share = itemView.findViewById(R.id.news_feed_Share_button);
            newsFeedImage = itemView.findViewById(R.id.newsFeedImage);
            newsFeedPdfButton = itemView.findViewById(R.id.newsFeedPdfButton);
            profile_image_post_cretore = itemView.findViewById(R.id.profile_image_post_cretore);


        }
    }

    private String getTimee(long timestamp) {
        long ts = timestamp * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time = sdf.format(new Date(ts));
        return time;
    }

    public void addPosts(List<DocumentSnapshot> posts) {

        this.newsfeed.addAll(posts);
        notifyDataSetChanged();


    }

    public void dwnld(Context context, String pdfUrl1,String name) {

        NewsAddedNote newsAddedNote = new NewsAddedNote();

        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(pdfUrl1);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

// set title and description
        request.setTitle(name);
        request.setDescription(name +" Downloading");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    public void deleteData(int index) {


        FirebaseFirestore docRef = FirebaseFirestore.getInstance();
        docRef.collection("Post")
                .document(newsfeed.get(index).getId())
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
