package com.example.miniprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    GoogleMap gMap;
    FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        map = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        @Override
        public void onMapReady(@NonNull  GoogleMap googleMap;googleMap) {
            this.gMap = googleMap;
            LatLng latLng = new LatLng(36.8065, 10.1815);
            this.gMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            this.gMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(latLng));

            this.gMap.setOnMapClickListener(this);



        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {
            super.onPointerCaptureChanged(hasCapture);
        }
        @Override
        public void onMapClick(LatLng latLng) {
            System.out.println("onMapClick");


            double clickedLatitude = latLng.latitude;
            double clickedLongitude = latLng.longitude;


            try {
                Geocoder geocoder = new Geocoder(Maps.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(clickedLatitude, clickedLongitude, 1);
                address = addresses.get(0).getAddressLine(0);
                localite = addresses.get(0).getLocality();
                city = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                String postalcode = addresses.get(0).getPostalCode();
                String knownname = addresses.get(0).getFeatureName();

                Toast.makeText(this, "" + address , Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // Handle the exception here
                Toast.makeText(this, "Error retrieving address information: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            showConfirmationDialog(clickedLatitude, clickedLongitude);

        }