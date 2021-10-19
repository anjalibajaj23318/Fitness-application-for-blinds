package com.fitness.tracker;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by inspirin on 10/16/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    String TAG = "FIS";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferences prefrences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        prefrences.edit().putString("token_id", refreshedToken).commit();

        FirebaseAuth auth = auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (auth.getCurrentUser() != null && prefrences.getBoolean("reg_status", false)) {
            DatabaseReference myRef = database.getReference(Constant.DB);


        }


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
