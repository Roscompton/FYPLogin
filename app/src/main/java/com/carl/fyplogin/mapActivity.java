package com.carl.fyplogin;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class mapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {


private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();




        if(mMap != null) {
            mMap.setMyLocationEnabled(true);
        }


    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        UiSettings mapSettings;
        mapSettings = mMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);

        //Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        //Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        //Get current location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();

        LatLng myCoordinates = new LatLng(latitude, longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 12);
        mMap.animateCamera(yourLocation);

        markerSetup();

       double distanceToMarker = getDistance(latitude,longitude);


        mMap.setOnMarkerClickListener(this);

    }


    private void markerSetup() {

        ArrayList<Double> getLat, getLng;
        ArrayList<String> getOwner;
        ArrayList<Integer> getID, getSSID, getSectors;
        getLat = SplashScreen.getLatitude();
        getLng = SplashScreen.getLongitude();
        getID = SplashScreen.getID();
        getOwner = SplashScreen.getOwner();
        getSectors = SplashScreen.getSectors();
        getSSID = SplashScreen.getSSID();

        for(int i=0; i<getLat.size();i++) {

            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(getLat.get(i), getLng.get(i)))
            .title("ID: " + getID.get(i))
            .snippet("SSID: " + getSSID.get(i) + "\nOwner: " + getOwner.get(i) + "\nSectors: " + getSectors.get(i)));


        }


    }
    @Override
    public boolean onMarkerClick(final Marker marker) {

       return false;
    }

    private double getDistance(double lat2, double lng2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(locationA.getLatitude());
        locationA.setLongitude(locationA.getLongitude());

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        double distance = locationA.distanceTo(locationB);

        Log.v("log", "distance " +distance);

        return distance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }



   }

