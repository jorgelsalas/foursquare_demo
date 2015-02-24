package com.example.salsamobidemo.interfaces;

import com.example.salsamobidemo.fragments.VenueListFragment;
import com.google.android.gms.maps.SupportMapFragment;


//Used to update fragment references inside activity when the TabsAdapter creates new fragments
public interface OnViewPagerFragmentUpdated {

	public void onNewVenuesFragment(VenueListFragment venueListFragment);
	public void onNewMapFragment(SupportMapFragment mapFragment);

}
