package com.bodovix.week5scencorsandlocation;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng timesSquare = new LatLng(40.7589,-73.9851);
        LatLng grandCanyon = new LatLng(36.0544,112.1401);
        LatLng eiffelTower = new LatLng(48.8584,2.2945);
        LatLng empStateBuilding = new LatLng(40.7484,73.9857);

        mMap.addMarker(new MarkerOptions().position(timesSquare).title("Times Square"));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        mMap.addMarker(new MarkerOptions().position(grandCanyon).title("Grand Canyon"));
        mMap.addMarker(new MarkerOptions().position(eiffelTower).title("Eiffel tower"));
        mMap.addMarker(new MarkerOptions().position(empStateBuilding).title("Empire State Building"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(grandCanyon));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);


        //you can draw polygons or circles with maps too
        PolygonOptions polygon = new PolygonOptions();
        polygon.add(new LatLng(51, 0));
        polygon.add(new LatLng(51, 1));
        polygon.add(new LatLng(52, 3));
        polygon.add(new LatLng(50, 3));
        polygon.add(new LatLng(50, 2));
        mMap.addPolygon(polygon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(55, -1)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String title = marker.getTitle();
        switch (marker.getTitle()){
            case "Times Square" :
                intent.setData(Uri.parse("https://www.timessquarenyc.org/"));
                break;
            case "Sydney":
                intent.setData(Uri.parse("https://www.australia.com/en-gb/places/sydney-and-surrounds/guide-to-sydney.html"));
                break;
            case "Grand Canyon":
                intent.setData(Uri.parse("https://grandcanyon.com/"));
                break;
            case "Eiffel tower":
                intent.setData(Uri.parse("https://www.toureiffel.paris/en"));
                break;
            case "Empire State Building":
                intent.setData(Uri.parse("https://www.esbnyc.com/"));
                break;
            case "New Marker":
                Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                search.putExtra(SearchManager.QUERY, marker.getPosition().latitude + " " +marker.getPosition().longitude); // query contains search string
                startActivity(intent);
                break;
        }
        startActivity(intent);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng newMarker = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(newMarker).title("New Marker"));

    }
}
