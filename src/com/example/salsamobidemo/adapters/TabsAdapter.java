package com.example.salsamobidemo.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.salsamobidemo.fragments.VenueListFragment;
import com.example.salsamobidemo.interfaces.OnViewPagerFragmentUpdated;
import com.google.android.gms.maps.SupportMapFragment;

public class TabsAdapter extends FragmentPagerAdapter {
	
	private static final int TAB_COUNT = 2;
	public static final int VENUE_LIST_FRAGMENT_ID = 0;
	public static final int MAP_FRAGMENT_ID = 1;
	private OnViewPagerFragmentUpdated mCallback;
	
	private static SparseArray<Fragment> storedFragments = new SparseArray<Fragment>();
	
	public TabsAdapter(FragmentManager fm, Activity activity) {
		super(fm);
		setCallback(activity);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case VENUE_LIST_FRAGMENT_ID:
            // List of venues
        	VenueListFragment venues = (VenueListFragment) getFragmentReference(index);
        	if (venues == null){
        		venues = new VenueListFragment();
        		storedFragments.put(index, venues);
        	}
        	mCallback.onNewVenuesFragment((VenueListFragment) getFragmentReference(index));
            return getFragmentReference(index);
        case MAP_FRAGMENT_ID:
            // Map fragment
        	SupportMapFragment map = (SupportMapFragment) getFragmentReference(index);
        	if(map == null){
        		map = new SupportMapFragment();
        		storedFragments.put(index, map);
        	}
        	mCallback.onNewMapFragment((SupportMapFragment) getFragmentReference(index));
        	
        	/* Old method
        	SupportMapFragment map = new SupportMapFragment();
        	storedFragments.put(index, map);
        	mCallback.onNewMapFragment((SupportMapFragment) getFragmentReference(index));
        	*/
        	
            return getFragmentReference(index);
        }
		return null;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		storedFragments.remove(position);
		super.destroyItem(container, position, object);
	}
	
	public Fragment getFragmentReference(int index){
		return storedFragments.get(index);
	}
	

	@Override
	public int getCount() {
		return TAB_COUNT;
	}
	
	public void setCallback(Activity activity){
		try {
            mCallback = (OnViewPagerFragmentUpdated) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnViewPagerFragmentUpdated.class.getName());
        }
	}

}
