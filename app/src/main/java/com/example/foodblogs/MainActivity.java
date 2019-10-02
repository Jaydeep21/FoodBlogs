package com.example.foodblogs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    RelativeLayout rellay1, rellay2;
    Button signup,login;
    EditText email,password;
    DatabaseHelper db;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        if( pref.contains("email") && pref.contains("password")){
            Intent intent = new Intent(MainActivity.this,bottomnavigation.class);
            startActivity(intent);
        }
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Signup.class);
                startActivity(i);

            }
        });
        db = new DatabaseHelper(this);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = email.getText().toString();
                String s2 = password.getText().toString();

                Boolean emailpassword = db.emailpassword(s1,s2);

                if(emailpassword==true){

                    pref = getSharedPreferences("user_details",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email",s1);
                    editor.putString("password",s2);
                    editor.commit();
                    Log.e("email",s1);
                    Toast.makeText(getApplicationContext(),"Successfully Logged in",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,bottomnavigation.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
