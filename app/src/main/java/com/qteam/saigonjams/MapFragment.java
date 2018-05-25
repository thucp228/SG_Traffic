package com.qteam.saigonjams;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private GoogleMap googleMap;
    private MapView mapView;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_map, container, false);

         mapView = rootView.findViewById(R.id.mapView);
         if (mapView != null) {
             mapView.onCreate(null);
             mapView.onResume();
             mapView.getMapAsync(this);
         }
         return rootView;
    }

    @Override
    public void onMapReady(GoogleMap ggMap) {
        googleMap = ggMap;
        showDefaultLocation();

        checkLocationPermission();

        googleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
//        googleMap.setOnMyLocationClickListener(onMyLocationClickListener);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void showDefaultLocation() {
        Toast.makeText(getActivity(), "Hiển thị vị trí mặc định", Toast.LENGTH_SHORT).show();
        LatLng defaultLocation = new LatLng(10.849029, 106.774181);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        else if (googleMap != null)
            googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED)
                checkLocationPermission();
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

//    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener = new GoogleMap.OnMyLocationClickListener() {
//        @Override
//        public void onMyLocationClick(@NonNull Location location) {
//            googleMap.setMinZoomPreference(12);
//
//            CircleOptions circleOptions = new CircleOptions();
//            circleOptions.center(new LatLng(location.getLatitude(), location.getLongitude()));
//            circleOptions.radius(200);
//            circleOptions.fillColor(Color.parseColor("#40479AF7"));
//            circleOptions.strokeWidth(3);
//            circleOptions.strokeColor(Color.parseColor("#479AF7"));
//
//            googleMap.addCircle(circleOptions);
//        }
//    };

}