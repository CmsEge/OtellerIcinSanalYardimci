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
    private boolean success=false;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //initialize kısmı
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.listenButton);
        queryText = (EditText) findViewById(R.id.queryText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        service=new Service(listenButton,queryText,resultTextView); //her işimizi bu servis arkadaına yaptırıcaz tüm metotları
        StudentList=new ArrayList<Student>();
        SyncData orderData=new SyncData();
        orderData.execute("");
    }
    private class SyncData extends AsyncTask<String,String,String> {
        String msg="Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom For details!";

        @Override
        protected void onPreExecute(){
            //do nothing
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //burada sıkıntı var sorunu anlamıyorum
                String url="jdbc:mysql//192.168.0.23/ogrenci_schema"; //192 li yere kendi ipv4 adresini yaz
                //veri tabanında bir veri tabanı(schema) oluştur oluşturduğunun ismini ogrenci_schema yerine yaz, gerisine dokunma
                Connection c= DriverManager.getConnection(url,"tez","14.Cms.14");
                //kendi username ve şifren mysqldeki
                if(c==null){
                    success=false;
                }else{
                    String query="SELECT id,first_name,last_name,email FROM student";
                    Statement stmt=c.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    if(rs!=null){
                       // Log.i(rs.getString("first_name"),rs.getString("last_name"));
                        success=true;
                        Log.i("success ","true rs null değil");
                    }else{
                        success=false;
                        Log.i("success ","false rs null");
                    }
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                Log.i("success ","false class forname de çaktı");
            }
            return null;
        }
        protected void onPostExecute(){
            //do nothing
        }
    }
    public void onClick (View view){ //tek butonumuz var zati
        service.StartChat();
    }
}





