package com.example.melik.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.melik.database.Database;
import com.example.melik.service.Service;
import com.example.melik.eventbrite.Configuration;
public class SignIn extends AppCompatActivity {
    private Button signUp;
    private Database database;
    private Service service;
    private EditText email;
    private EditText pass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(getApplicationContext());
        service = new Service(database);
        //service.InsertTables();
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       // getActionBar().hide();
       if(service.getCustomerbyStatus().size()>0){
           Log.i("Customer", service.listAll("Customer").toString());
           Intent intent = new Intent(SignIn.this, MainScreen.class);
           startActivity(intent);
        }else{
           Log.i("Customer", service.listAll("Customer").toString());
            setContentView(R.layout.log_in);
       }
        signUp = findViewById(R.id.signUpButton);
    }
    public void SignUp(View v) {
        Intent intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
    }
    public void SignIn(View v) {
        email = (EditText) findViewById(R.id.mailEdit2);
        pass = (EditText) findViewById(R.id.passEdit2);
        if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please, fill the rows!", Toast.LENGTH_LONG).show();
        } else if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            String password = service.getCustomerByEmail(email.getText().toString());
            if (password.equals(pass.getText().toString())) {
                service.updateCustomer(email.getText().toString(),1);
                Log.i("Customer", service.listAll("Customer").toString());
                Intent intent = new Intent(SignIn.this, MainScreen.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "User couldn't find.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please, fill right your email!", Toast.LENGTH_LONG).show();
        }
    }
}
