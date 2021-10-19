package com.fitness.tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class HeartRateResult extends AppCompatActivity {

    private String user, Date;
    int HR;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    java.util.Date today = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_result);

        Date = df.format(today);
        TextView RHR = (TextView) this.findViewById(R.id.HRR);
        ImageButton SHR = (ImageButton) this.findViewById(R.id.SendHR);
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HR = bundle.getInt("bpm");
            user = bundle.getString("Usr");
            Log.d("DEBUG_TAG", "ccccc" + user);
            RHR.setText(String.valueOf(HR));
            speakRead("Heart rate is: "+HR);
//            t1.speak("Heart rate is: "+HR, TextToSpeech.QUEUE_FLUSH, null);
        }

        SHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
                i.putExtra(Intent.EXTRA_TEXT, user + "'s Heart Rate " + "\n" + " at " + Date + " is :    " + HR);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HeartRateResult.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.user_profile);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }
   TextToSpeech t1;

    SharedPreferences preferences;
    DatabaseReference mDatabaseReference;

    public void saveDetails(View v) {
        mDatabaseReference.child(preferences.getString("userid", "")).child(Constant.heart_rate).setValue(HR);
        Toast.makeText(this, "Heart Rate Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HeartRateResult.this, MainActivity.class);
        i.putExtra("Usr", user);
        startActivity(i);
        finish();
    }
    public void speakRead(final String speak) {
        if (speak != null) {
            new CountDownTimer(1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    t1.speak(speak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }.start();


        }
    }
}
