package edu.orangecoastcollege.cs273.caffeinefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Controller for activity_caffeine_list.xml
 * displays list of nearby caffeine souorces
 */
public class CaffeineListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper db;
    private List<Location> allLocationsList;
    private ListView locationsListView;
    private LocationListAdapter locationListAdapter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_list);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importLocationsFromCSV("locations.csv");

        allLocationsList = db.getAllCaffeineLocations();
        locationsListView = (ListView) findViewById(R.id.locationsListView);
        locationListAdapter = new LocationListAdapter(this, R.layout.location_list_item, allLocationsList);
        locationsListView.setAdapter(locationListAdapter);

        // Hook up support map fragment
        SupportMapFragment caffeineMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.caffeineMapFragment);
        caffeineMapFragment.getMapAsync(this);
    }

    /**
     * Gets the selected location and passes it to CaffeineDetailsActivity
     * @param view the view
     */
    public void viewLocationDetails(View view) {
        Location location = (Location) view.getTag();
        Intent detailsIntent = new Intent(this, CaffeineDetailsActivity.class);
        detailsIntent.putExtra("Location", location);
        startActivity(detailsIntent);
    }

    /**
     * Readies map
     * @param googleMap google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // use lat and long to for each location to create a marker on the google map
        mMap = googleMap;
        // loop through each location

        for (Location location : allLocationsList) {
            LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(coordinate).title(location.getName()));
        }

        LatLng currentPosition = new LatLng(33.671028, -117.911305);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPosition).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }
}
