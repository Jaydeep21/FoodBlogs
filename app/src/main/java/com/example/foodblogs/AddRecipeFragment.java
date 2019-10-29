package com.example.foodblogs;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddRecipeFragment extends Fragment implements View.OnClickListener {
    Button b1;
    EditText e1, e2, e3;
    ImageView i1, i2, i3, i4, i5;
    Spinner s1, s2;
    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int SELECT_VIDEO = 3;
    private static final int CAPTURE_VIDEO = 4;

    private static final int PICK_IMAGE_REQUEST = 100;
    private Bitmap imageToStore;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private boolean hasImageChanged = false;
    Bitmap thumbnail;
    SharedPreferences pref;
    DbHelper dbHelper;
    private String imageFilePath;
    private String videoFilePath;
    private boolean isimageTakenFromCamera = false;
    private boolean isvideoTakenFromCamera = false;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_addrecipe, container, false);

        e1 = view.findViewById(R.id.dishName);
        s1 = view.findViewById(R.id.spinner1);
        s2 = view.findViewById(R.id.spinner2);

        i1 = view.findViewById(R.id.camera);

        i1.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            i1.setEnabled(false);

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            i1.setEnabled(true);

        }
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"hey",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_PHOTO);
            }
        });
        i2 = view.findViewById(R.id.imageupload);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        i3 = view.findViewById(R.id.capture_video);
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
        i4 = view.findViewById(R.id.videoupload);
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        i5 = view.findViewById(R.id.selectedimage);
        e2 = view.findViewById(R.id.description);

        e3 = view.findViewById(R.id.recipie);

        b1 = view.findViewById(R.id.addRecipie);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DbHelper(getContext());
                addToDb(v);
                if (isimageTakenFromCamera) {
                    imageFilePath = saveImage(bitmap, e1.getText().toString());
                }
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

    public void setProgressBar() {
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
                while (progressBarStatus < 100) {
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

        Log.d("TAG", "Request Code: " + requestCode + " Result code: " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    // image is selected form gallery
                    i5.setMaxWidth(200);
                    i5.setImageBitmap(onGalleryImageSelected(data));
                    isimageTakenFromCamera = false;
                    break;
                case CAPTURE_PHOTO:
                    // image is capture using the camera
                    i5.setMaxWidth(200);
                    i5.setImageBitmap(onCaptureImageResult(data));
                    isimageTakenFromCamera = true;
                    break;
                case SELECT_VIDEO:
                    onGalleryVideoSelected(data);
                    isvideoTakenFromCamera = false;
                    break;
                case CAPTURE_VIDEO:
                    isvideoTakenFromCamera= true;
                    break;
            }
        }

    }

    private Bitmap onGalleryImageSelected(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
        cursor.close();
        imageFilePath = imgDecodableString;
        return bitmap;
    }


    private static final int pick = 100;
    Uri videoUri;
    VideoView videoview;
    private void onGalleryVideoSelected(Intent data) {


        try {
            File newfile;

            AssetFileDescriptor videoAsset = getActivity().getContentResolver().openAssetFileDescriptor(data.getData(), "r");
            FileInputStream in = videoAsset.createInputStream();

            File filepath =  getContext().getFilesDir();
            File dir = new File(filepath.getAbsolutePath() + "/" +"videos" + "/");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            newfile = new File(dir, "save_"+ e1.getText().toString() +".mp4");

            if (newfile.exists()) newfile.delete();

            OutputStream out = new FileOutputStream(newfile);

            // Copy the bits from in stream to out stream
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

            Log.v("", "Copy file successful.");

            videoUri = data.getData();
            videoview.setVideoURI(videoUri);
            videoview.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        //set Progress Bar
        setProgressBar();
        return bitmap;
    }

    public void addToDb(View view) {

        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String email = pref.getString("email", null);
        String dishname = e1.getText().toString();
        String cusine = s1.getSelectedItem().toString();
        String course = s2.getSelectedItem().toString();
        String descripton = e2.getText().toString();
        String recipie = e3.getText().toString();



        dbHelper.addToDb(new ModelClass(email, dishname, cusine, course, imageFilePath, videoFilePath,descripton, recipie, isimageTakenFromCamera, isvideoTakenFromCamera));
        //email,dishname,cusine,course,data,video,descripton,recipie
        Toast.makeText(getActivity(), "Image saved to DB successfully", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

    }

    private String getFilePath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Log.e("TAG", "IMAGE PATH: " + selectedImage.getPath());

        // Get the cursor
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        return imgDecodableString;
    }

    private String saveImage(Bitmap bitmap, String title) {
        String filename = getActivity().getFilesDir() + "/images/" + title + ".jpeg";
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }
}
