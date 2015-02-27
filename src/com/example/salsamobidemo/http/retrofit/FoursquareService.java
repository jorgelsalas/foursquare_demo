package com.example.salsamobidemo.http.retrofit;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;

import com.example.salsamobidemo.entities.FourSquareVenue;
import com.example.salsamobidemo.constants.VenueSearchConstants;

public interface FoursquareService {
	
	
	
	
	@GET(VenueSearchConstants.VENUE_SEARCH_SERVICE)
	public ArrayList<FourSquareVenue> searchVenues(
			@Query(VenueSearchConstants.PARAMETER_CLIENT_ID) String client_id,
			@Query(VenueSearchConstants.PARAMETER_CLIENT_SECRET) String client_secret,
			@Query(VenueSearchConstants.PARAMETER_VERSION) String version,
			@Query(VenueSearchConstants.PARAMETER_LAT_LONG) String latlng,
			@Query(VenueSearchConstants.PARAMETER_NEARBY) String city
			);
	
	@GET(VenueSearchConstants.VENUE_SEARCH_SERVICE)
	public ArrayList<FourSquareVenue> searchVenues(
			@Query(VenueSearchConstants.PARAMETER_LAT_LONG) String latlng,
			@Query(VenueSearchConstants.PARAMETER_NEARBY) String city
			);
}
