package com.fitness.tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUpProfile extends AppCompatActivity {
    EditText txtHeightFt, txtHeightIn, txtAge, txtWeight;
    RadioGroup rdgGender;
    Spinner spnActiveness, spnBodyFat;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        txtHeightFt = findViewById(R.id.txtHeightFt);
        txtHeightIn = findViewById(R.id.txtHeightIn);
        txtAge = findViewById(R.id.txtAge);
        txtWeight = findViewById(R.id.txtWeight);
        rdgGender = findViewById(R.id.rdgGender);

        spnActiveness = findViewById(R.id.spnActiveness);
        spnBodyFat = findViewById(R.id.spnBodyFat);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("profile_update", false)) {
            txtHeightFt.setText(sharedPreferences.getString("height_ft", null));
            txtHeightIn.setText(sharedPreferences.getString("height_in", null));
            txtAge.setText(sharedPreferences.getString("age", null));
            String gender = sharedPreferences.getString("gender", null);
            txtWeight.setText(sharedPreferences.getString("weight", null));
            if (gender.equals("Male")) {
                rdgGender.check(R.id.rdMale);
            } else {
                rdgGender.check(R.id.rdFemale);
            }


            String activeness = sharedPreferences.getString("activeness", null);
            String[] strActiveness = getResources().getStringArray(R.array.spnActiveness);
            List<String> stringList = new ArrayList<String>(Arrays.asList(strActiveness));
            spnActiveness.setSelection(stringList.indexOf(activeness));

            String body_fat = sharedPreferences.getString("body_fat", null);
            String[] strbody_fat = getResources().getStringArray(R.array.spnBodyFat);
            List<String> stringListbody_fat = new ArrayList<String>(Arrays.asList(strbody_fat));
            spnBodyFat.setSelection(stringListbody_fat.indexOf(body_fat));


        }
    }


    public void viewResults(View v) {
        Intent intent1 = new Intent(getBaseContext(), BMIResults.class);

        startActivity(intent1);
    }

    public void createProfile(View v) {
        String strHeightIn = txtHeightIn.getText().toString();
        String strHightFt = txtHeightFt.getText().toString();
        String strAge = txtAge.getText().toString();
        String strWeight = txtWeight.getText().toString();
        String strGender = "Male";
        if (rdgGender.getCheckedRadioButtonId() == R.id.rdFemale) {
            strGender = "Female";
        }


        String strActiveness = spnActiveness.getSelectedItem().toString();
        String strBodyFat = spnBodyFat.getSelectedItem().toString();

        if (strHeightIn.length() > 0) {
            if (strHightFt.length() > 0) {
                if (strAge.length() > 0) {
                    if (strWeight.length() > 0) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(Constant.DB);

                        UserProfile userProfile = new UserProfile();

                        userProfile.setUserid(sharedPreferences.getString("userid", ""));
                        userProfile.setActiveness(strActiveness);
                        userProfile.setAge(strAge);
                        userProfile.setBmi(0);
                        userProfile.setBody_fat(strBodyFat);
                        userProfile.setCalorie(0);
                        userProfile.setGender(strGender);
                        userProfile.setHeart_rate(0);
                        userProfile.setHeight_Ft(strHightFt);
                        userProfile.setHeight_In(strHeightIn);
                        userProfile.setSteps(0);
                        userProfile.setWeight(strWeight);
                       // userProfile.set(strWeight);
                        myRef.child(Constant.user_profile).child(sharedPreferences.getString("userid", "")).setValue(userProfile);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("height_in", strHeightIn)
                                .putString("height_ft", strHightFt)
                                .putString("age", strAge)
                                .putString("gender", strGender)
                                .putString("weight", strWeight)
                                .putString("activeness", strActiveness)
                                .putString("body_fat", strBodyFat)
                                .putBoolean("profile_update", true)

                                .apply();

                        Toast.makeText(this, "Profile Creation successfull", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Please check weight", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please check age", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please check height", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please check height", Toast.LENGTH_SHORT).show();
        }


    }
}
