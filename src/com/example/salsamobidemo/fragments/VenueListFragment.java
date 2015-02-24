package com.example.salsamobidemo.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.salsamobidemo.R;
import com.example.salsamobidemo.adapters.VenueAdapter;
import com.example.salsamobidemo.entities.FourSquareVenue;



public class VenueListFragment extends Fragment {
	
	
	private ListView venueList;
	private VenueAdapter venueAdapter;

	public VenueListFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.venue_list, container, false);
		venueList = (ListView) v;
		venueAdapter = new VenueAdapter(getActivity());
		venueList.setAdapter(venueAdapter);
		return v;
    }

	public void populateListView(ArrayList<FourSquareVenue> venues){
		venueAdapter.newData(venues);
	}

}
