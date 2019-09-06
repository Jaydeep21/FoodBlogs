package com.example.foodblogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class AddRecipeFragment extends Fragment  {
    Button b1;
    EditText e1,e2,e3;
    ImageView i1,i2;
    Spinner s1,s2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_addrecipe,container,false);
        e1 = view.findViewById(R.id.dishName);
        s1 = view.findViewById(R.id.spinner1);
        s2 = view.findViewById(R.id.spinner2);

        i1 =  view.findViewById(R.id.camera);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
       });

        i2 =  view.findViewById(R.id.camera);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        e2 = view.findViewById(R.id.description);

        e3 = view.findViewById(R.id.recipie);

        b1 = view.findViewById(R.id.addRecipie);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }



    public void openCamera(View v){
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }


}
