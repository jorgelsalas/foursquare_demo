package com.example.salsamobidemo.asynctasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.salsamobidemo.constants.VenueSearchConstants;
import com.example.salsamobidemo.entities.FourSquareVenue;
import com.example.salsamobidemo.helpers.RestAdapterHelper;
import com.example.salsamobidemo.http.HttpClient;
import com.example.salsamobidemo.interfaces.OnVenueDataFetchCompleted;

public class VenueSearchTask extends AsyncTask <String, Integer, String>{
	
	private ArrayList<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
	private JSONObject jo = null;
	private HttpClient httpHelper;
	private boolean isNull = false;
	private boolean isEmpty = false;
	private boolean hasGeocodeError = false;
	private OnVenueDataFetchCompleted mCallback;
	private static final String LOG_TAG = VenueSearchTask.class.getSimpleName();
	
	public VenueSearchTask(String city, double latitude, double longitude, Activity activity)  {
		httpHelper = new HttpClient(city, latitude, longitude);
		setCallback(activity);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		
		try {
    		jo = httpHelper.getVenuesJSON(VenueSearchConstants.ENDPOINT);
    		ArrayList<FourSquareVenue> venues = RestAdapterHelper.getFourSquareService().searchVenues("40.7,-74", "vegas,NV");
            venues.size();
    		if (jo == null){
    			isNull = true;
    		}
    		else if(jo.getJSONObject(VenueSearchConstants.META_TAG).has(VenueSearchConstants.ERROR_TYPE_TAG)){
    			String error = jo.getJSONObject(VenueSearchConstants.META_TAG).getString(VenueSearchConstants.ERROR_TYPE_TAG);
				if (error.equalsIgnoreCase(VenueSearchConstants.FAILED_GEOCODE_STATUS)){
					hasGeocodeError = true;
				}
    		}
    		else if (!jo.getJSONObject(VenueSearchConstants.RESPONSE_TAG).has(VenueSearchConstants.VENUES_TAG)){
    			isEmpty = true;
    		}
    		else {
    			
				JSONArray responseVenues = jo.getJSONObject(VenueSearchConstants.RESPONSE_TAG).getJSONArray(VenueSearchConstants.VENUES_TAG);
    			
				for (int i = 0; i < responseVenues.length(); i++) {
    				
    					FourSquareVenue venue = new FourSquareVenue();
    					
    					JSONObject joVenue = (JSONObject) responseVenues.get(i);
                        venue.setName(joVenue.getString(VenueSearchConstants.NAME_TAG));
                        venue.setResult_number(i+1);
                        
                        JSONObject location = new JSONObject(joVenue.getString(VenueSearchConstants.LOCATION_TAG));
                    	venue.setLatitude(location.getDouble(VenueSearchConstants.LATITUDE_TAG));
                    	venue.setLongitude(location.getDouble(VenueSearchConstants.LONGITUDE_TAG));
                    	double distanceInKM = (double) location.getInt(VenueSearchConstants.DISTANCE_TAG);
                    	distanceInKM /= 1000.0;
                    	venue.setDistanceToLocation(distanceInKM);
                    	venues.add(venue);
                }
				if (venues.size() == 0){
	    			isEmpty = true;
	    		}
    		}
    		
    	}
    	catch (Exception e){
    		Log.e(LOG_TAG, "Error while retrieving Foursquare data: "+e.toString(), e);
    		
    	}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (isNull){
			mCallback.onDataReceivedFailure();
		}
		else if(isEmpty){
			mCallback.onEmptyDataSetReceived();
		}
		else if(hasGeocodeError){
			mCallback.onDataReceivedWithGeocodeError();
		}
		else{
			mCallback.onDataReceived(venues);
		}
	}
	
	public void setCallback(Activity activity){
		try {
            mCallback = (OnVenueDataFetchCompleted) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnVenueDataFetchCompleted.class.getName());
        }
	}

}
