package com.example.foodblogs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    Button signup;
    EditText username,email,pass,cpass;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = username.getText().toString();
                String s2 = email.getText().toString();
                String s3 = pass.getText().toString();
                String s4 = cpass.getText().toString();

                if(username.equals("") || email.equals("") || pass.equals("") || cpass.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(s3.equals(s4)){
                        Boolean checkmail = db.checkemail(s2);
                        if(checkmail==true){
                            Boolean insert  = db.insert(s1,s2,s3);
                            if(insert==true){
                                Toast.makeText(getApplicationContext(),"Successfully signed in",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Query error",Toast.LENGTH_SHORT);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email alresdy exits",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
