package com.example.wifischeduler;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperations extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WiFiScheduler.db";
    private SQLiteDatabase db;

    public DatabaseOperations(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS scheduler (id INTEGER PRIMARY KEY, schedulerStatus tinyint(1), time varchar(255) default null) ");
        ContentValues values = new ContentValues();
        values.put("schedulerStatus", 0);
        values.put("time", MainActivity.touch);
        int d = (int) db.insert("scheduler", null , values);
        
        WiFiOperations.schedulerStatus = false;
		WiFiOperations.time = MainActivity.touch;
        
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
    	db.execSQL("DROP TABLE IF EXISTS scheduler");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    public static boolean getSchedulerState(){
    	return WiFiOperations.schedulerStatus;
    }
    
    public static String getTime(){
    	return WiFiOperations.time;
    }
    
    public void getData(){
       
    	Cursor cursor = getReadableDatabase().rawQuery("select * from scheduler where id = ? ", new String[] {"1"});
		cursor.moveToFirst();
		String status = cursor.getString(cursor.getColumnIndexOrThrow("schedulerStatus"));
		String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
		 
		if(status.equalsIgnoreCase("1"))
			WiFiOperations.schedulerStatus = true;
		else 
			WiFiOperations.schedulerStatus = false;
		
		WiFiOperations.time = time;
   
    }
    
    public void updateValues(int status, String time){
    	db = getReadableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("schedulerStatus", status);
    	values.put("time", time);
    	int rows = db.update("scheduler", values, "id = ?", new String[] {"1"});
    	getData();
    }
	  
}
