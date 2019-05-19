package com.example.melik.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.eventbrite.Configuration;
import com.example.melik.places.PlaceMain;
import com.example.melik.service.Service;
import com.example.melik.database.Database;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    private Service service;
    private Database database;
    private Button shire;
    private TextView text;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        shire = findViewById(R.id.shireButton);
        database = new Database(getApplicationContext());
        service = new Service(database);
        text = findViewById(R.id.textView);
        ArrayList<String> cust = service.getCustomerbyStatus();
        text.append("Welcome " + cust.get(1));
        checkLocationPermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        service.changeStatus();
        Intent intent = new Intent(MainScreen.this, SignIn.class);
        startActivity(intent);
    }


    public void ChatBot(View v) {
        Intent intent = new Intent(MainScreen.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
    }

    public void LogOut(View v) {
        service.changeStatus();
        Log.i("customer", service.listAll("Customer").toString());
        Intent intent = new Intent(MainScreen.this, SignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void Event(View v) {
        Intent intent = new Intent(MainScreen.this, Configuration.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
    }

    public void Places(View v) {
        Intent intent = new Intent(MainScreen.this, PlaceMain.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainScreen.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}