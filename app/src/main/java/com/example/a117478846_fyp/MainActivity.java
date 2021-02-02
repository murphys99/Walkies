package com.example.a117478846_fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity  {
    private Button create, signIn;



//Used this video to style buttons https://www.youtube.com/watch?v=nlPtfncjOWA&t=306s&ab_channel=Stevdza-San
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //launch the first activity. Create
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(),RegisterUser.class);
                startActivity(startIntent);
            }
        });

        //launch the sign in activity
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIntent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signIntent);
            }
        });

    }



    }
