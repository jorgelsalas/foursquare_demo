package com.example.salsamobidemo.helpers;

import com.example.salsamobidemo.constants.VenueSearchConstants;
import com.example.salsamobidemo.http.retrofit.FoursquareService;
import com.example.salsamobidemo.http.retrofit.request_interceptor.FourSquareRequestInterceptor;

import retrofit.RestAdapter;

public class RestAdapterHelper {

	public RestAdapterHelper() {
		
	}
	
	public static RestAdapter getRestAdapter(){
		RestAdapter restAdapter = new RestAdapter.Builder()
			.setEndpoint(VenueSearchConstants.FOURSQUARE_SERVICE_ADDRESS_FOR_RETROFIT)
			.setLogLevel(RestAdapter.LogLevel.FULL)
			.setRequestInterceptor(new FourSquareRequestInterceptor())
			.build();
				
		return restAdapter;
	}
	
	public static FoursquareService getFourSquareService(){
		return getRestAdapter().create(FoursquareService.class);
	}

}
