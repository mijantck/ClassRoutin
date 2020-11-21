package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView con,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        con = findViewById(R.id.contracme);
        email = findViewById(R.id.contracemail);

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                con.setVisibility(View.GONE);
                email.setVisibility(View.VISIBLE);

            }
        });
    }
}
