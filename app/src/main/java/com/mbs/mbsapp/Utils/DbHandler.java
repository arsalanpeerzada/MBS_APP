package com.mbs.mbsapp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dell 5521 on 9/23/2016.
 */
public class DbHandler extends SQLiteOpenHelper {

    protected static String DATABASE_NAME = "smart_rest_db.sqlite";
    protected static String DATABASE_PATH;
    private static final int DATABASE_VERSION = 1;
    private static Context context;
    private static DbHandler handler;
    private SQLiteDatabase database;


    public SQLiteDatabase getDatabase(){
        return database;
    }

    public static DbHandler getInstance(Context _context){
        context = _context;
        return handler!=null?handler:(handler = new DbHandler(_context));
    }

    public static DbHandler newInstance(){
        return handler = new DbHandler(context);
    }

    private DbHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
//        DATABASE_PATH = Environment.getExternalStorageDirectory()+"/SmartRestaurant/db/";
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        try {

                createDatabase();
                openDatabase();
            } catch (Exception e) {e.printStackTrace();}
    }

    private void createDatabase() throws IOException {
        if(!isDatabaseExist()){
            this.getReadableDatabase();
            copyDatabaseBase();
        }
    }

    public boolean isDatabaseExist(){
        return (new File(DATABASE_PATH+DATABASE_NAME)).exists();
    }

    private void copyDatabaseBase(){
        try {
            InputStream readStream = context.getAssets().open(DATABASE_NAME);
            File file = new File(DATABASE_PATH);
            if(file.exists()){
                DeleteRecursive(file);
                if(file.mkdirs()) {
                    String outputFilePath = DATABASE_PATH + DATABASE_NAME;
                    OutputStream writeStream = new FileOutputStream(outputFilePath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = readStream.read(buffer)) > 0)
                        writeStream.write(buffer, 0, length);
                    writeStream.flush();
                    writeStream.close();
                    readStream.close();
                } else {
                    //AppLog.d("DbError","error in creating db file");
                }
            } else {
                file.mkdirs();
                String outputFilePath = DATABASE_PATH + DATABASE_NAME;
                OutputStream writeStream = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = readStream.read(buffer)) > 0)
                    writeStream.write(buffer, 0, length);
                writeStream.flush();
                writeStream.close();
                readStream.close();
            }
        } catch (Exception e){e.printStackTrace();}
    }

    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    public boolean openDatabase(){
        try {
            String path = DATABASE_PATH+DATABASE_NAME;
            database = SQLiteDatabase.openDatabase(path,null, SQLiteDatabase.OPEN_READWRITE);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized void close() {
        if(database!=null) database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
