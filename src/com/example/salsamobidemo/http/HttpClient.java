package com.example.salsamobidemo.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Log;

import com.example.salsamobidemo.constants.VenueSearchConstants;



public class HttpClient {
	
	private final String CHARSET = "UTF-8";
	private final String HTTTP_GET = "GET";
	private String city;
	private double latitude;
	private double longitude;
	
	public HttpClient(String city, double latitude, double longitude){
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		
	}
	
	
	public JSONObject getVenuesJSON(String url){
    	String line = "";
    	JSONObject jo = null;
    	
    	try {
    		String fullUrl = url+"?";
    		fullUrl += VenueSearchConstants.PARAMETER_CLIENT_ID + "=" + VenueSearchConstants.CLIENT_ID;
    		fullUrl += "&" + VenueSearchConstants.PARAMETER_CLIENT_SECRET + "=" + VenueSearchConstants.CLIENT_SECRET;
    		fullUrl += "&" + VenueSearchConstants.PARAMETER_VERSION + "=" + VenueSearchConstants.VERSION_VALUE;
    		fullUrl += "&" + VenueSearchConstants.PARAMETER_LAT_LONG + "=" + String.valueOf(latitude) + "," + String.valueOf(longitude);
    		fullUrl += "&" + VenueSearchConstants.PARAMETER_NEARBY + "=" + city;
    		URLEncoder.encode(fullUrl, CHARSET);
    		Log.i(HttpClient.class.getName(), "URL QUERIED: "+ fullUrl);
    		URL conexion = new URL(fullUrl);
            URLConnection tc = conexion.openConnection();
            tc.setRequestProperty("charset", CHARSET);
            
            ((HttpURLConnection) tc).setRequestMethod(HTTTP_GET);
            BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            
            while ((line = in.readLine()) != null) {
            	jo = new JSONObject(line);
            }
            
            int responseCode = ((HttpURLConnection) tc).getResponseCode();
            System.out.println("Response code: " + responseCode);
            if (responseCode == 200){
            	Log.i("HTTP GET","Succesfully retrieved info");
            }
            else {
            	Log.e("HTTP GET","Something went wrong with the http request");
            }
    	}
    	catch (Exception e){
    		Log.e("log_tag", "Error while connecting to Foursquare: "+e.toString(), e);
    		Log.e("log_tag", "Error message: "+e.getMessage(), e);
    	}
    	
    	return jo;
    }
	
}




