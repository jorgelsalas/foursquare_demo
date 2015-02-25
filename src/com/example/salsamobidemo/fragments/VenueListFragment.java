package com.example.salsamobidemo.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.salsamobidemo.R;
import com.example.salsamobidemo.adapters.VenueAdapter;
import com.example.salsamobidemo.entities.FourSquareVenue;
import com.example.salsamobidemo.interfaces.OnRestoreStateListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class VenueListFragment extends Fragment {
	
	
	private ListView venueList;
	private VenueAdapter venueAdapter;
	private OnRestoreStateListener mCallback;
	private static final String VENUES_BUNDLE_KEY = "venues_key";

	public VenueListFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.venue_list, container, false);
		venueList = (ListView) v;
		venueAdapter = new VenueAdapter(getActivity());
		venueList.setAdapter(venueAdapter);
		setCallback(getActivity());
		//Use gson to retrieve venues
		if (savedInstanceState != null){
			String json = savedInstanceState.getString(VENUES_BUNDLE_KEY);
			if(json != null){
				Type arrayListOfVenues = new TypeToken<ArrayList<FourSquareVenue>>(){}.getType();
				ArrayList<FourSquareVenue> venues = new Gson().fromJson(json, arrayListOfVenues);
				if (venues != null){
					venueAdapter.newData(venues);
					//This is called to repopulate markers in map
					mCallback.onListViewRestored(venues);
				}
			}
		}
		
		return v;
    }

	public void populateListView(ArrayList<FourSquareVenue> venues){
		venueAdapter.newData(venues);
	}
	
	public VenueAdapter getAdapter(){
		return venueAdapter;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//Use gson to save venues
		super.onSaveInstanceState(outState);
		if (venueAdapter != null && venueAdapter.getItems() != null && venueAdapter.getItems().size() > 0){
			String json = new Gson().toJson(venueAdapter.getItems());
			outState.putString(VENUES_BUNDLE_KEY, (json));
		}
		super.onSaveInstanceState(outState);
	}
	
	
	public void setCallback(Activity activity){
		try {
            mCallback = (OnRestoreStateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnRestoreStateListener.class.getName());
        }
	}
	
	
}
