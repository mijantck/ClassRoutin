package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {



    PhotoView imageviewALL;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageviewALL = findViewById(R.id.imageviewALL);

        Bundle bundle = getIntent().getExtras();
        imageURL = bundle.getString("URL");
        Picasso.get().load(imageURL).into(imageviewALL);




    }
}