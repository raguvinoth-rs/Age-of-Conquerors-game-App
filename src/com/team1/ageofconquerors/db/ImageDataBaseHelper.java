package com.team1.ageofconquerors.db;


import java.util.ArrayList;
import java.util.List;

import com.team1.ageofconquerors.units.Images;
import com.team1.ageofconquerors.util.SystemUiHider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * DB Helper class for all game attributes
 */

public class ImageDataBaseHelper extends SQLiteOpenHelper{

	 public static final int DATABASE_VERSION = 1;
	 public static final String DATABASE_NAME = "imagemanager";
	 public static final String TABLE_IMAGES = "images";
	 
	 //columns of the table IMAGES
	 public static final String KEY_ID = "id"; //primary key
	 public static final String KEY_NAME = "name";
	 public static final String UNIT_TYPE  = "units";
	 
	 public ImageDataBaseHelper(Context context){
		 super(context,DATABASE_NAME,null,DATABASE_VERSION);
	 }
	 
	 // Creating Tables
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
	                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
	                + UNIT_TYPE + " TEXT" + ")";
	        db.execSQL(CREATE_IMAGES_TABLE);
	    }
	 // Upgrading database
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
	 
	        // Create tables again
	        onCreate(db);
	    }
	    
	    // method to insert image into database
	   public void addimages(Images image){
		   SQLiteDatabase db = this.getWritableDatabase();
		   ContentValues values = new ContentValues();
		   values.put(KEY_ID,image.getID());
		   values.put(KEY_NAME,image.getName()); // image name
		   values.put(UNIT_TYPE,image.getUnit());//image unit
		   
		   //insert row
		   db.insert(TABLE_IMAGES,null,values);
		   //closing database connection
		   db.close();
	   }
	   
	   //get single image 
	   public Images getImage(String name){
		   SQLiteDatabase db = this.getReadableDatabase();
		   Cursor cursor = db.query(TABLE_IMAGES, new String[]{KEY_ID,KEY_NAME,UNIT_TYPE},KEY_NAME + "=?",
				   new String[]{name},null,null,null,null);
		   // at least one record retrieved
		   Images image = new Images();
		   if (cursor != null && cursor.moveToFirst())
		   { //move to first retrieved record
		   
		   image = new Images(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		  
}
            return image;
	   }
	   
	   // get all the images
	   public List<Images> getImagesByUnit(String unit){
		   //create imagelist to capture all the images from db
		   List<Images> imagelist = new ArrayList<Images>();
		   
		   //Select query
		   String select = "SELECT * FROM " + TABLE_IMAGES + " WHERE " + UNIT_TYPE + " = '" + unit + "'" ;
		   SQLiteDatabase db = this.getReadableDatabase();
		   Cursor cursor = db.rawQuery(select,null);
		  // Cursor cursor = db.query(TABLE_IMAGES, new String[]{KEY_ID,KEY_NAME,UNIT_TYPE},UNIT_TYPE + "=?",
		//		   new String[]{String.valueOf(unit)},null,null,null,null);
		   // loop through all the retrieved data rows and add to list
		   if (cursor != null) {
		   if(cursor.moveToFirst()){
			   do{
				   Images image = new Images();
				   image.setID(Integer.parseInt(cursor.getString(0)));
				   image.setName(cursor.getString(1));
				   image.setUnit(cursor.getString(2));
				   // add to image list
				   imagelist.add(image);
			   }while(cursor.moveToNext());
		   }
	     }
		   // close database connection
		   db.close();
		   
		   return imagelist;
	   }	   
	   
}

