package com.mijan.classroutin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mijan.classroutin.cours.GroupSingleView;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MCPMeeting extends AppCompatActivity {

    String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    String randomSerchCode;

    Button join,create;
    TextInputEditText MCPJOinCOde;
    TextView lastJointCode;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyJoinCode" ;
    String co;

    FirebaseAuth mAuth;
    FirebaseUser current;
    JitsiMeetUserInfo userInfo;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_c_p_meeting);



        join = findViewById(R.id.meetingJoin);
        create = findViewById(R.id.meetingCreate);
        MCPJOinCOde = findViewById(R.id.MCPJOinCOde);
        lastJointCode = findViewById(R.id.lastJointCode);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        co = sharedpreferences.getString("code","no code");

        lastJointCode.setText("Last Meeting code : "+co);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        userInfo = new JitsiMeetUserInfo();

        Toolbar toolbar = findViewById(R.id.toolbar_ID_meeting);
        toolbar.setTitle("M.C.P Meeting ");
        setSupportActionBar(toolbar);


        userInfo.setDisplayName(current.getDisplayName());
        userInfo.setEmail(current.getEmail());

        try {
            userInfo.setAvatar(new URL(current.getPhotoUrl().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        lastJointCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)MCPMeeting.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(co);
                Toast.makeText(MCPMeeting.this, co+" Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getcode = MCPJOinCOde.getText().toString().trim();
                if (getcode.isEmpty()){
                    Toast.makeText(MCPMeeting.this, "input code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!getcode.isEmpty()){
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(getcode)
                            .setUserInfo(userInfo)
                            .build();
                    JitsiMeetActivity.launch(MCPMeeting.this,options);
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomSerchCode =   generateRandom(candidateChars);
                co = randomSerchCode;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("code",randomSerchCode);
                editor.apply();
                lastJointCode.setText("Last Meeting code : "+randomSerchCode);
                if(!randomSerchCode.isEmpty()){
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(randomSerchCode)
                            .setUserInfo(userInfo)
                            .build();
                    JitsiMeetActivity.launch(MCPMeeting.this,options);
                }

            }
        });

    }
    private static String generateRandom(String aToZ) {
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8 ; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }
}