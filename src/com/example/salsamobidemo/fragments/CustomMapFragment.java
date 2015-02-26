package com.example.salsamobidemo.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salsamobidemo.entities.SimpleMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;




public class CustomMapFragment extends SupportMapFragment {
	
	
	private GoogleMap map;
	private ArrayList<SimpleMarker> markers = new ArrayList<SimpleMarker>();
	private static final String MARKERS_BUNDLE_KEY = "markers_key";

	public CustomMapFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		map = getMap();
		//Use gson to retrieve venues
		if (savedInstanceState != null){
			String json = savedInstanceState.getString(MARKERS_BUNDLE_KEY);
			if(json != null){
				Type arrayListOfSimpleMarkers = new TypeToken<ArrayList<SimpleMarker>>(){}.getType();
				markers = new Gson().fromJson(json, arrayListOfSimpleMarkers);
				if (markers != null && markers.size() > 0){
					placeMarkers(markers);
				}
			}
		}
		
		return v;
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//Use gson to save markers
		super.onSaveInstanceState(outState);
		if (markers.size() > 0){
			String json = new Gson().toJson(markers);
			outState.putString(MARKERS_BUNDLE_KEY, (json));
		}
		super.onSaveInstanceState(outState);
	}
	
	public void setMarkerList(ArrayList<SimpleMarker> newMarkers){
		markers = newMarkers;
	}
	
	private void placeMarkers(ArrayList<SimpleMarker> newMarkers){
		for(SimpleMarker marker : newMarkers){
			map.addMarker(new MarkerOptions()
							.position(marker.getPosition())
							.title(marker.getTitle()));
		}
	}
	
}
