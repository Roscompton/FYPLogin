package com.carl.fyplogin;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.carl.fyplogin.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;


public class mapActivity extends FragmentActivity {

    private static final String TAG_SSID = "SSID: ";
    private static final String TAG_ID = "ID: ";
    private static final String TAG_SECTORS = "Number of Sectors: ";
    private static String url = "http://danu6.it.nuigalway.ie/wbbsl/listview.php/";
    private GoogleMap mMap;
    ListView list;
    TextView SSID;
    TextView ID;
    TextView Sectors;



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
            mMap.addMarker(new MarkerOptions()
                .position(new LatLng(53.2719, -9.0489))
                .title("Wireless Base Station"));
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

       double distanceToMarker = getDistance(latitude,longitude);
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


    public void populateList() {


    }

   }