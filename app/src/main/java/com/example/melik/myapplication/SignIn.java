package com.example.melik.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewAnimator;


public class SignIn extends AppCompatActivity {

    private Button signUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        signUp=findViewById(R.id.signUpButton);
    }
    public void SignUp(View v){
        Intent intent=new Intent(SignIn.this,SignUp.class);
        startActivity(intent);
    }
    public void SignIn(View v){
        Intent intent=new Intent(SignIn.this,MainScreen.class);
        startActivity(intent);
    }
}
