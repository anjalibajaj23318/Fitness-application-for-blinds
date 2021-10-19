package com.fitness.tracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DoctorsList extends AppCompatActivity {
    TextView txtDetails;
    Context context;
    SharedPreferences preferences;
    Adapter1 adapter1;
    ListView list;

    ArrayList<String> lstUsers = new ArrayList<>();
    ArrayList<String> lstUsersID = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    String TAG = "TAG";
    String special1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = findViewById(R.id.list);
        context = this;
        adapter1 = new Adapter1(context, R.layout.custum_list, lstUsers);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        special1 = getIntent().getStringExtra("special");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        list.setAdapter(adapter1);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);
        myRef.child(Constant.users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstUsers.clear();
                lstUsersID.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String special = (String) dataSnapshot1.child("special").getValue();
                    try {
                        if (special1.contains(special)) {

                            lstUsers.add(dataSnapshot1.child("name").getValue(String.class));

                            lstUsersID.add(dataSnapshot1.getKey());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    adapter1.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewProfile.class);
                intent.putExtra("userid", lstUsersID.get(position));
                startActivity(intent);
            }
        });
    }


}
