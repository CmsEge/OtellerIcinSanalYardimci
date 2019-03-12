package com.example.melik.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.*;
import ai.api.ui.AIDialog;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.service.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private Button listenButton;
    private TextView resultTextView;
    private EditText queryText;
    private Service service;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //initialize kısmı
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.listenButton);
        queryText = (EditText) findViewById(R.id.queryText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        service=new Service(listenButton,queryText,resultTextView); //her işimizi bu servis arkadaına yaptırıcaz tüm metotları
    }
    public void onClick (View view){ //tek butonumuz var zati

        service.StartChat();
    }
   /* private void insert() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql//192.168.0.23/ogrenci_schema";
            Connection c=DriverManager.getConnection(url,"tez","14.Cms.14");
            PreparedStatement st=c.prepareStatement("insert into student values(?,?,?,?)");
            st.setInt(1,8);
            st.setString(2,"A001");
            st.setString(3,"momo");
            st.setString(4,"coco");
            st.execute();
            st.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }*/
}





