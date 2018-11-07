package com.qteam.saigonjams.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.qteam.saigonjams.R;
import com.qteam.saigonjams.activity.MainActivity;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int LOCATION_REQUEST = 2;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    PendingResult<LocationSettingsResult> result;

    public MapFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_map, container, false);

         if (MainActivity.mainNav.getVisibility() != View.VISIBLE)
             MainActivity.mainNav.setVisibility(View.VISIBLE);

         MapView mapView = view.findViewById(R.id.mapView);
         if (mapView != null) {
             mapView.onCreate(null);
             mapView.onResume();
             mapView.getMapAsync(this);
         }
         return view;
    }

    @Override
    public void onMapReady(GoogleMap ggMap) {
        googleMap = ggMap;

        checkLocationPermission();

        googleMap.setTrafficEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void checkLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(getContext(), permission[0]) == PackageManager.PERMISSION_GRANTED) {
            showDefaultLocation();
            googleMap.setMyLocationEnabled(true);
            requestLocationSettings();
        }
        else
            requestPermissions(permission, LOCATION_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                showDefaultLocation();
                requestLocationSettings();
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    googleMap.setMyLocationEnabled(true);
            }
            else
                showDefaultLocation();
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            googleMap.setMinZoomPreference(15);
            return false;
        }
    };

    private void showDefaultLocation() {
        LatLng defaultLocation = new LatLng(10.849029, 106.774181);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void requestLocationSettings() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), LOCATION_REQUEST);
                        }
                        catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        Toast.makeText(getActivity(), "Đã bật GPS!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        Toast.makeText(getActivity(), "Từ chối bật GPS!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}