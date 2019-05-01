package com.example.melik.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.melik.eventbrite.Configuration;
import com.example.melik.places.PlaceMain;
import com.example.melik.service.Service;
import com.example.melik.database.Database;

public class MainScreen extends AppCompatActivity {

    private Service service;
    private Database database;
    private Button shire;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        shire=findViewById(R.id.shireButton);
        database=new Database(getApplicationContext());
        service= new Service(database);

    }
    public void ChatBot(View v){
        Intent intent=new Intent(MainScreen.this,MainActivity.class);
        startActivity(intent);
    }
    public void LogOut(View v){
        service.changeStatus();
        Log.i("customer", service.listAll("Customer").toString());
        Intent intent = new Intent(MainScreen.this, SignIn.class);
        startActivity(intent);
    }
    public void Event(View v){
        Intent intent=new Intent(MainScreen.this, Configuration.class);
        startActivity(intent);
    }
    public void Places(View v){
        Intent intent=new Intent(MainScreen.this, PlaceMain.class);
        startActivity(intent);
    }
}