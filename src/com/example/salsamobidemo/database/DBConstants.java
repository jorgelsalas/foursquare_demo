package com.example.salsamobidemo.database;

public final class DBConstants {
	
	public static final String DB_NAME = "VenuesDB";
	public static final Integer DB_VERSION = 1;
	
	public static final String VENUE_TABLE_NAME = "venues";
	public static final String VENUE_ID = "id_venue";
	public static final String VENUE_RESULT_NUMBER = "result_number";
	public static final String VENUE_NAME = "name";
	public static final String VENUE_LATITUDE = "latitude";
	public static final String VENUE_LONGITUDE = "longitude";
	public static final String VENUE_DISTANCE = "distance";
	
	
	public static final String CREATE_VENUE_TABLE_CMD =

			"CREATE TABLE " + VENUE_TABLE_NAME + " (" 
					+ VENUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ VENUE_RESULT_NUMBER + " INTEGER NOT NULL, "
					+ VENUE_NAME + " TEXT NOT NULL, "
					+ VENUE_LATITUDE + " REAL NOT NULL, "
					+ VENUE_LONGITUDE + " REAL NOT NULL, "
					+ VENUE_DISTANCE + " REAL NOT NULL)";


}
