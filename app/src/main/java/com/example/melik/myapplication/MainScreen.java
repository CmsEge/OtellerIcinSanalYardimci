package com.example.melik.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    private Button shire;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        shire=findViewById(R.id.shireButton);

    }
    public void ChatBot(View v){
        Intent intent=new Intent(MainScreen.this,MainActivity.class);
        startActivity(intent);
    }
    public void LogOut(View v){
        Intent intent = new Intent(MainScreen.this, SignIn.class);
        startActivity(intent);
    }
}