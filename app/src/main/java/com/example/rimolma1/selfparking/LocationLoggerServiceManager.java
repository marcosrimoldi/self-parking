package com.example.rimolma1.selfparking;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by RIMOLMA1 on 10/31/2014.
 *
 * For more information look: https://gist.github.com/blackcj/20efe2ac885c7297a676
 */
public class LocationLoggerServiceManager extends BroadcastReceiver {

    private SharedPreferences mPrefs;

    public static final String TAG = "LocationLoggerServiceManager";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure we are getting the right intent
        if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Log.d("LocationLoggerServiceManager", "BOOT_COMPLETED received");
            boolean mUpdatesRequested = false;
            // Open the shared preferences
            mPrefs = context.getSharedPreferences("SharedPreferences",
                    Context.MODE_PRIVATE);
        /*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
            if (mPrefs.contains("KEY_UPDATES_ON")) {
                mUpdatesRequested = mPrefs.getBoolean("KEY_UPDATES_ON", false);
            }
            if(mUpdatesRequested){
                ComponentName comp = new ComponentName(context.getPackageName(), LocationPullService.class.getName());
                ComponentName service = context.startService(new Intent().setComponent(comp));

                if (null == service){
                    // something really wrong here
                    Log.e(TAG, "Could not start service " + comp.toString());
                }
            }

        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}
