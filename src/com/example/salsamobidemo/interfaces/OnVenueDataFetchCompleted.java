package com.example.salsamobidemo.interfaces;

import java.util.ArrayList;

import com.example.salsamobidemo.entities.FourSquareVenue;


public interface OnVenueDataFetchCompleted {

	public void onDataReceived(ArrayList<FourSquareVenue> venues);
	public void onDataReceivedFailure();
	public void onEmptyDataSetReceived();
	public void onDataReceivedWithGeocodeError();
	public void onDataReceivedFromLocalDB(ArrayList<FourSquareVenue> venues);

}
