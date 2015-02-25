package com.example.salsamobidemo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.salsamobidemo.fragments.VenueListFragment;
import com.google.android.gms.maps.SupportMapFragment;

public class TabsAdapter extends FragmentPagerAdapter {
	
	private static final int TAB_COUNT = 2;
	public static final int VENUE_LIST_FRAGMENT_ID = 0;
	public static final int MAP_FRAGMENT_ID = 1;
	
	public TabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case VENUE_LIST_FRAGMENT_ID:
            // List of venues
            return new VenueListFragment();
        case MAP_FRAGMENT_ID:
            // Map fragment
            return new SupportMapFragment();
        }
		return null;
	}
	

	@Override
	public int getCount() {
		return TAB_COUNT;
	}

}
