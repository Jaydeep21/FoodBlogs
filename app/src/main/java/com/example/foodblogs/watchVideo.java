package com.example.foodblogs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class watchVideo extends AppCompatActivity {

    TextView t1,t2,t3,t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        //Log.d("walsh", getIntent().getStringExtra("email"));

        t1 = findViewById(R.id.title);
        t1.setText(getIntent().getStringExtra("dishname"));

        t2 = findViewById(R.id.cusine);
        t2.setText(getIntent().getStringExtra("cusine"));

        t3 = findViewById(R.id.course);
        t3.setText(getIntent().getStringExtra("course"));

        t4 = findViewById(R.id.recipie);
        t4.setText(getIntent().getStringExtra("recipie"));
    }
}
