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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
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
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
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

}