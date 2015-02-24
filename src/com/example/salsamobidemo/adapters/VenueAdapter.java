package com.example.salsamobidemo.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.salsamobidemo.R;
import com.example.salsamobidemo.entities.FourSquareVenue;
import com.example.salsamobidemo.interfaces.OnVenueClickListener;

public class VenueAdapter extends BaseAdapter implements ListAdapter{
	
	private Activity activity;
	private ArrayList<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
	private OnVenueClickListener mCallback;

	public VenueAdapter() {
		
	}
	
	public VenueAdapter(Activity context) {
		this.activity = context;
		setCallback(activity);
	}
	
	public void newData(ArrayList<FourSquareVenue> data){
		venues = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return venues.size();
	}

	@Override
	public Object getItem(int position) {
		return venues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FourSquareVenue venue = venues.get(position);
		LinearLayout venue_row = (LinearLayout) convertView;
        if (venue_row == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            venue_row = (LinearLayout) inflater.inflate(R.layout.venue_list_row, parent, false);
        }
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/PTS76F.ttf");
        TextView temp = (TextView)venue_row.findViewById(R.id.venue_name_textView);
        temp.setText(activity.getString(R.string.venue_row_name_label) + venue.getName());
        temp.setTypeface(typeface);
        temp = (TextView)venue_row.findViewById(R.id.distance_textView);
        temp.setText(activity.getString(R.string.distance_row_label) + venue.getDistanceToLocation());
        temp.setTypeface(typeface);
        
        venue_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onVenueClicked(venue);
			}
		});
		return venue_row;
	}
	
	public void setCallback(Activity activity){
		try {
            mCallback = (OnVenueClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnVenueClickListener.class.getName());
        }
	}

}
