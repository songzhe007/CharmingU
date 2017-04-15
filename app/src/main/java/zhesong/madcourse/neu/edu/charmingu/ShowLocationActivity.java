package zhesong.madcourse.neu.edu.charmingu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;

/**
 * Created by SongZheDerrick on 2017/4/12.
 */

public class ShowLocationActivity extends AppCompatActivity implements
       ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    // 定义LocationManager对象
    private LocationManager locationManager;
    private TextView show;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationListener locationListener;
    private String locationProvider;
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    private Boolean isGPSenabled = false;
    private Boolean isNetWorkenabled = false;
    private Boolean mRequest = false;
    private long minTime=2000;
    private float minDistance = 5;
    private LocationListener listener;
    private LocationRequest mLocationRequest;
    private ArrayList<LatLng> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        show = (TextView) findViewById(R.id.main_et_show);

        //build GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // 获取系统LocationManager服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetWorkenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        updateView(mLastLocation);

    }


    @Override
    public void onConnected(Bundle connectionHint) {

        // 获取permission
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
        createLocationRequest();
        startLocationUpdates();
        updateView(mLastLocation);
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
        System.out.println("312321321");
        updateView(location);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); //you already have this
        //points.add(latLng); //added
       System.out.println(latLng);

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





    protected void onStart() {
        mGoogleApiClient.connect();
        System.out.println("connected!!!");
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateView(Location location) {
        if (location != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("实时的位置信息：\n经度：");
            sb.append(location.getLatitude());
            sb.append("\n纬度：");
            sb.append(location.getLongitude());
            sb.append("\n高度：");
            sb.append(location.getAltitude());
            sb.append("\n速度：");
            sb.append(location.getSpeed());
            sb.append("\n方向：");
            sb.append(location.getBearing());
            sb.append("\n精度：");
            sb.append(location.getAccuracy());
            show.setText(sb.toString());
        } else {

            show.setText("什么也没有！！！");
        }
    }


}
