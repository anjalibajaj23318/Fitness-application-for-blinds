package com.fitness.tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class BMIResults extends AppCompatActivity {
    TextView txtDetails;
    double latitude, longitude;
    double total_calorie = 0;
    double total_fat, total_protein, total_carbo;
    double total_bmi = 0;
    SharedPreferences prefrences;
Context context;
DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiresults);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtDetails = findViewById(R.id.txtDetails);
        context=this;
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);
        mDatabaseReference=FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.user_profile);
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{

        }catch (Exception e){

        }
    }

    TextToSpeech t1;
    String TAG = "BMI";

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefrences.getBoolean("profile_update", false)) {
            Intent intent = new Intent(getBaseContext(), SetUpProfile.class);
            startActivity(intent);
        } else {
            String height_ft = prefrences.getString("height_ft", "0.0");
            String height_in = prefrences.getString("height_in", "0.0");
            String age = prefrences.getString("age", "0.0");
            String gender = prefrences.getString("gender", "Male");
            String activeness = prefrences.getString("activeness", null);
            String weight = prefrences.getString("weight", "0.0");
            double height = Double.parseDouble(height_ft) * 30.48 + Double.parseDouble(height_in) * 2.54;

            CalorieCalculator calorieCalculator = new CalorieCalculator();


            if (gender.equals("Male")) {
                try {
                    double active = calorieCalculator.exerciseLevelCalcMale(activeness);

                    total_calorie = calorieCalculator.calorieCalculatorMale(active, Double.parseDouble(weight), height, Double.parseDouble(age));
                   Log.i(TAG, "onResume: active=" + active + "%%" + height + "::" + total_calorie);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                total_fat = calorieCalculator.fatCalculator(total_calorie);
                total_protein = calorieCalculator.protienCalculator(Double.parseDouble(weight));
                total_carbo = calorieCalculator.carboHydrateCalculator(total_calorie, total_protein, total_fat);
                total_bmi = calorieCalculator.calculateBMI(Double.parseDouble(weight), Double.parseDouble(height_ft), Double.parseDouble(height_in));

                txtDetails.setText("Total Calories: " + total_calorie +
                        "\nYou Must Intake the following daily\nFat: " +
                        total_fat + "Per Day\nProtein: " +
                        total_protein + " Per Day\nCarbohydrate: " +
                        total_carbo + " Per Day"+"\nBMI: "+total_bmi);
                mDatabaseReference.child(prefrences.getString("userid", "")).child(Constant.calorie).setValue(total_calorie);
                mDatabaseReference.child(prefrences.getString("userid", "")).child(Constant.bmi).setValue(total_bmi);
                speakRead("Total Calories: " +
                        total_calorie + "\nYou Must Intake the following daily\nFat: " +
                        total_fat + "Per Day\nProtein: " +
                        total_protein + " Per Day\nCarbohydrate: "
                        + total_carbo + " Per Day"+"\nBMI: "+total_bmi);
            } else {
                if (gender.equals("Female")) {
                    try {
                        double active = calorieCalculator.exerciseLevelCalcFeMale(activeness);
                        total_calorie = calorieCalculator.calorieCalculatorFeMale(active, Double.parseDouble(weight), height, Double.parseDouble(age));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    total_fat = calorieCalculator.fatCalculator(total_calorie);
                    total_protein = calorieCalculator.protienCalculator(Double.parseDouble(weight));
                    total_carbo = calorieCalculator.carboHydrateCalculator(total_calorie, total_protein, total_fat);
                    total_bmi = calorieCalculator.calculateBMI(Double.parseDouble(weight), Double.parseDouble(height_ft), Double.parseDouble(height_in));


                    txtDetails.setText("Total Calories: " +
                            total_calorie + "\nYou Must Intake the following daily\nFat: " +
                            total_fat + "Per Day\nProtein: " +
                            total_protein + " Per Day\nCarbohydrate: "
                            + total_carbo + " Per Day"+"\nBMI: "+total_bmi);
                    mDatabaseReference.child(prefrences.getString("userid", "")).child(Constant.calorie).setValue(total_calorie);
                    mDatabaseReference.child(prefrences.getString("userid", "")).child(Constant.bmi).setValue(total_bmi);
speakRead("Total Calories: " +
        total_calorie + "\nYou Must Intake the following daily\nFat: " +
        total_fat + "Per Day\nProtein: " +
        total_protein + " Per Day\nCarbohydrate: "
        + total_carbo + " Per Day"+"\nBMI: "+total_bmi);
                }
            }
        }
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
