package com.raza.mealshare.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.raza.mealshare.CustomDialogs.CustomToast;
import com.raza.mealshare.Database.AllDataBaseConstant;
import com.raza.mealshare.Database.RadiusFiles.AllRadius;
import com.raza.mealshare.Database.RadiusFiles.RadiusAndCategory;
import com.raza.mealshare.ExtraFiles.FirebaseRef;
import com.raza.mealshare.MainActivity;
import com.raza.mealshare.R;
import com.raza.mealshare.Utilities.CheckForPermissions;
import com.raza.mealshare.Utilities.LoadingDialog;
import com.raza.mealshare.Utilities.Utilities;
import com.raza.mealshare.databinding.ActivityPickUpLocationBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PickUpLocation extends AppCompatActivity implements OnMapReadyCallback {
    private LatLng currentLatLog = null;
    private GoogleMap mMap;
    private View mMapView;
    private String address;
    private LoadingDialog loadingDialog;
    private FirebaseRef ref =new FirebaseRef();
    private int LocationPermisssion = 104;
    private FusedLocationProviderClient fusedLocationClient;
    private int REQUEST_CHECK_SETTINGS=105;
    private ActivityPickUpLocationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPickUpLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog=new LoadingDialog(this,R.style.DialogLoadingTheme);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mMapView=mapFragment.getView();
        setUpToolbar();
        binding.saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndExit();
            }
        });
    }
    private void setUpToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Item");
    }
    @Override
    public boolean onSupportNavigateUp() {
        saveAndExit();
        return super.onSupportNavigateUp();
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

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng mPosition = mMap.getCameraPosition().target;
                Log.i("values", String.valueOf(mPosition.latitude) + "  " + String.valueOf(mPosition.longitude));
                address=Address(mPosition);
                binding.ComplainBox.setText(address);
            }
        });

        if (mMapView != null && mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 20, 20);
        }

          CheckForLocation();

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==REQUEST_CHECK_SETTINGS){
            if (resultCode==Activity.RESULT_OK){
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(5000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                loadingDialog.show();
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            if (task.getResult()!=null){
                                Location location=task.getResult();
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                mMap.animateCamera(cameraUpdate);
                            }
                        }
                    }
                });
                fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            loadingDialog.dismiss();
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            mMap.animateCamera(cameraUpdate);
                            fusedLocationClient.removeLocationUpdates(this);
                            return;
                        }
                    }
                }, Looper.getMainLooper());
            }
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationPermisssion) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CheckForLocation();
            } else {
                new CustomToast(PickUpLocation.this,"Location required");
                finish();
            }
        }
    }
    private void CheckForLocation() {
        CheckForPermissions.CheckForLocationPermission(PickUpLocation.this, PickUpLocation.this, LocationPermisssion, new CheckForPermissions.Results() {
            @Override
            public void HavePermission() {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(PickUpLocation.this);
                if (ActivityCompat.checkSelfPermission(PickUpLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PickUpLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    new CustomToast(PickUpLocation.this,"Location required");
                    finish();
                }
                createLocationRequest();

            }

            @Override
            public void Requested() {

            }
        });

    }
    @SuppressLint("MissingPermission")
    protected void createLocationRequest() {
        mMap.setMyLocationEnabled(true);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                loadingDialog.show();
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            if (task.getResult()!=null){
                                Location location=task.getResult();
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                mMap.animateCamera(cameraUpdate);
                            }
                        }
                    }
                });
                fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            loadingDialog.dismiss();
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            mMap.animateCamera(cameraUpdate);
                            fusedLocationClient.removeLocationUpdates(this);
                            return;
                        }
                    }
                }, Looper.getMainLooper());
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(PickUpLocation.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveAndExit();
        super.onBackPressed();
    }

    private void saveAndExit() {

        if (currentLatLog!=null){
            if (address.length()<5){
                new CustomToast(PickUpLocation.this,"Address too short");
                return;
            }
            Utilities.SaveLocation(PickUpLocation.this,currentLatLog);

            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                finish();
                return;
            }

            loadingDialog.show();
            HashMap<String, Object> map=new HashMap<>();
            map.put(ref.user_address,address);
            GeoPoint geoPoint=new GeoPoint(currentLatLog.latitude,currentLatLog.longitude);
            map.put(ref.user_location,geoPoint);
            loadingDialog.show();
            FirebaseFirestore.getInstance().collection(ref.users).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    try {
                        if (task.isSuccessful()){
                            new CustomToast(PickUpLocation.this,"Updated");
                            loadingDialog.dismiss();
                            finish();
                        }else {
                            new CustomToast(PickUpLocation.this,"Try again");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        finish();
    }
    private String Address(@NotNull LatLng latLng){
        currentLatLog=latLng;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.isEmpty()){
                return "";
            }
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}