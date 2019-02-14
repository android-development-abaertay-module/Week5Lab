package com.bodovix.week5scencorsandlocation;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.location.LocationServices;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int REQUEST_ACCESS_FINE_STATE = 1;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkPermisions();

    }

    @Override
    protected void onStop() {
        //disconnect the api on stop to save battery/data
        googleApiClient.disconnect();
        mLocationManager.removeUpdates(this);
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    Log.d("gs", "granted");
                    //permissions granted
                } else {
                    //Return to main activity
                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void checkPermisions() {
        //if permissions not granted. Request them
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_STATE);

        } else {
            //permissions already granted
        }
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
        LatLng timesSquare = new LatLng(40.7589, -73.9851);
        LatLng grandCanyon = new LatLng(36.0544, 112.1401);
        LatLng eiffelTower = new LatLng(48.8584, 2.2945);
        LatLng empStateBuilding = new LatLng(40.7484, 73.9857);

        mMap.addMarker(new MarkerOptions().position(timesSquare).title("Times Square"));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        mMap.addMarker(new MarkerOptions().position(grandCanyon).title("Grand Canyon"));
        mMap.addMarker(new MarkerOptions().position(eiffelTower).title("Eiffel tower"));
        mMap.addMarker(new MarkerOptions().position(empStateBuilding).title("Empire State Building"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(grandCanyon));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        mMap.setMyLocationEnabled(true);


        //you can draw polygons or circles with maps too
        PolygonOptions polygon = new PolygonOptions();
        polygon.add(new LatLng(51, 0));
        polygon.add(new LatLng(51, 1));
        polygon.add(new LatLng(52, 3));
        polygon.add(new LatLng(50, 3));
        polygon.add(new LatLng(50, 2));
        mMap.addPolygon(polygon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(55, -1)));

        //Geolocation stuff
        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        googleApiClient.connect();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Requesting Location updates from locationManager
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = mLocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(provider, 1000, 0, this);
        } else {
            Toast.makeText(this, "Permission Required!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng newLocation = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(newLocation).title("Current Location");
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));


        //You can also get last known location at any point manually by using: LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
