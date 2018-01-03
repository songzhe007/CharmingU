package zhe.charmu;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class GoogleMapActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{



    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private Polyline line;

    private LocationManager locationManager;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;

    private float distance = 0;
    private float speed=0;
    private ArrayList<Location> locationArrayList;

    private Boolean isGPSenabled = false;
    private Boolean isNetWorkenabled = false;
    private String provider;
    private LocationRequest mLocationRequest;
    private ArrayList<LatLng> points;
    private float s=0;

    private TextView timer_tv;
    private LocationListener mLocationListener;
    private Button start_btn;
    private Button finish_btn;
    private Button stop_btn;
    private TextView speed_tv;
    private Boolean isRun = true;
    private Chronometer ch;
    private TextView distance_tv;

    private int i=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_map);
        points = new ArrayList<LatLng>();
        locationArrayList = new ArrayList<Location>();


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetWorkenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        ch=(Chronometer) findViewById(R.id.timer);

        distance_tv = (TextView) findViewById(R.id.distance);
        speed_tv =(TextView) findViewById(R.id.speed);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("no permission!!!");
            return;


        }
        if(isGPSenabled) {
            System.out.println("66666!!!!!!");
            mLastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (mLastLocation != null) {
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
            }
        }
        else if(isNetWorkenabled){
            System.out.println("wowudi!!!!!!");
            mLastLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if (mLastLocation != null) {
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
            }
        }
        else{
            System.out.println("Not Available!!");
        }


        start_btn = (Button) findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
                ch.setBase(SystemClock.elapsedRealtime());
                int hour = (int) ((SystemClock.elapsedRealtime() - ch.getBase()) / 1000 / 60);
                ch.setFormat("0"+String.valueOf(hour)+":%s");
                ch.start();

                addMarker();
                start_btn.setVisibility(View.INVISIBLE);
                stop_btn.setEnabled(true);

            }
        });

        finish_btn =(Button) findViewById(R.id.finish);
        finish_btn.setVisibility(View.INVISIBLE);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoogleMapActivity.this,MainActivity.class));
            }
        });

        stop_btn = (Button) findViewById(R.id.stop);
        stop_btn.setEnabled(false);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRun = false;
                ch.stop();
                stop_btn.setVisibility(View.INVISIBLE);
                finish_btn.setVisibility(View.VISIBLE);

            }
        });



    }



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GoogleApiClient.ConnectionCallbacks googleconc = new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {

                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            if(isNetWorkenabled) {
                provider = LocationManager.NETWORK_PROVIDER;
            }
            else if(isGPSenabled) {
                provider = LocationManager.GPS_PROVIDER;
            }

            locationManager.requestLocationUpdates(provider, 2000, 1,
                    new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {


                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            points.add(latLng);
                            locationArrayList.add(location);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                                    16));

                            if(locationArrayList.size()==1) {
                                distance= mLastLocation.distanceTo(locationArrayList.get(0));
                            }
                            else if(locationArrayList.size()>1){
                                distance+=locationArrayList.get(locationArrayList.size()-2).
                                        distanceTo(locationArrayList.get(locationArrayList.size()-1));
                            }
                            System.out.println(points);
                            distance = (float) Math.round(distance * 100)/100;
                            speed = distance/((float)2*i);
                            speed = (float) Math.round(speed *100)/100;
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
                    });
            i++;
            System.out.println(i);
        }

    };



    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            if (isRun) {
                Message message = new Message();
                mHandler.postDelayed(this, 2000);
                mHandler.sendMessage(message);
            }
            else{
                return;
            }
        }
    });



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Find Your Location", Toast.LENGTH_SHORT).show();

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }


    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        redrawLine();


        distance_tv.setText("Distance:"+distance+"m");
        speed_tv.setText("Speed:"+speed+"m/s");

        System.out.println("main thread distance:"+distance);


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


    }

    private void redrawLine(){

        mMap.clear();

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }

        //addMarker(); //add Marker in current position
        line = mMap.addPolyline(options);

        System.out.println("start drawing!!!!");

    }


    private void addMarker() {
        MarkerOptions options = new MarkerOptions();


        LatLng startLatLng = new LatLng(mLatitude, mLongitude);
        options.position(startLatLng);
        Marker mapMarker = mMap.addMarker(options);
        String title = "My Location";
        mapMarker.setTitle(title);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng,
                12));
    }




    protected void onStart() {
        mGoogleApiClient.connect();
        System.out.println("connected!!!");
        super.onStart();
    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void onResume(){

        super.onResume();
    }
    protected void onPause(){

        super.onPause();

    }


/**
 protected void startLocationUpdates() {
 if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
 != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
 (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
 != PackageManager.PERMISSION_GRANTED) {
 return;
 }
 LocationServices.FusedLocationApi.requestLocationUpdates(
 mGoogleApiClient, mLocationRequest, this);
 }

 protected void createLocationRequest() {
 mLocationRequest = new LocationRequest();
 mLocationRequest.setInterval(5000);
 mLocationRequest.setFastestInterval(1000);
 mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
 // mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
 }*/


}
