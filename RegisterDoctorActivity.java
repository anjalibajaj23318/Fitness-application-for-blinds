package com.fitness.tracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterDoctorActivity extends AppCompatActivity {
    private DatabaseReference mdb;


    private EditText et1, et2, et3, et4, et5, et6, et7, et8, et10;
    private Button btn;
    private Spinner sp1, sp2, sp3;
    private String blod, Gen, st;
    TextInputEditText et9, etConfirmPassword;

    //ArrayList blood=new ArrayList();
    String[] blood = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    String[] Gender = {"Male", "Female", "Other"};
    String[] State = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu & Kashmir ", "Jharkhand", "Karnataka"
            , "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Odisha", "Punjab ", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal"};
    Context context;
    TextView txtMedicalHis;
    ImageView imgDisplay;
    EditText txtSpecial;
String usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        context = this;
        btn = (Button) findViewById(R.id.Button);
        et1 = (EditText) findViewById(R.id.EditTextName);
        et2 = (EditText) findViewById(R.id.EditTextDob);
        //  et3 = (EditText) findViewById(R.id.EditTextGender);
        et4 = (EditText) findViewById(R.id.EditTextCity);
        et5 = (EditText) findViewById(R.id.EditTextPincode);
        et7 = (EditText) findViewById(R.id.EditTextNumber);
        et8 = (EditText) findViewById(R.id.EditTextEmail);
        et9 = findViewById(R.id.etPassword);
        txtSpecial = findViewById(R.id.txtSpecial);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        txtMedicalHis = findViewById(R.id.txtMedicalHis);
        imgDisplay = findViewById(R.id.imgDisplay);
        usertype=getIntent().getStringExtra("usertype");
        //et10= (EditText) findViewById(R.id.EditTextState);
        // auth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference(Constant.DB);
        storageReference = FirebaseStorage.getInstance().getReference(Constant.DB);

        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2 = (Spinner) findViewById(R.id.spinner2);
        sp3 = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blood);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blod = blood[position];
                // Toast.makeText(RegisterActivity.this, ""+blod, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Gender);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Gen = Gender[position];
                // Toast.makeText(RegisterActivity.this, "You selected"+Gen, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, State);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(adapter3);

        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st = State[position];
                Toast.makeText(RegisterDoctorActivity.this, "" + st, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                                   @Override
                                                                                   public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                                                                                       // Do whatever you want when the date is selected.
                                                                                       String dd = Integer.toString(i2);
                                                                                       if (Integer.toString(i2).length() < 2) {
                                                                                           dd = "0" + i2;
                                                                                       }
                                                                                       String mm = Integer.toString(i1);
                                                                                       if (Integer.toString(i1).length() < 2) {
                                                                                           mm = "0" + i1;
                                                                                       }
                                                                                       et2.setText(dd + "/" + mm + "/" + i);

                                                                                   }
                                                                               },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        //datePickerDialog.setYearRange(1900, 2009); // You can add your value for YEARS_IN_THE_FUTURE.
        Calendar calendar1 = Calendar.getInstance();

        calendar1.add(Calendar.YEAR, -18);
        datePickerDialog.setMaxDate(calendar1);
        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "hello");
            }
        });


    }

    Map<String, Object> mfb = new HashMap<>();
    StorageReference storageReference;
String special;
ProgressDialog dialog;
    public void submit(View v) {
        final String name = et1.getText().toString();
        String dob = et2.getText().toString();
        //String gender = et3.getText().toString();
        String city = et4.getText().toString();
        String pincode = et5.getText().toString();
        //String state=et10.getText().toString();
        //String bloodgrp = et6.getText().toString();
        String number = et7.getText().toString();
        final String email = et8.getText().toString();
        final String password = et9.getText().toString();
        special = txtSpecial.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(name)) {
            et1.setError("please enter name");
        } else if (TextUtils.isEmpty(dob) || et2.getText().length() != 10) {
            et2.setError("please enter valid DOB");
        }

        //else if (TextUtils.isEmpty(gender) ) {
        //  et3.setError("please enter valid gender");
        //}


        else if (TextUtils.isEmpty(city)) {
            et4.setError("please enter city");
        } else if (TextUtils.isEmpty(pincode) || et5.getText().length() != 6) {
            et5.setError("please enter valid pincode");
        }
        //else if(TextUtils.isEmpty(state))
        //{
        //  et10.setError("please enter valid state");
        //}
        //else if (TextUtils.isEmpty(bloodgrp) ) {
        // et6.setError("please enter country");
        //}
        else if (TextUtils.isEmpty(number) || et7.getText().length() != 10) {
            et7.setError("please enter valid number");
        } else if (!email.matches(emailPattern)) {
            et8.setError("please enter valid email");
        } else if (TextUtils.isEmpty(password) || et9.getText().length() <= 5) {
            et9.setError("please enter minimum 5 length");
        } else if (!et9.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("Password & Confirm password do not match");
        } else {

            /*SQLiteDatabase db = openOrCreateDatabase("Registraition", MODE_APPEND, null);
            String q = "insert into Registraition_Table1(name,dob,gender,city,pincode,state,bloodgrp,number,email,password)
            values ('" + name + "','" +dob + "','" + Gen + "','" + city + "','" + pincode + "','" + st + "','" + blod + "',
            '" + number + "','" + email + "','" + password + "')";
            db.execSQL(q);
            Toast.makeText(this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
            db.close();*/

dialog=ProgressDialog.show(RegisterDoctorActivity.this,"Please wait...","Saving Details..",true,true);
            mfb.put("name", name);
            mfb.put("dob", dob);
            mfb.put("gender", Gen);
            mfb.put("city", city);
            mfb.put("pincode", pincode);
            mfb.put("state", st);
            mfb.put("blood_group", blod);
            mfb.put("phone", number);
            mfb.put("email", email);
            mfb.put("password", password);
            mfb.put("special", special);
            mfb.put("utype", usertype);
                                  if (uri_image != null) {
                                    storageReference.child(System.currentTimeMillis() + "").putFile(uri_image).addOnCompleteListener(RegisterDoctorActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isComplete()) {

                                                mfb.put("url", task.getResult().getDownloadUrl().toString());

                                                if (uri != null) {
                                                    storageReference.child(System.currentTimeMillis() + "").putFile(uri).addOnCompleteListener(RegisterDoctorActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            if (task.isComplete()) {
                                                                mfb.put("additional_file", task.getResult().getDownloadUrl().toString());

                                                                mdb.child(Constant.users).child(System.currentTimeMillis()+"_").setValue(mfb).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(RegisterDoctorActivity.this, "Succesfully Registered", Toast.LENGTH_SHORT).show();

//                                                                            et1.setText("");
//                                                                            et2.setText("");
//                                                                            et4.setText("");
//                                                                            et5.setText("");
//                                                                            et7.setText("");
//                                                                            et8.setText("");
//                                                                            et9.setText("");

                                                                            Toast.makeText(RegisterDoctorActivity.this, "Authentication Successful.",
                                                                                    Toast.LENGTH_SHORT).show();

                                                                            finish();

                                                                        } else {
                                                                            Toast.makeText(RegisterDoctorActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        Log.i("DB", "onComplete: " + task.getException());
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    mfb.put("additional_file", null);
                                                    mdb.child(Constant.users).child(System.currentTimeMillis()+"_").setValue(mfb).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            dialog.dismiss();
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(RegisterDoctorActivity.this, "Succesfully Registered", Toast.LENGTH_SHORT).show();

                                                                et1.setText("");
                                                                et2.setText("");
                                                                et4.setText("");
                                                                et5.setText("");
                                                                et7.setText("");
                                                                et8.setText("");
                                                                et9.setText("");
                                                                txtSpecial.setText("");


                                                                Toast.makeText(RegisterDoctorActivity.this, "Authentication Successful.",
                                                                        Toast.LENGTH_SHORT).show();
                                                                finish();

                                                            } else {
                                                                Toast.makeText(RegisterDoctorActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                            Log.i("DB", "onComplete: " + task.getException());
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "Please select profile image", Toast.LENGTH_SHORT).show();
                                }





        }
    }

    Uri uri = null;
    Uri uri_image = null;

    public void selectMedicalHistory(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Medical History Image"), 2);

    }

    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    String path = null;
    String path_image = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    path = myFile.getAbsolutePath();


                    Log.i("MA", "onActivityResult: " + path);


                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = context.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    txtMedicalHis.setText(displayName);
                    /*   path= getRealPathFromURI(uri);
                        Log.i("MA", "onActivityResult: "+path);*/

                    try {
                        path = PathUtils.getPath(context, uri);
                        Log.i("MA", "onActivityResult: " + path);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    uri_image = data.getData();
                    String uriString = uri_image.toString();
                    File myFile = new File(uriString);
                    path_image = myFile.getAbsolutePath();


                    Log.i("MA", "onActivityResult: " + path_image);


                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = context.getContentResolver().query(uri_image, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    Picasso.with(context).load(uri_image).into(imgDisplay);


                    try {
                        path = PathUtils.getPath(context, uri_image);
                        Log.i("MA", "onActivityResult: " + path);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


