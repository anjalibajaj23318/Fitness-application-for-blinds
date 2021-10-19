package com.fitness.tracker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference myRef;
    ArrayList<Double> lstLatitude = new ArrayList<>();
    ArrayList<Double> lstLongitude = new ArrayList<>();
    ArrayList<String> lstName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myRef = FirebaseDatabase.getInstance().getReference(Constant.DB);
        myRef.child(Constant.users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstLatitude.clear();
                lstLongitude.clear();
                lstName.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    lstLatitude.add(dataSnapshot1.child("latitude").getValue(Double.class));
                    lstLongitude.add(dataSnapshot1.child("longitude").getValue(Double.class));
                    lstName.add(dataSnapshot1.child("name").getValue(String.class));
                }
                int count = 0;
                while (count < lstLatitude.size()) {
                    LatLng sydney = new LatLng(lstLatitude.get(count), lstLongitude.get(count));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(lstName.get(count)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    count++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int count = 0;
        while (count < lstLatitude.size()) {
            LatLng sydney = new LatLng(lstLatitude.get(count), lstLongitude.get(count));
            mMap.addMarker(new MarkerOptions().position(sydney).title(lstName.get(count)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            count++;
        }
        // Add a marker in Sydney and move the camera
    }
}
