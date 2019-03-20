package com.example.melik.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.database.CustomerDB;
import com.example.melik.service.Service;


public class MainActivity extends AppCompatActivity {

    private Button listenButton;
    private TextView resultTextView;
    private EditText queryText;
    private Service service;
    private CustomerDB customerDB;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //initialize kısmı
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = (Button) findViewById(R.id.listenButton);
        queryText = (EditText) findViewById(R.id.queryText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        customerDB = new CustomerDB(getApplicationContext());

        service=new Service(listenButton,queryText,resultTextView); //her işimizi bu servis arkadaşına yaptırıcaz tüm metotları
        service.InsertTables(customerDB);//syncdata fonksiyonunda sqllite çalıştırıyoruz bu çalıştırma için context'e ihtiyaç duyuyor o yüzden parametre olarak gönderiyoruz.

    }


    public void onClick (View view){ //tek butonumuz var zati
        service.StartChat();
    }
}





