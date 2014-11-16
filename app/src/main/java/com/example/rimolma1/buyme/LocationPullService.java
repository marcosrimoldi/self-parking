package com.example.rimolma1.buyme;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RIMOLMA1 on 10/29/2014.
 *
 * For more information look: https://gist.github.com/blackcj/20efe2ac885c7297a676
 */

public class LocationPullService extends Service implements GooglePlayServicesClient.ConnectionCallbacks,
            GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 1;
    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 30;
    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    // Stores the lat / long pairs in a text file
    public static final String LOCATION_FILE = "sdcard/location.txt";
    // Stores the connect / disconnect data in a text file
    public static final String LOG_FILE = "sdcard/log.txt";


    IBinder mBinder = new LocalBinder();
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Boolean servicesAvailable = false;

    // Flag that indicates if a request is underway.
    private boolean mInProgress;


    public class LocalBinder extends Binder {
        public LocationPullService getServerInstance() {
            return LocationPullService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        mInProgress = false;
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        servicesAvailable = servicesConnected();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
    }

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            return true;
        } else {

            return false;
        }
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(!servicesAvailable || mLocationClient.isConnected() || mInProgress)
            return START_STICKY;

        setUpLocationClientIfNeeded();
        if(!mLocationClient.isConnected() || !mLocationClient.isConnecting() && !mInProgress)
        {
            appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Started", LOG_FILE);
            mInProgress = true;
            mLocationClient.connect();
        }

        return START_STICKY;
    }

	/*
     * Create a new location client, using the enclosing class to
     * handle callbacks.
     */
    private void setUpLocationClientIfNeeded() {
        if(mLocationClient == null)
            mLocationClient = new LocationClient(this, this, this);
    }

    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d("debug", msg);

        //appendLog(msg, LOCATION_FILE);
        postLocation(location);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

    public void appendLog(String text, String filename)
    {
        File logFile = new File(filename);
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        // Turn off the request flag
        mInProgress = false;
        if(servicesAvailable && mLocationClient != null) {
            mLocationClient.removeLocationUpdates(this);
            // Destroy the current location client
            mLocationClient = null;
        }
        // Display the connection status
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Stopped", LOG_FILE);
        super.onDestroy();
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {

        // Request location updates using static settings
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Connected", LOG_FILE);

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Turn off the request flag
        mInProgress = false;
        // Destroy the current location client
        mLocationClient = null;
        // Display the connection status
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Disconnected", LOG_FILE);
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mInProgress = false;

    /*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
        if (connectionResult.hasResolution()) {

            // If no resolution is available, display an error dialog
        } else {

        }
    }

    private void postLocation(Location location) {
        int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpPost request = new HttpPost(Constants.POST_NEW_ALARM);
        request.setHeader("Content-type", "application/json");

        JSONObject alertJSON = new JSONObject();
        try {
            alertJSON.put("userId", Constants.userID);
            alertJSON.put("latitude", location.getLatitude());
            alertJSON.put("longitude", location.getLongitude());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            StringEntity se = new StringEntity( alertJSON.toString(), HTTP.UTF_8);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            request.setEntity(se);

            //request.setEntity(new ByteArrayEntity(alertJSON.toString().getBytes("UTF8")));
            HttpResponse response = client.execute(request);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}