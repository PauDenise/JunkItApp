package com.gui.pc1.junkit;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class MapsFragment extends Fragment implements OnMapReadyCallback{

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is Ready.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready.");
        mMap = googleMap;
    }

    private static final String TAG = "MapsFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public Boolean mLocationPermissionsGranted = false;
    public GoogleMap mMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isServicesOK();
        getLocationPermission();
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    public void isServicesOK() {
        Log.d(TAG, "isServicesOK: Checking Google Services version.");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (available == ConnectionResult.SUCCESS) {
            //Goods.
            Log.d(TAG, "isServicesOK: Google Play Services is working.");
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it.
            Log.d(TAG, "An error occurred but we can fix it.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "You can't make map requests.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap(){
        Log.d(TAG, "initMap: Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions.");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        Log.d(TAG, "getLocationPermission: getcontext value is: "+this.getContext());

        if (PermissionChecker.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(),
                   COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "getLocationPermission: "+ContextCompat.checkSelfPermission(getContext(),
                            COARSE_LOCATION));
                    Log.d(TAG, "getLocationPermission: eto yung dapat:"+PackageManager.PERMISSION_GRANTED );
                    mLocationPermissionsGranted = true;
                    //initMap();
                }else{
                    requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE);
                }
        }else {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
        requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Called.");
        mLocationPermissionsGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i =0; i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed.");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted.");
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();
                }
            }

        }
    }


}

