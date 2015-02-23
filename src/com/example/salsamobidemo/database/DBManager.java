package com.example.salsamobidemo.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.salsamobidemo.entities.FourSquareVenue;

public class DBManager {
	
	private SQLiteDatabase mDB = null;
	private DBHelper mDbHelper;
	private final static String LOG_TAG = "DB MANAGER LOG TAG";
	
	public DBManager(Context context){
		mDbHelper = new DBHelper(context);
		mDB = mDbHelper.getWritableDatabase();
	}
	
	// Delete all venues
	public void clearVenues() {
		mDB.delete(DBConstants.VENUE_TABLE_NAME, null, null);
	}
	
	public void close(){
		mDB.close();
	}
	
	public void deleteDatabase(){
		mDbHelper.deleteDatabase();
	}
	
	//TODO: Remember to consider scenario with no internet connection and no previous
	//search results stored.
	public ArrayList<FourSquareVenue> getStoredVenues(){
		ArrayList<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
		//Querying the database
		Cursor c = mDB.query(
				DBConstants.VENUE_TABLE_NAME,
				new String [] {DBConstants.VENUE_NAME, 
								DBConstants.VENUE_RESULT_NUMBER,
								DBConstants.VENUE_LATITUDE,
								DBConstants.VENUE_LONGITUDE,
								DBConstants.VENUE_DISTANCE}, 
				null, 
				null,
				null,
				null,
				null);
		
		if (c.getCount() == 0){
			Log.e(LOG_TAG,"No venues currently stored in database");
		}
		
		//Iterating over the cursor to populate an ArrayList with venues
		while(c.moveToNext()){
			FourSquareVenue venue = new FourSquareVenue();
			venue.setName(c.getString(0));
			venue.setResult_number(c.getInt(1));
			//TODO: Verify parsing works fine
			venue.setLatitude(c.getDouble(2));
			venue.setLongitude(c.getDouble(3));
			venue.setDistanceToLocation(c.getDouble(4));
			venues.add(venue);
		}
		
		return venues;
	}
	
	public void storeVenues(ArrayList<FourSquareVenue> venues){
		for (FourSquareVenue venue : venues){
			ContentValues values = new ContentValues();
			values.put(DBConstants.VENUE_NAME, venue.getName());
			values.put(DBConstants.VENUE_RESULT_NUMBER, venue.getResult_number());
			values.put(DBConstants.VENUE_LATITUDE, venue.getLatitude());
			values.put(DBConstants.VENUE_LONGITUDE, venue.getLongitude());
			values.put(DBConstants.VENUE_DISTANCE, venue.getDistanceToLocation());
			mDB.insert(DBConstants.VENUE_TABLE_NAME, null, values);
			values.clear();
		}
	}

}
