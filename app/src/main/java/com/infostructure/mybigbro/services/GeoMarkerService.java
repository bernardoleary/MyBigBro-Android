package com.infostructure.mybigbro.services;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.model.GeoMarkerDisplay;
import com.infostructure.mybigbro.ui.activities.MainActivity;

public class GeoMarkerService extends Service implements LocationListener {

    // Services
    private DataAccessService mDataAccessService;
    private LocationService mLocationService;
    private TimerService mTimerService;
    private LocationManager mLocationManager;

    // Members
    private Location currentLocation;
    private Intent mIntent = null;
    private NotificationManager mNotificationManager = null;

    public GeoMarkerService() {
    }

    private static final String TAG = "GeoMarkerService";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {

		/* Create the services that we need */
        this.mDataAccessService = DataAccessService.getInstance();
        this.mDataAccessService.setApplicationContext(this.getApplicationContext());
        this.mLocationService = new LocationService(mDataAccessService);
        this.mTimerService = new TimerService(mDataAccessService);

		/* Use the LocationManager class to obtain GPS locations */
        this.mLocationManager = (LocationManager)this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        /* Load up the notifications manager */
        this.mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(TAG, "onCreate");
    }

    @SuppressWarnings("deprecation")
    private void showTrackingNotification(){
        Notification notification = new Notification(R.drawable.ic_drawer, "My Big Bro is Running", System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, Notification.FLAG_ONGOING_EVENT);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(this, "My Big Bro", "My Big Bro is Running", contentIntent);
        this.mNotificationManager.notify(1, notification);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        this.showTrackingNotification();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mLocationManager.removeUpdates(this);
        this.mNotificationManager.cancelAll();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (this.mTimerService.timerSequenceExpired()) {
            currentLocation = loc;
            this.update();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void update() {
        if (currentLocation != null) {
            GeoMarkerDisplay geoMarkerDisplay = this.mLocationService.locationChanged(currentLocation);
            /* Not currently doing anything in the UI with this information
            *
            try {
                this.updateDashboard(geoMarkerDisplay);
            } catch (Exception e) {
                Log.d("Error: ", e.toString());
            }
             */
        }
    }
}
