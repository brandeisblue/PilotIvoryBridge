package com.johnshopkins.ivorybridge.pilotivorybridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    private TextView mTempMax, mTempMin, mWeatherCondition;
    private ImageView mWeatherConditionImage;
    private static final String TAG = Dashboard.class.getName();

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private DatabaseReference mConditionRef;


    @Override
    protected void onStart() {
        super.onStart();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mConditionRef = mRootRef.child("condition");

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Initiate Firebase
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //Set up Views
        mTempMax = (TextView) findViewById(R.id.dashboard_tempMax_text_view);
        mTempMin = (TextView) findViewById(R.id.dashboard_tempMin_text_view);
        mWeatherCondition = (TextView) findViewById(R.id.dashboard_weathercondition_textview);
        mWeatherConditionImage = (ImageView) findViewById(R.id.dashboard_weather_icon_imageview);

        // Session check - if the user is logged in
        if (mAuth.getCurrentUser() != null) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "Test: Dashboard onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout_menu:
                logoutUser();
                return true;
            case R.id.account_menu:
                startActivity(new Intent(Dashboard.this, Account.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(Dashboard.this, MainActivityLogIn.class));
            finish();
        }
    }
}
