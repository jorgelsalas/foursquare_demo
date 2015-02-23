package com.example.salsamobidemo.activities;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.salsamobidemo.R;
import com.example.salsamobidemo.asynctasks.LoadVenuesFromDatabaseTask;
import com.example.salsamobidemo.asynctasks.UpdateDatabaseTask;
import com.example.salsamobidemo.asynctasks.VenueSearchTask;
import com.example.salsamobidemo.entities.FourSquareVenue;
import com.example.salsamobidemo.fragments.VenueListFragment;
import com.example.salsamobidemo.helpers.GoogleServicesHelper;
import com.example.salsamobidemo.helpers.InternetHelper;
import com.example.salsamobidemo.helpers.LocationManagerHelper;
import com.example.salsamobidemo.interfaces.OnVenueClickListener;
import com.example.salsamobidemo.interfaces.OnVenueDataFetchCompleted;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Home extends ActionBarActivity implements OnVenueClickListener, OnVenueDataFetchCompleted, 
														ConnectionCallbacks ,OnConnectionFailedListener,
														OnMapReadyCallback {
	
	private final String LOG_TAG = "Home Activity";
	private GoogleServicesHelper googleServicesHelper;
	private VenueListFragment venueFragment = null;
	private SupportMapFragment mapFragment;
	private GoogleMap map = null;
	private Location lastLocation = null;
	private LocationManagerHelper locationManagerHelper;
	private EditText searchText;
	private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        hideActionBar();
        mapUIComponents();
        setListeners();
        FragmentManager fragmentManager = getSupportFragmentManager();
        venueFragment = (VenueListFragment) fragmentManager.findFragmentById(R.id.venue_list_fragment);
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleServicesHelper = new GoogleServicesHelper(this);
        locationManagerHelper = new LocationManagerHelper(this);
        
        /*
        //InternetHelper.verifyInternetAccess(this);
        //new VenueSearchTask("Chicago,IL", 40.7,-74, this).execute("");
        //new LoadVenuesFromDatabaseTask(this).execute("");
        lastLocation = locationManagerHelper.getLastKnownLocation();
        if (lastLocation == null){
        	Toast.makeText(this, "Loc manager gave null location", Toast.LENGTH_LONG).show();
        }
        else {
        	//Toast.makeText(this, "Location: lat :" + lastLocation.getLatitude() + " long: " + lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
        	new VenueSearchTask("Chicago,ILL", lastLocation.getLatitude(),lastLocation.getLongitude(), this).execute("");
        }
        //*/
    }
    
    //Hide the ActionBar, if the API is available.
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void hideActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().hide();
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void bindData(ArrayList<FourSquareVenue> venues){
    	if (venueFragment != null){
    		venueFragment.populateListView(venues);
    	}
    	else{
    		Log.e(LOG_TAG, "venue list fragment is not available");
    	}
    }


	@Override
	public void onVenueClicked(FourSquareVenue venue) {
		if (mapFragment != null && map != null){
			//We zoom to the corresponding marker
			LatLng latLng = new LatLng(venue.getLatitude(), venue.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			map.animateCamera(CameraUpdateFactory.zoomTo(14));
		}
	}


	@Override
	public void onDataReceived(ArrayList<FourSquareVenue> venues) {
		bindData(venues);
		new UpdateDatabaseTask(this, venues).execute("");
		addVenuesToMap(venues);
	}


	@Override
	public void onDataReceivedFromLocalDB(ArrayList<FourSquareVenue> venues) {
		bindData(venues);
		addVenuesToMap(venues);
		if(venues.size() == 0){
			//Notify user that local DB had no venues stored
			Toast.makeText(this, R.string.no_venues_retrieved_from_local_warning, Toast.LENGTH_LONG).show();
		}
	}
	
	//First checks to verify if map is available, then adds markers
	public void addVenuesToMap(ArrayList<FourSquareVenue> venues){
		if (map != null){
			for (FourSquareVenue venue : venues){
				map.addMarker(new MarkerOptions()
						.position(new LatLng(venue.getLatitude(), venue.getLongitude()))
						.title(venue.getName()));
			}
			if (venues.size() > 0){
				LatLng latLng = new LatLng(venues.get(0).getLatitude(), venues.get(0).getLongitude());
				map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				map.animateCamera(CameraUpdateFactory.zoomTo(10));
			}
		}
	}


	@Override
	public void onConnected(Bundle arg0) {
		Log.i(LOG_TAG, getString(R.string.connected_to_google_services));
	}
	
	@Override
	public void onConnectionSuspended(int arg0) {
		Log.e(LOG_TAG, getString(R.string.connection_to_google_services_suspended));
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.e(LOG_TAG, getString(R.string.connection_to_google_services_failed));
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
	}
	
	public void mapUIComponents(){
		searchText = (EditText) findViewById(R.id.city_input_text);
		searchButton = (Button) findViewById(R.id.search_button);
	}
	
	public void setListeners(){
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchVenues();
			}
		});
	}
	
	public void searchVenues(){
		if (InternetHelper.verifyInternetAccess(this)){
			//We have Internet connection and can perform a search
			String city = searchText.getText().toString().trim();
			if (!city.equalsIgnoreCase("")){
				//User has provided input so we get the devices location
				lastLocation = locationManagerHelper.getLastKnownLocation();
				if (lastLocation == null && googleServicesHelper.isConnectionAvailable()){
					//Location Manager could not acquire last location
					//Falling back to google services
					lastLocation = googleServicesHelper.getLastLocation();
				}
				//Verify if still null after querying google services
				if (lastLocation != null){
					//We have a location so we can query FourSquare servers!
					new VenueSearchTask(city, lastLocation.getLatitude(),lastLocation.getLongitude(), this).execute("");
				}
				else {
					//Location still null, so we warn the user
					
					Toast.makeText(this, R.string.location_not_acquired_warning, Toast.LENGTH_LONG).show();
					Toast.makeText(this, R.string.verify_gps_and_network_warning, Toast.LENGTH_LONG).show();
				}
			}
			else {
				//User has left field blank so we ask to him to input data
				Toast.makeText(this, R.string.empty_search_text_warning, Toast.LENGTH_LONG).show();
			}
		}
		else {
			//We don't have Internet so we search our local DB
			Toast.makeText(this, R.string.internet_not_available_warning, Toast.LENGTH_LONG).show();
			new LoadVenuesFromDatabaseTask(this).execute("");
			searchText.setText("");
		}
	}

	@Override
	public void onDataReceivedFailure() {
		//Something went wrong while querying FourSquare servers
		Toast.makeText(this, R.string.foursquare_error_warning, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDataReceivedWithGeocodeError() {
		//The city name supplied was not valid
		Toast.makeText(this, R.string.geocode_error_warning, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onEmptyDataSetReceived() {
		//Notify user that no venues were retrieved
		Toast.makeText(this, R.string.no_venues_retrieved_from_web_warning, Toast.LENGTH_LONG).show();
	}
	
	
	
}
