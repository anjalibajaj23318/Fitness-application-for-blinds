package com.fitness.tracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    ArrayList<String> lstUsers = new ArrayList<>();
    ArrayList<String> lstUsersID = new ArrayList<>();
    FirebaseAuth auth;
    String TAG = "ADMIN";

    EditText txtSearch;
    Context context;
    StorageReference mStorageReference;
    ListView listView;
    String data = "";
    Adapter1 adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtSearch = findViewById(R.id.txtSearch);
        listView = findViewById(R.id.list);
        adapter1 = new Adapter1(this, R.layout.custum_list, lstUsers);
        listView.setAdapter(adapter1);
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Symptom", Snackbar.LENGTH_LONG)
                        .setAction("Click Here", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(AdminPanel.this,RegisterDoctorActivity.class);
                                intent.putExtra("usertype","doctor");
                                startActivity(intent);
                            }
                        }).show();
            }
        });
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);
        mStorageReference = FirebaseStorage.getInstance().getReference();


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, lstUsers);
//        spnUsers.setAdapter(adapter);

listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                      Intent intent=new Intent(getBaseContext(),ViewProfile.class);
                      intent.putExtra("userid",lstUsersID.get(position));
                      startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("What do you want?").setPositiveButton("View Profile", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }
});
        getUserList(data);
    }

    public void search(View v) {
        data = txtSearch.getText().toString();
        if (data.length() > 0) {
            getUserList(data);

        } else {
            Toast.makeText(context, "Please check input", Toast.LENGTH_SHORT).show();
        }
    }
public void newEmployee(View v) {


    }

    public void getUserList(final String data) {
        myRef.child(Constant.users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstUsers.clear();
                lstUsersID.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String name = (String) dataSnapshot1.child("name").getValue();
                    if (data.length() > 0) {
                        if (name.contains(data)) {

                            lstUsers.add(name);
                            lstUsersID.add(dataSnapshot1.getKey());
              }
                    } else {
                        lstUsers.add(name);
                        lstUsersID.add(dataSnapshot1.getKey());
                    }

                    adapter1.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    FirebaseDatabase database;
    DatabaseReference myRef;


}
