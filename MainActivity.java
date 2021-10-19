package com.fitness.tracker;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fitness.tracker.music.MusicPlayerActivity;
import com.fitness.tracker.steps.SimplePedometerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    TextView txtLoc;

    SharedPreferences prefrences;


    LocationManager locationManager;

    NavigationView navigationView;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        context = this;
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);

        txtLoc = findViewById(R.id.txtLoc);

//
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);

        if (!prefrences.getBoolean("login", false)) {
            prefrences.edit().putString("name", getIntent().getStringExtra("name"))
                    .putString("userid", getIntent().getStringExtra("userid"))
                    .putString("email", getIntent().getStringExtra("email"))
                    .putString("utype", getIntent().getStringExtra("utype"))
                    .putBoolean("login", true)
                    .putString("image", getIntent().getStringExtra("image")).commit();

        }
        userid = prefrences.getString("userid", "");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED
                ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0,
                        0, this);
            } else if (locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0,
                        0, this);
            } else {
                Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        speakRead("Your options are Music, Heart Rate, Pedometer and Music Player");

    }
    TextToSpeech t1;
    @Override
    protected void onPause() {
        super.onPause();
        t1.stop();

    }

    public void speakRead(final String speak) {
        if(speak!=null){
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

            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    promptSpeechInput();
                }
            }.start();
        }else{
            promptSpeechInput();
        }

    }
    private final int REQ_CODE_SPEECH_INPUT = 100;

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Please speak");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry Speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void heartRate(View v){
        t1.speak("Kindly put your finger on camera till one minute", TextToSpeech.QUEUE_FLUSH, null);
        Intent intent=new Intent(getBaseContext(),HeartRateProcess.class);
        intent.putExtra("","");
        startActivity(intent);
}

    public void bmiCalculator(View v){
        Intent intent=new Intent(getBaseContext(),BMIResults.class);
        intent.putExtra("","");
        startActivity(intent);
    }
    public void musicPlayer(View v){
//        Intent intent=new Intent(getBaseContext(),MusicPlayerActivity.class);
//        intent.putExtra("","");
//        startActivity(intent);
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
        startActivity(intent);
    }
    public void stepCounter(View v){
        Intent intent=new Intent(getBaseContext(),SimplePedometerActivity.class);
        intent.putExtra("","");
        startActivity(intent);
    }
    public void trackOtherUsers(View v){
        Intent intent=new Intent(getBaseContext(),MapsActivity.class);
        intent.putExtra("","");
        startActivity(intent);
    }

    FirebaseDatabase database;
    DatabaseReference myRef;

    String query = "";
    ArrayList<String> lstSymptom = new ArrayList();
    ArrayList<String> lstDisplay = new ArrayList();
    ArrayList<String> lstID = new ArrayList();
    ArrayList<String> lstCancer = new ArrayList();
    ArrayList<String> lstLevel = new ArrayList();


    public void refresh(View v) {
        requestLocation();
        Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: " + location.getLatitude() + "," + location.getLongitude() + "altitude=" + location.getAltitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        prefrences.edit().putString("latitude", Double.toString(latitude)).putString("longitude", Double.toString(longitude)).apply();
        double distance = distance(Constant.latitude, latitude, Constant.longitude, longitude, Constant.altitude, 0);
        if (location.getLatitude() > 0 && location.getLongitude() > 0) {
            myRef.child(Constant.users).child(userid).child("latitude").setValue(latitude);
            myRef.child(Constant.users).child(userid).child("longitude").setValue(longitude);
        }

//        if (distance < 100) {
//            String current_date = Constant.getDate();
//            if (!current_date.equals(past_date)) {
//                String current_time = Constant.getTime();
//                AttendenceModel model = new AttendenceModel(auth.getUid(), auth.getCurrentUser().getDisplayName(), true, current_date, current_time);
//                myRef.child(Constant.user_attendence).push().setValue(model);
//                prefrences.edit().putString("past_date", current_date).apply();
//                Toast.makeText(context, "Attendence Marked Successfully", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Log.i(TAG, "onLocationChanged: attendece already marked");
//            }
//        } else {
//            Log.i(TAG, "onLocationChanged: distance not applicable" + distance);
//        }
        txtLoc.setText("Loc.." + latitude + "," + longitude);
        GetCurrentAddress getCurrentAddress = new GetCurrentAddress();
        getCurrentAddress.execute();


        Log.i(TAG, "onLocationChanged: distance=" + distance);
    }

    public String getAddress(Context ctx, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                String locality = address.getLocality();
                String region_code = address.getCountryCode();
                result.append(""
                        + address.getAddressLine(0) + " "
                        // + address.getAddressLine(1) + " "
                        + address.getPostalCode() + " ");
                //result.append(locality + " ");

                //result.append(region_code);

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    String address1 = "";

    private class GetCurrentAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // this lat and log we can get from current location but here we
            // given hard coded
            address1 = "";
            System.out.println("lati=" + latitude + " longi=" + longitude);

            String address = getAddress(context, latitude, longitude);
            System.out.println("address=" + address);

            return address;
        }

        @Override
        protected void onPostExecute(String resultString) {
            // dialog.dismiss();
            address1 = address1 + resultString;
            myRef.child(Constant.users).child(userid).child("location").setValue(address1);
            txtLoc.setText(address1);
        }
    }


    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    Context context;



    String token_id;

    @Override
    protected void onResume() {
        super.onResume();

        checkRegID();
        requestLocation();


        navigationView.getMenu().findItem(R.id.nav_sign_in).setTitle(prefrences.getString("name", ""));
        View hView = navigationView.getHeaderView(0);
        ImageView nav_user = (ImageView) hView.findViewById(R.id.imageView);
        TextView txtEmail = (TextView) hView.findViewById(R.id.txtEmail);
        TextView txtName = (TextView) hView.findViewById(R.id.txtName);
        txtEmail.setText(prefrences.getString("email", ""));
        txtName.setText(prefrences.getString("name", ""));
//            if(prefrences.getString("utype","").equalsIgnoreCase("admin")){
//                navigationView.getMenu().findItem(R.id.nav_admin).setVisible(false);
//            }
        try {

            Picasso.with(this).load(prefrences.getString("image", "")).into(nav_user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED)) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0,
                        1, this);
            } else if (locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0,
                        1, this);
            } else {


                Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }


    }

    public void checkRegID() {
        if (playServicesAvailable()) {

            Log.i(TAG, "onCreate: " + token_id);
            token_id = FirebaseInstanceId.getInstance().getToken();
            if (prefrences.getBoolean("reg_status", false)) {
                myRef.child(Constant.users).child(userid).child("token_id").setValue(token_id);
                prefrences.edit().putString("token_id", token_id).commit();


            }
            Log.i(TAG, "onCreate: " + token_id);

        } else {
            Log.i(TAG, "sendNotificationToUser: no play services");
            // ... log error, or handle gracefully
        }
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean playServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int RC_SIGN_IN = 123;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sign_in) {


            Intent intent1 = new Intent(getBaseContext(), ViewProfile.class);
            intent1.putExtra("userid", userid);
            startActivity(intent1);
            // already signed in

            // Handle the camera action
        } else if (id == R.id.nav_logout) {

            prefrences.edit().clear().commit();
            Intent intent1 = new Intent(getBaseContext(), LoginActivity.class);

            startActivity(intent1);
            finish();
            Toast.makeText(context, "Logout success", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_admin) {
//
            Intent intent1 = new Intent(getBaseContext(), AdminPanel.class);

            startActivity(intent1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String path;
    String TAG = "MAINACTIVITY";

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {


                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }else
        if(requestCode==REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
// \n To Make Phone Call say Call (Mobile No.)
//        \nTo Send SMS Say send SMS (Message) To (Mobile No.)
//        \nTo Navigate Say Navigate From (Source) To (Destination)
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String value = result.get(0).toLowerCase();
                Log.i("VOICE", value);
                try {
                    if (value.contains("bmi")||value.contains("vmi")) {
                        Intent intent=new Intent(getBaseContext(),BMIResults.class);
                        intent.putExtra("","");
                        startActivity(intent);

                    } else if (value.contains("heart rate")) {
                        t1.speak("Kindly put your finger on camera till one minute", TextToSpeech.QUEUE_FLUSH, null);
                        Intent intent=new Intent(getBaseContext(),HeartRateProcess.class);
                        intent.putExtra("","");
                        startActivity(intent);
                    } else if (value.contains("pedometer")) {
                        Intent intent=new Intent(getBaseContext(),SimplePedometerActivity.class);
                        intent.putExtra("","");
                        startActivity(intent);
                    } else if (value.contains("music")) {
//                        Intent intent=new Intent(getBaseContext(),MusicPlayerActivity.class);
//                        intent.putExtra("","");
//                        startActivity(intent);
                        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                        startActivity(intent);
                    } else {
                        speakRead("Please say again");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    speakRead("Please say again");
                }
            }
            }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void showSnackbar(final int id) {
        Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();


    }

    Location location = null;

    public void requestLocation() {
        Log.i(TAG, "requestLocation: requesting location");

        try {
            LocationManager locationManager;
            String contex = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) getSystemService(contex);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, false);
            int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

            if (rc == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(provider);
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                //seconds and meter
                locationManager.requestLocationUpdates(provider, 0, 0,
                        locationListener);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            updateWithNewLongitude(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

            //updateWithNewLongitude(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    double latitude = 0.0, longitude = 0.0;

    private void updateWithNewLongitude(Location location) {

        // myLocationText = (TextView) findViewById(R.id.myLocationText);
        if (location != null) {
            latitude = location.getLongitude();
            longitude = location.getLongitude();

            //  Toast.makeText(context, "Current Loc="+latitude+","+longitude, Toast.LENGTH_SHORT).show();

        }

    }
    public void speak(View v){
        speakRead("Your options are Music, Heart Rate, Pedometer and Music Player");
    }
}
