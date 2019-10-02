package com.example.foodblogs;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddRecipeFragment extends Fragment implements View.OnClickListener {
    Button b1;
    EditText e1,e2,e3;
    ImageView i1,i2, i3, i4, i5;
    Spinner s1,s2;
    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int PICK_IMAGE_REQUEST = 100;
    Bitmap imageToStore;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private boolean hasImageChanged = false;
    Bitmap thumbnail;
    SharedPreferences pref;
    DbHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_addrecipe,container,false);

        e1 = view.findViewById(R.id.dishName);
        s1 = view.findViewById(R.id.spinner1);
        s2 = view.findViewById(R.id.spinner2);

        i1 =  view.findViewById(R.id.camera);

        i1.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            i1.setEnabled(false);
            //i2.setEnabled(false);
//            i3.setEnabled(false);
//            i4.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            i1.setEnabled(true);
//            i2.setEnabled(true);
//            i3.setEnabled(true);
//            i4.setEnabled(true);
        }
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"hey",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_PHOTO);
            }
       });
        i2 =  view.findViewById(R.id.imageupload);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        i3 =  view.findViewById(R.id.capture_video);
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 1 );
            }
        });
        i4 =  view.findViewById(R.id.videoupload);
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        i5 =  view.findViewById(R.id.selectedimage);
        e2 = view.findViewById(R.id.description);

        e3 = view.findViewById(R.id.recipie);

        b1 = view.findViewById(R.id.addRecipie);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DbHelper(getContext());
                addToDb(v);
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                i1.setEnabled(true);
            }
        }
    }

    public void setProgressBar(){
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < 100){
                    progressBarStatus += 30;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }

            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO){
            if(resultCode == Activity.RESULT_OK) {
                try {
                     Uri imageUri = data.getData();
                     InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                     //imageToStore = (Bitmap) BitmapFactory.decodeStream(new ByteArrayInputStream(imageStream.toByteArray()));
                     imageToStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                    //set Progress Bar
                    //setProgressBar();
                    //set profile picture form gallery
                    i5.setImageBitmap(imageToStore);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == Activity.RESULT_OK) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");

        //set Progress Bar
        //setProgressBar();
        //set profile picture form camera
        i5.setMaxWidth(200);
        i5.setImageBitmap(thumbnail);
    }

//    public byte[] imageViewToByte(ImageView image) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_launcher);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        return byteArray;
//    }

    public void addToDb(View view){

        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String email = pref.getString("email",null);
        String dishname = e1.getText().toString();
        String cusine = s1.getSelectedItem().toString();
        String course = s2.getSelectedItem().toString();
        String descripton = e2.getText().toString() ;
        String recipie = e3.getText().toString();
        //byte[] data= imageViewToByte(i5);
        Bitmap video = null;
       // byte[] data = null;
//        i5.setDrawingCacheEnabled(true);
//        i5.buildDrawingCache();
//        Bitmap bitmap = i5.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //byte[] data = baos.toByteArray();

        dbHelper.addToDb(new ModelClass(email,dishname,cusine,course,imageToStore,descripton,recipie));
        //email,dishname,cusine,course,data,video,descripton,recipie
        Toast.makeText(getActivity(), "Image saved to DB successfully", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

    }

}
