package com.example.salsamobidemo.http.retrofit.request_interceptor;

import com.example.salsamobidemo.constants.VenueSearchConstants;

import retrofit.RequestInterceptor;

public class FourSquareRequestInterceptor implements RequestInterceptor{

	@Override
	public void intercept(RequestFacade request) {
		request.addQueryParam(VenueSearchConstants.PARAMETER_CLIENT_ID, VenueSearchConstants.CLIENT_ID);
		request.addQueryParam(VenueSearchConstants.PARAMETER_CLIENT_SECRET, VenueSearchConstants.CLIENT_SECRET);
		request.addQueryParam(VenueSearchConstants.PARAMETER_VERSION, VenueSearchConstants.VERSION_VALUE);
	}

}
