package com.example.melik.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.melik.myapplication.MainActivity;
import com.example.melik.database.Database;
import com.example.melik.service.Service;

public class SignUp  extends AppCompatActivity {
    private Button signUp;
    private EditText name;
    private EditText surname;
    private EditText phoneNo;
    private EditText roomNo;
    private EditText email;
    private EditText pass;
    private Database database;
    private Service service;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUp=findViewById(R.id.signUpButton);
        database=new Database(getApplicationContext());
        service=new Service(database);
        service.InsertTables();
        Log.i("list",service.getAllAlacarteNames().toString());
    }
    public void SignUp(View v){
        name = (EditText) findViewById(R.id.nameEdit);
        surname = (EditText) findViewById(R.id.surNameEdit);
        phoneNo = (EditText) findViewById(R.id.phoneNoEdit);
        roomNo = (EditText) findViewById(R.id.roomNoedit);
        email = (EditText) findViewById(R.id.emailEdit);
        pass = (EditText) findViewById(R.id.passEdit2);
        if(name.getText().toString().isEmpty() || surname.getText().toString().isEmpty() || phoneNo.getText().toString().isEmpty() || roomNo.getText().toString().isEmpty() || email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please, fill all the cell!",Toast.LENGTH_LONG).show();
        }else{
            if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                service.insertCustomer(name.getText().toString(),surname.getText().toString(),roomNo.getText().toString(),phoneNo.getText().toString(),pass.getText().toString(),email.getText().toString());
                Log.i("customer", service.listAll("Customer").toString());
                Intent intent=new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Please, fill right your email!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
