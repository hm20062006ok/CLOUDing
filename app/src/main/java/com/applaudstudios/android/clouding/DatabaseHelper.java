package com.applaudstudios.android.clouding;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjplaud83 on 4/7/17.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Clouds.db";
    private static final String TEXT_TYPE = " TEXT";
 //   private static final String REAL_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.Clouds.TABLE_NAME + " (" +
                    DatabaseContract.Clouds._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Clouds.COLUMN_NAME_CLOUD_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Clouds.COLUMN_NAME_CLOUD_RATING + INT_TYPE + COMMA_SEP +
                    DatabaseContract.Clouds.COLUMN_NAME_CLOUD_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Clouds.COLUMN_NAME_CLOUD_IMAGEURI + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.Clouds.TABLE_NAME;
    private static Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(DATABASE_NAME);
    }

    public int cloudCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = db.rawQuery("SELECT _id FROM " + DatabaseContract.Clouds.TABLE_NAME, null).getCount();
        db.close();
        return count;
    }

    //public boolean addCloud(int id, String name, float rating, String description, int imageId) {
    public boolean addCloud(int id, String name, float rating, String description, String imageUri) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (id != -1) {
            values.put(DatabaseContract.Clouds._ID, id);
        }
        values.put(DatabaseContract.Clouds.COLUMN_NAME_CLOUD_NAME, name);
        values.put(DatabaseContract.Clouds.COLUMN_NAME_CLOUD_RATING, rating);
        values.put(DatabaseContract.Clouds.COLUMN_NAME_CLOUD_DESCRIPTION, description);
        values.put(DatabaseContract.Clouds.COLUMN_NAME_CLOUD_IMAGEURI, imageUri);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Clouds.TABLE_NAME,
                DatabaseContract.Clouds.COLUMN_NAME_NULLABLE,
                values);

        db.close();

        return newRowId != -1;
    }

    //Return all data
    public List<Cloud> getAllClouds() {

        List<Cloud> clouds = new ArrayList<>();

        // Gets the data repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor selectAll = db.rawQuery("SELECT * FROM " + DatabaseContract.Clouds.TABLE_NAME, null);
        while (selectAll.moveToNext()) {

            int cloudId = selectAll.getInt(selectAll.getColumnIndex("_id"));
            String cloudName = selectAll.getString(selectAll.getColumnIndex("name"));
            float cloudRating = selectAll.getFloat(selectAll.getColumnIndex("rating"));
            String cloudDescription = selectAll.getString(selectAll.getColumnIndex("description"));
            String cloudImageUri = selectAll.getString(selectAll.getColumnIndex("imageuri"));

            clouds.add(new Cloud(cloudId, cloudName, cloudRating, cloudDescription, cloudImageUri));

        }

        db.close();

        return clouds;
    }

    public Cloud getCloud(int Id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor getCloud = db.rawQuery("SELECT * FROM " + DatabaseContract.Clouds.TABLE_NAME + " WHERE _id = ?", new String[]{Id + ""});
        Cloud singleCloud = null;

        if (getCloud.getCount() > 0) {

            getCloud.moveToFirst();
            int cloudId = getCloud.getInt(getCloud.getColumnIndex("_id"));
            String cloudName = getCloud.getString(getCloud.getColumnIndex("name"));
            float cloudRating = getCloud.getFloat(getCloud.getColumnIndex("rating"));
            String cloudDescription = getCloud.getString(getCloud.getColumnIndex("description"));
            String cloudImageUri = getCloud.getString(getCloud.getColumnIndex("imageuri"));

            singleCloud = new Cloud(cloudId, cloudName, cloudRating, cloudDescription, cloudImageUri);
        }

        db.close();

        return singleCloud;

    }


    public void deleteCloud(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = DatabaseContract.Clouds._ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        db.delete(DatabaseContract.Clouds.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseContract.Clouds.TABLE_NAME, null, null);

        db.close();
    }
}
