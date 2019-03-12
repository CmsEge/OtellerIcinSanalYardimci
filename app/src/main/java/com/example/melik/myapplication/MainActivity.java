package com.example.melik.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.melik.service.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button listenButton;
    private TextView resultTextView;
    private EditText queryText;
    private Service service;

    private ArrayList<Student> StudentList;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //initialize kısmı
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.listenButton);
        queryText = (EditText) findViewById(R.id.queryText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        service=new Service(listenButton,queryText,resultTextView); //her işimizi bu servis arkadaına yaptırıcaz tüm metotları

        SyncData orderData=new SyncData();//veri tabanı bağlantısı başlatılıyor
        orderData.execute("");
    }
    private class SyncData extends AsyncTask<String,String,String> {//veri tabanı bağlantısı başlatılıyor
        @Override
        protected String doInBackground(String... strings) {
            service.SyncData();
            return null;
        }
    }
    public void onClick (View view){ //tek butonumuz var zati
        service.StartChat();
    }
}





