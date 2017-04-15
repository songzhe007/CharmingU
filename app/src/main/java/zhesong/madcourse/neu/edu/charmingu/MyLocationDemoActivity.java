package zhesong.madcourse.neu.edu.charmingu;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.country;

/**
 * Created by SongZheDerrick on 2017/4/11.
 */

public class MyLocationDemoActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */


    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private Polyline line;

    private LocationManager locationManager;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    private double current_Lat;
    private double current_Long;
    private  double EARTH_RADIUS = 6378137.0;
    private double distance;
    private double speed;

    private Boolean isGPSenabled = false;
    private Boolean isNetWorkenabled = false;
    private LocationRequest mLocationRequest;
    private ArrayList<LatLng> points;

    private TextView timer_tv;
    private LocationListener mLocationListener;
    private Button start_btn;
    private Button stop_btn;
    private TextView speed_tv;
    private Boolean isRun = true;
    private Chronometer ch;
    private TextView distance_tv;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_location_demo);
        points = new ArrayList<LatLng>();

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
        distance_tv.setText("Distance:"+distance+"m");

        speed_tv =(TextView) findViewById(R.id.speed);
        speed_tv.setText("Speed:"+speed+"m/s");


      start_btn = (Button) findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
                ch.setBase(SystemClock.elapsedRealtime());//计时器清零
             int hour = (int) ((SystemClock.elapsedRealtime() - ch.getBase()) / 1000 / 60);
                ch.setFormat("0"+String.valueOf(hour)+":%s");
                ch.start();

            }
        });

        stop_btn = (Button) findViewById(R.id.stop);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 isRun = false;
                ch.stop();
            }
        });

       // timer_tv = (TextView) findViewById(R.id.timer);
       // timer.schedule(timerTask,0,1000);


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
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1,
                        new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {


                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                points.add(latLng);

                                redrawLine();

                               current_Lat = location.getLatitude();
                                current_Long = location.getLongitude();
                                distance = distance_calculate();
                                speed = speed_calculate();

                                System.out.println(points);
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


            }

        };

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            if (isRun) {
                Message message = new Message();
                mHandler.postDelayed(this, 3000);
                mHandler.sendMessage(message);
            }
        }
    });



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Find Your Location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
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
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
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

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


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
        addMarker();


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
        // 获取permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("no permission!!!");
            return;
        }


    }

    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }

        addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline

        System.out.println("start drawing!!!!");

    }


    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info

        LatLng currentLatLng = new LatLng(mLatitude, mLongitude);
        options.position(currentLatLng);
        Marker mapMarker = mMap.addMarker(options);
        //long atTime = mLastLocation.getTime();
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
       // String title = mLastUpdateTime.concat(", " + requiredArea).concat(", " + city).concat(", " + country);
        String title = "My Location";
        mapMarker.setTitle(title);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                12));
    }

    private  double distance_calculate(){
        double radLat1 =(mLatitude * Math.PI/180.0);
        double radLat2 =(mLongitude * Math.PI/180.0);
        double a = radLat1 - radLat2;
        double b = (mLongitude - current_Long) * Math.PI/180.0 ;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;

    }

    private double speed_calculate(){
        double v = distance/3;
        return v;
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
    }


}
