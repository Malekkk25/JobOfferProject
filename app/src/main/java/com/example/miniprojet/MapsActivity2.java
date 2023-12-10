package com.example.miniprojet;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap gMap;
    private FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        map = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng latLng = new LatLng(36.8065, 10.1815);
        gMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        gMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        double clickedLatitude = latLng.latitude;
        double clickedLongitude = latLng.longitude;

        try {
            Geocoder geocoder = new Geocoder(MapsActivity2.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(clickedLatitude, clickedLongitude, 1);
            String address, locality, adminArea, country;

            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                locality = addresses.get(0).getLocality();
                adminArea = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();

                Toast.makeText(this, "Address: " + address, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivity2.this, InsertJobPostActivity.class);
                intent.putExtra("address", address);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Toast.makeText(this, "Error retrieving address information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        showConfirmationDialog(clickedLatitude, clickedLongitude);
    }

    private void showConfirmationDialog(double latitude, double longitude) {
        // Implement your confirmation dialog logic here
        // You can use latitude and longitude in this dialog
    }
}
