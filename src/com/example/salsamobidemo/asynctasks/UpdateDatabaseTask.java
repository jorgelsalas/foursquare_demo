package com.example.salsamobidemo.asynctasks;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.salsamobidemo.database.DBManager;
import com.example.salsamobidemo.entities.FourSquareVenue;

public class UpdateDatabaseTask extends AsyncTask <String, Integer, String>{
	
	private ArrayList<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
	private DBManager dbManager;
	private final static String LOG_TAG = "Update Database Task"; 
	
	public UpdateDatabaseTask(Activity activity, ArrayList<FourSquareVenue> venues)  {
		dbManager = new DBManager(activity);
		this.venues = venues;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		if (venues.size() > 0){
			//Clears preexisting venues from previous search if there are new ones to replace them with
			dbManager.clearVenues();
			
			//We only store 10 venues at most
			if (venues.size() > 10){
				ArrayList<FourSquareVenue> newSet = new ArrayList<FourSquareVenue>();
				for (int i = 0; i < 10 ; i++){
					newSet.add(venues.get(i));
				}
				venues = newSet;
			}
			dbManager.storeVenues(venues);
		}
		else {
			Log.i(LOG_TAG, "There are no venues to store to the database");
		}
		return result;
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		
	}
	
	

}
