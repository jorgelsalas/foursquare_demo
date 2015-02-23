package com.example.salsamobidemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	final private Context mContext;
	
	public DBHelper(Context context) {
		super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DBConstants.CREATE_VENUE_TABLE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	void deleteDatabase(){
		mContext.deleteDatabase(DBConstants.DB_NAME);
	}

}
