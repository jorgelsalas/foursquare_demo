package com.example.salsamobidemo.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.salsamobidemo.R;
import com.example.salsamobidemo.adapters.TabsAdapter;
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
import com.example.salsamobidemo.interfaces.OnViewPagerFragmentUpdated;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@SuppressWarnings("deprecation")
public class Home extends ActionBarActivity implements OnVenueClickListener, OnVenueDataFetchCompleted, 
						ConnectionCallbacks ,OnConnectionFailedListener, OnMapReadyCallback, TabListener, 
						OnViewPagerFragmentUpdated {
	
	private final String LOG_TAG = "Home Activity";
	private GoogleServicesHelper googleServicesHelper;
	private VenueListFragment venueFragment = null;
	private SupportMapFragment mapFragment;
	private GoogleMap map = null;
	private Location lastLocation = null;
	private LocationManagerHelper locationManagerHelper;
	private EditText searchText;
	private Button searchButton;
	private ViewPager viewPager = null;
    private TabsAdapter mAdapter = null;
    private ActionBar actionBar = null;
    private ArrayList<FourSquareVenue> mVenues = null;
    private static final String VENUES_BUNDLE_KEY = "venues_key";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mapUIComponents();
        setListeners();
        FragmentManager fragmentManager = getSupportFragmentManager();
        venueFragment = (VenueListFragment) fragmentManager.findFragmentById(R.id.venue_list_fragment);
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        ///* ViewPager Handling
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsAdapter(fragmentManager, this);
        if ((viewPager != null) && (mAdapter != null)){
        	//This code will only execute on phones (ie: devices not considered as having a large display)
        	viewPager.setAdapter(mAdapter);
        	actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.addTab(actionBar.newTab().setText(R.string.venue_list_tab_title).setTabListener(this));
            actionBar.addTab(actionBar.newTab().setText(R.string.venue_map_tab_title).setTabListener(this));
            setPageChangeListener();
            venueFragment = (VenueListFragment) ((TabsAdapter) viewPager.getAdapter()).getFragmentReference(TabsAdapter.VENUE_LIST_FRAGMENT_ID);
            mapFragment = (SupportMapFragment) ((TabsAdapter) viewPager.getAdapter()).getFragmentReference(TabsAdapter.MAP_FRAGMENT_ID);
            
            //Phones will only work in portrait mode
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (viewPager == null) {
        	//Only hide it in tablets, which won't have a viewPager
        	hideActionBar();
        }
        //End ViewPager Handling*/
        
        if (mapFragment != null){
        	mapFragment.getMapAsync(this);
        }
        googleServicesHelper = new GoogleServicesHelper(this);
        locationManagerHelper = new LocationManagerHelper(this);
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
    
    //TODO: Update to handle viewPager
    public void bindData(ArrayList<FourSquareVenue> venues){
    	//ViewPager handling
    	if (venueFragment == null && viewPager != null){
    		venueFragment = (VenueListFragment) ((TabsAdapter) viewPager.getAdapter()).getFragmentReference(TabsAdapter.VENUE_LIST_FRAGMENT_ID);
    	}
    	//END viewPager handling
    	if (venueFragment != null){
    		venueFragment.populateListView(venues);
    	}
    	else{
    		Log.e(LOG_TAG, "venue list fragment is not available");
    	}
    }

    //TODO: Update to handle viewPager
	@Override
	public void onVenueClicked(FourSquareVenue venue) {
		//ViewPager handling
    	if (mapFragment == null && viewPager != null){
    		mapFragment = (SupportMapFragment) ((TabsAdapter) viewPager.getAdapter()).getFragmentReference(TabsAdapter.MAP_FRAGMENT_ID);
    		map = mapFragment.getMap();
    	}
    	//END viewPager handling
		if (mapFragment != null && map != null){
			//We zoom to the corresponding marker
			LatLng latLng = new LatLng(venue.getLatitude(), venue.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			map.animateCamera(CameraUpdateFactory.zoomTo(14));
			if (viewPager != null){
				viewPager.setCurrentItem(1);
			}
		}
		else {
			Log.e(LOG_TAG, getString(R.string.map_not_available_warning));
		}
	}


	@Override
	public void onDataReceived(ArrayList<FourSquareVenue> venues) {
		bindData(venues);
		new UpdateDatabaseTask(this, venues).execute("");
		addVenuesToMap(venues);
		// Update local venues to keep on orientation change
		mVenues = venues;
	}


	@Override
	public void onDataReceivedFromLocalDB(ArrayList<FourSquareVenue> venues) {
		bindData(venues);
		addVenuesToMap(venues);
		if(venues.size() == 0){
			//Notify user that local DB had no venues stored
			Toast.makeText(this, R.string.no_venues_retrieved_from_local_warning, Toast.LENGTH_LONG).show();
		}
		// Update local venues to keep on orientation change
		mVenues = venues;
	}
	
	//First checks to verify if map is available, then adds markers
	//TODO: Update to handle viewPager
	public void addVenuesToMap(ArrayList<FourSquareVenue> venues){
		//ViewPager handling
    	if (map == null && viewPager != null){
    		mapFragment = (SupportMapFragment) ((TabsAdapter) viewPager.getAdapter()).getFragmentReference(TabsAdapter.MAP_FRAGMENT_ID);
    		if (mapFragment != null){
    			map = mapFragment.getMap();
    		}
    	}
    	//END viewPager handling
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
		else {
			Log.e(LOG_TAG, getString(R.string.map_not_available_warning));
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
		if (mVenues != null){
			addVenuesToMap(mVenues);
		}
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
		//Hide keyboard
		InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	    
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
	

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPageChangeListener(){
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
	}

	@Override
	public void onNewVenuesFragment(VenueListFragment venueListFragment) {
		this.venueFragment = venueListFragment;
	}

	@Override
	public void onNewMapFragment(SupportMapFragment mapFragment) {
		this.mapFragment = mapFragment;
		if (this.mapFragment != null){
			this.mapFragment.getMapAsync(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		googleServicesHelper.Disconnect();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		googleServicesHelper.Disconnect();
	}

	@Override
	protected void onResume() {
		super.onResume();
		googleServicesHelper.connect();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//Use gson to save venues
		
		super.onSaveInstanceState(outState);
		if (mVenues != null){
			String json = new Gson().toJson(mVenues);
			outState.putString(VENUES_BUNDLE_KEY, (json));
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//Use gson to retrieve venues
		if (savedInstanceState != null){
			String json = savedInstanceState.getString(VENUES_BUNDLE_KEY);
			Type arrayListOfVenues = new TypeToken<ArrayList<FourSquareVenue>>(){}.getType();
			mVenues = new Gson().fromJson(json, arrayListOfVenues);
			
			
			if (mVenues != null) {
				// Apply UI changes to recover state
				bindData(mVenues);
				addVenuesToMap(mVenues);
			}
		}
	}
	
	
}
