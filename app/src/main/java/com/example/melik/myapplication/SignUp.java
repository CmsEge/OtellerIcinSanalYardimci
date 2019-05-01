package com.example.melik.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignUp  extends AppCompatActivity {

    private Button signUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUp=findViewById(R.id.signUpButton);
    }
    public void SignUp(View v){
        Intent intent=new Intent(SignUp.this,SignIn.class);
        startActivity(intent);
    }
}
