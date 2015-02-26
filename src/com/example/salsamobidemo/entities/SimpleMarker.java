package com.example.salsamobidemo.entities;

import com.google.android.gms.maps.model.LatLng;

public class SimpleMarker {
	
	public SimpleMarker(LatLng position, String title) {
		super();
		this.position = position;
		this.title = title;
	}

	private LatLng position;
	private String title;

	public SimpleMarker() {
		// TODO Auto-generated constructor stub
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
