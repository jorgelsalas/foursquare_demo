package com.example.salsamobidemo.asynctasks;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.salsamobidemo.database.DBManager;
import com.example.salsamobidemo.entities.FourSquareVenue;
import com.example.salsamobidemo.interfaces.OnVenueDataFetchCompleted;

public class LoadVenuesFromDatabaseTask extends AsyncTask <String, Integer, String>{
	
	private ArrayList<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
	private DBManager dbManager;
	private OnVenueDataFetchCompleted mCallback;
	private final static String LOG_TAG = "Load Venues from Database Task"; 
	
	public LoadVenuesFromDatabaseTask(Activity activity)  {
		dbManager = new DBManager(activity);
		setCallback(activity);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		venues = dbManager.getStoredVenues();
		if (venues.size() == 0){
			Log.w(LOG_TAG, "No venues retrieved from DB");
		}
		return result;
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		mCallback.onDataReceivedFromLocalDB(venues);
	}
	
	public void setCallback(Activity activity){
		try {
            mCallback = (OnVenueDataFetchCompleted) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnVenueDataFetchCompleted.class.getName());
        }
	}

}
