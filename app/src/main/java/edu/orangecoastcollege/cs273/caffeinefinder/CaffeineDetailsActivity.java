package edu.orangecoastcollege.cs273.caffeinefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Controller for activity_caffeine_detaisl.xml
 * Displays details of selected location
 */
public class CaffeineDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{
    Location mLocation;
    GoogleMap mMap;
    private TextView detailsNameTextView;
    private TextView detailsAddressTextView;
    private TextView detailsPhoneNumberTextView;
    private TextView detailsCoordinatesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_details);
        mLocation = getIntent().getParcelableExtra("Location");
        detailsNameTextView = (TextView) findViewById(R.id.detailsNameTextView);
        detailsAddressTextView = (TextView) findViewById(R.id.detailsAddressTextView);
        detailsPhoneNumberTextView = (TextView) findViewById(R.id.detailsPhoneNumberTextView);
        detailsCoordinatesTextView = (TextView) findViewById(R.id.detailsCoordinatesTextView);

        detailsNameTextView.setText(mLocation.getName());
        detailsAddressTextView.setText(mLocation.getFullAddress());
        detailsPhoneNumberTextView.setText(mLocation.getPhone());

        String latDirection = (mLocation.getLatitude() >= 0.0) ? "N" : "S";
        String longDirection = (mLocation.getLongitude() >= 0.0) ? "E" : "W";
        detailsCoordinatesTextView.setText(Math.abs(mLocation.getLatitude()) + " " + latDirection
                + " " + (Math.abs(mLocation.getLongitude()) + " " + longDirection));

        SupportMapFragment caffeineMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.caffieneDetaislMapFragment);
        caffeineMapFragment.getMapAsync(this);
    }

    /**
     * Readies map
     * @param googleMap google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng coordinates = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(coordinates).title(mLocation.getName()));
        CameraPosition cameraPosition = CameraPosition.builder().target(coordinates).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }
}
