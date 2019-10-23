package com.example.foodblogs;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;

public class DbHelper extends SQLiteOpenHelper {
    //SharedPreferences pref;
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 2;

    SQLiteDatabase db;
    ContentResolver mContentResolver;
    Context context;
    public final static String COLUMN_NAME = "imagename";

    public final static String TABLE_NAME = "recipie";

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
//        mContentResolver = context.getContentResolver();
//
//        db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT ,  dishname TEXT , cusine TEXT, course TEXT,image Text, video Text, description TEXT, recipie TEXT, FOREIGN KEY(email) REFERENCES user(email)  " + " );";

        db.execSQL(SQL_CREATE_IMAGE_TABLE);

        Log.d(TAG, "Database Created Successfully" );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addToDb(ModelClass objectModelClass){

        SQLiteDatabase db =this.getWritableDatabase();

        // TODO: add code to get the image and store it in a folder
        String newImagepath = null;
        if (!objectModelClass.isImageSaved()) {
            newImagepath = saveImage(new File(objectModelClass.getImage()), objectModelClass.getTitle());
        } else {
            newImagepath = objectModelClass.getImage();
        }


        ContentValues cv = new  ContentValues();

        cv.put("email", objectModelClass.getEmail());
        cv.put("dishname", objectModelClass.getTitle());
        cv.put("cusine", objectModelClass.getCusine());
        cv.put("course", objectModelClass.getCourse());
        cv.put("description", objectModelClass.getDescription());
        cv.put("recipie", objectModelClass.getRecipie());
        cv.put("image",  newImagepath);
        //cv.put("video",   video);
        long checkIfQueryRuns = db.insert( TABLE_NAME, null, cv );
        if(checkIfQueryRuns!=0){
            //Toast.makeText(context,"Data Inserted",Toast.LENGTH_SHORT).show();
            db.close();
        }
        else{
            //Toast.makeText(context,"Not Inserted",Toast.LENGTH_SHORT).show();
        }
    }

//    public void listDetails(List<ClipData.Item> arrayList){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from recipie",null);
//        if(cursor != null){
//            if(cursor.moveToFirst()){
//                do{
//                    ClipData.Item item = new ClipData.Item(arrayList);
//                }while ();
//            }
//        }
//    }
    /**
     * This method will save all the images to a folder named images in the internal storage
     *
     */
    private String saveImage(File uploadingFile, String title) {
        String newFilePath = null;
        try {
            File internalFilesDir = context.getFilesDir();
            File imagesFolder = new File(internalFilesDir + "/images");

            if (!imagesFolder.exists()) {
                // if the images folder does not exist, create one
                imagesFolder.mkdir();
            }

            // jo file humko upload karna hai usko humne inputstream lagaya hai kynki usse humme data lena hai
            FileInputStream fileInputStream = new FileInputStream(uploadingFile);

            // yeh naya file banaya recipie ke title ka naam ka
            File file = new File(imagesFolder + "/" + title + ".jpeg");
            // ab file ke liye ek output stream banaya kyunki yeh file ko humme write karna hai
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            // yeh byte array banaya hai taki hum thoda thooda kare content idhar se udhar kar sake
            byte[] bytes = new byte[1024];
            int length;

            // yeh line apne upload hone wale file se kuch content read karega aur usko byte wale array me store karega
            // agar bytes baki hai aur uska length return karega, aur agar nahi baki hai toh -1 return karega
            while((length = fileInputStream.read(bytes)) != -1) {
                // yeh line tere file se 1024 bytes tak read karega, agar file me bytes ha toh
                fileOutputStream.write(bytes, 0, length);
            }
            Log.d("TAG", "Image with name " + file.getName() + " saved at " + file.getAbsolutePath());
            fileInputStream.close();
            fileOutputStream.close();
            newFilePath = file.getAbsolutePath();
        } catch(IOException e){
            e.printStackTrace();
            Log.e("TAG", "Image couldn\'t be saved");
        }

        return newFilePath;
    }
}
