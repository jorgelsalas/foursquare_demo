package com.example.salsamobidemo.interfaces;

import java.util.ArrayList;

import com.example.salsamobidemo.entities.FourSquareVenue;

public interface OnRestoreStateListener {

	public void onListViewRestored(ArrayList<FourSquareVenue> venues);

}
