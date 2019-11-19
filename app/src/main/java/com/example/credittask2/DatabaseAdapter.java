package com.example.credittask2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;
public class DatabaseAdapter {
    public static final String DATABASE_NAME = "MyMovie.db";
    public static final String TABLE_NAME = "tblWatchListMovie";
    public static final String LID = "LID";
    public static final String MOVIEID= "MovieID";

    public static final String DATABASE_CREATE = "create table "+ TABLE_NAME + " ("+
            LID +" integer primary key autoincrement, "+
            MOVIEID + " text not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DatabaseAdapter(Context cxt) {
        this.context = cxt;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG,"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS tblTransaction");
            onCreate(db);
        }
    }

    public DatabaseAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertItem(int movieID)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(MOVIEID, movieID);
        return db.insert(TABLE_NAME, null, initialValues);
    }
    public Cursor getAllItems()
    {
        Cursor c = db.query(TABLE_NAME, new String[] {LID, MOVIEID}, null, null, null, null, null);
        if(c != null)
            c.moveToFirst();
        return c;
    }

}