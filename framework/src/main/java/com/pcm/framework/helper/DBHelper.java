package com.pcm.framework.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.pcm.framework.data.MyData;
import com.pcm.framework.util.LogUtils;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "photoData";
    public static final String URIString = "URIString";
    public static final String PicsName = "PicsName";
    public static final String Describe = "Describe";
    public static final String Title= "Title";
    public static final String Path = "Path";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    URIString + " TEXT PRIMARY KEY," +
                    Path + " TEXT," +
                    PicsName + " TEXT," +
                    Describe + " TEXT," +
                    Title + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";


    private static volatile DBHelper dbHelper;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getDbHelper(Context context) {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }


    private SQLiteDatabase db_writeable;
    private SQLiteDatabase db_readable;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(String uri, String path, String picsName, String title, String describe) {
        if (db_writeable == null) {
            db_writeable = dbHelper.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(URIString, uri);
        values.put(PicsName, picsName);
        values.put(Describe, describe);
        values.put(Path, path);
        values.put(Title, title);
        long insert = db_writeable.insert(TABLE_NAME, null, values);
        LogUtils.getLogger().i("insert:" +insert);
    }
    public void delete(String[] uris){
        if (db_writeable == null) {
            db_writeable = dbHelper.getWritableDatabase();
        }
        String selection = URIString + " = ?";

        // Issue SQL statement.
        int deletedRows = db_writeable.delete(TABLE_NAME, selection, uris);
        LogUtils.getLogger().i("deleteRows:" + deletedRows);
    }

    public void queryAndGetToMyData(){
        if (db_readable == null) {
            db_readable = dbHelper.getReadableDatabase();
        }
        String sortOrder =
                URIString + " DESC";
        Cursor cursor = db_readable.query(TABLE_NAME, new String[]{URIString, PicsName, Describe, Title, Path},
                null,null, null, null, sortOrder);
        while(cursor.moveToNext()){
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(URIString));
            String picsName = cursor.getString(cursor.getColumnIndexOrThrow(PicsName));
            String describe = cursor.getString(cursor.getColumnIndexOrThrow(Describe));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Title));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(Path));
            MyData.getMyData().getPhotoUris().add(uri);
            MyData.getMyData().getPicsName().add(picsName);
            MyData.getMyData().getTitle().add(title);
            MyData.getMyData().getDescribe().add(describe);
            MyData.getMyData().getPathes().add(path);
        }
    }

    public void updateNameTitleDes(String uriString, String name, String title, String describe) {
        if (db_writeable == null) {
            db_writeable = dbHelper.getWritableDatabase();
        }
        ContentValues value = new ContentValues();
        value.put(Title, title);
        value.put(Describe, describe);
        value.put(PicsName, name);
        int count = db_writeable.update(TABLE_NAME, value, URIString + " = ?", new String[]{uriString});
        LogUtils.getLogger().i("update:" + count);
    }

}
