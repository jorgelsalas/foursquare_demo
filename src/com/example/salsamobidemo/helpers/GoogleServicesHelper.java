package com.example.salsamobidemo.helpers;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class GoogleServicesHelper {
	
	private Activity activity;
	private GoogleApiClient mGoogleApiClient;
	private final static String LOG_TAG = GoogleServicesHelper.class.getSimpleName();

	public GoogleServicesHelper() {
		
	}
	
	public GoogleServicesHelper(Activity activity) {
		this.activity = activity;
		buildGoogleApiClient();
	}
	
	
	protected synchronized void buildGoogleApiClient() {
	    mGoogleApiClient = new GoogleApiClient.Builder(activity)
	        .addConnectionCallbacks((ConnectionCallbacks) activity)
	        .addOnConnectionFailedListener((OnConnectionFailedListener) activity)
	        .addApi(LocationServices.API)
	        .build();
	    mGoogleApiClient.connect();
	}
	
	public Location getLastLocation(){
		Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if(lastLocation == null){
			Log.e(LOG_TAG, "Google Services returned null location");
		}
		return lastLocation;
	}
	
	public boolean isConnectionAvailable(){
		return mGoogleApiClient.isConnected();
	}

}
