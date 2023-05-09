package org.code.parentsplashscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.ActivityLocationOptionBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import stream.customalert.CustomAlertDialogue;

public class LocationOptionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityLocationOptionBinding locationOptionBinding;
    private MapView mapView;
    private MapboxMap map;
    private Marker marker;
    private Double latitude;
    private Double longitude;
    private String region;
    private LocationManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        locationOptionBinding = DataBindingUtil.setContentView(this, R.layout.activity_location_option);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapView = locationOptionBinding.mapView;
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onMapClick(@NonNull LatLng point) {
                map.clear();
                cameraPosition(point);
                map.addMarker(new MarkerOptions().position(point));
                latitude = point.getLatitude();
                longitude = point.getLongitude();
                region = getAddressInfo(latitude, longitude);
                locationOptionBinding.getLocation.setEnabled(true);
                locationOptionBinding.getLocation.setBackgroundColor(Color.rgb(46, 204, 113));
            }
        });

        /** Button that get real location of parent **/
        locationOptionBinding.getMyLocation.setOnClickListener(view -> {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                initialPosition();
                locationOptionBinding.getLocation.setEnabled(true);
                locationOptionBinding.getLocation.setBackgroundColor(Color.rgb(46, 204, 113));
            } else {
                showGpsDialog();
            }
        });

        /** Function that ensure address of parent **/
        locationOptionBinding.getLocation.setOnClickListener(view -> {
            CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(LocationOptionActivity.this)
                    .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                    .setCancelable(false)
                    .setTitle("Is This Your Address ?")
                    .setMessage(region)
                    .setPositiveText("Yes")
                    .setPositiveColor(R.color.negative)
                    .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                    .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                        @Override
                        public void OnClick(View view, Dialog dialog) {
                            dialog.dismiss();
                            Intent data = new Intent();
                            data.putExtra("latitude", latitude);
                            data.putExtra("longitude", longitude);
                            data.putExtra("region", region);
                            setResult(Activity.RESULT_OK, data);
                            finish();
                            Toast.makeText(LocationOptionActivity.this, "done", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeText("No")
                    .setNegativeColor(R.color.positive)
                    .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                        @Override
                        public void OnClick(View view, Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setDecorView(getWindow().getDecorView())
                    .build();
            alert.show();
        });


    }

    /**
     * Function that get real location of parent
     **/
    @SuppressLint("MissingPermission")
    private void initialPosition() {
        LocationServices.getFusedLocationProviderClient(LocationOptionActivity.this).
                getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }

                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }
                }).addOnSuccessListener(location -> {
            if (location != null) {
                map.clear();
                map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                region = getAddressInfo(location.getLatitude(), location.getLongitude());
                cameraPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    private void cameraPosition(LatLng location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0), 10000);
    }

    /**
     * getting the address info.
     ***/
    private String getAddressInfo(Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(LocationOptionActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("hossam", e.getMessage());
        }

        String address = "";
        String city = "";
        String country = "";
        try {
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryName();
        } catch (Exception e) {
            Toast.makeText(this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        Log.d("hossam", "address is : " + address + " city: " + city + " country: " + country);
        return address;
    }

    /**
     * GPS Dialog that tell user to open gps
     **/
    private void showGpsDialog() {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(LocationOptionActivity.this)
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle("Open GPS")
                .setMessage("Please open your GPS then we can to fetch your location correctly!")
                .setNegativeText("OK")
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getWindow().getDecorView())
                .build();
        alert.show();
    }


    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}