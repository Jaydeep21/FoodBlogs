package com.example.foodblogs;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements Adapter.ItemClickListener{

    RecyclerView recyclerView;
    ArrayList<ClipData.Item> arrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView = view.findViewById(R.id.list_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);


        List<ModelClass> modelClassList = new ArrayList<>();

        DbHelper database = new DbHelper(getContext());
        SQLiteDatabase db = database.getWritableDatabase();

        if(db!=null){
            Cursor cursor = db.rawQuery("select * from recipie",null);
            Log.d("TAG", "Cursor Count: " + cursor.getCount());
            if(cursor.getCount()==0){
                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
            }else{
                while (cursor.moveToNext()){
                    //byte[] imagebyte = cursor.getBlob(5);
                    //Bitmap objectBitmap = BitmapFactory.decodeByteArray(imagebyte,0,imagebyte.length);

                    modelClassList.add(
                            new ModelClass(cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3) ,
                                    cursor.getString(4),
                                    cursor.getString(5),
                                    cursor.getString(6),
                                    cursor.getString(7),
                                    cursor.getString(8)
                            )
                    );
                }
            }
            cursor.close();
        }
        Adapter adapter = new Adapter(getContext(), modelClassList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setItemClickListener(
                new Adapter.ItemClickListener() {
                    @Override
                    public void onClick(String email, String dishName, String cusine, String course, String image, String video, String description, String recipie) {

                        Intent intent = new Intent(getActivity(), watchVideo.class);
                        intent.putExtra("email", email);
                        intent.putExtra("dishname", dishName);
                        intent.putExtra("cusine", cusine);
                        intent.putExtra("course", course);
                        intent.putExtra("description", description);
                        intent.putExtra("recipie", recipie);
                        intent.putExtra("image",   image);
                        intent.putExtra("video",   video);
                        startActivity(intent);
                    }
                }
        );

        return view;
    }

    @Override// Bitmap video,
    public void onClick(String email, String dishName, String cusine, String course, String image, String video, String description, String recipie) {
        Intent intent = new Intent(getActivity(), watchVideo.class);
        intent.putExtra("email", email);
        intent.putExtra("dishname", dishName);
        intent.putExtra("cusine", cusine);
        intent.putExtra("course", course);
        intent.putExtra("description", description);
        intent.putExtra("recipie", recipie);
        intent.putExtra("image",   image);
        intent.putExtra("video",   video);
        startActivity(intent);
    }
}
