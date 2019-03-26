package matekgames.com.vreme;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.support.v4.app.ActivityCompat.requestPermissions;

public class GPSTracker extends Service implements LocationListener {

    boolean asked = false;
    public  Boolean update = true;

    private final Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100000; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 20; // 1 minute
    // Declaring a Location Manager
    protected LocationManager locationManager;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String TAG_RUNTIME_PERMISSION = "TAG_RUNTIME_PERMISSION";

    public GPSTracker(Context context) {
        this.mContext = context;
        if (getUpdate()) {
            getLocation();
        }
    }


//    public static class LocationAddress {
//        private static final String TAG = "LocationAddress";
//
//        public void getAddressFromLocation(final double latitude, final double longitude,
//                                                  final Context context, final Handler handler) {
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                    String result = null;
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(
//                                latitude, longitude, 1);
//                        if (addressList != null && addressList.size() > 0) {
//                            Address address = addressList.get(0);
//                            StringBuilder sb = new StringBuilder();
//                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                                sb.append(address.getAddressLine(i)).append("\n");
//                            }
//                            sb.append(address.getLocality()).append("\n");
//                            sb.append(address.getPostalCode()).append("\n");
//                            sb.append(address.getCountryName());
//                            result = sb.toString();
//                            Log.e("address", result);
//                        }
//                    } catch (IOException e) {
//                        Log.e(TAG, "Unable connect to Geocoder", e);
//                    } finally {
//                        Message message = Message.obtain();
//                        message.setTarget(handler);
//                        if (result != null) {
//                            Dan latlong = new Dan();
//                            float[] izid = new float[1];
////                            Location.distanceBetween(latitude,longitude,latlong.getBiljeLatitude(), latlong.getBiljeLongtitude(),izid);
////                            float min=izid[0];
////                            izid[0] = 0;
////                            Location.distanceBetween(latitude,longitude,latlong.getLjubljanaLatitude(), latlong.getLjubljanaLongtitude(),izid);
////                            if(izid[0]>min){Log.e("Krajsa razdalja je min", String.valueOf(min));}
////                            else Log.e("Krajsa razdalja je izid", String.valueOf(izid[0]));
////                            Log.e("min - Bilje", String.valueOf(min));
////                            Log.e("Izid - Lj", String.valueOf(izid[0]));
//                            message.what = 1;
//                            Bundle bundle = new Bundle();
//                            result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                    "\n\nAddress:\n" + result;
//                            bundle.putString("address", result);
//                            message.setData(bundle);
//
//
//                        } else {
//                            message.what = 1;
//                            Bundle bundle = new Bundle();
//                            result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                    "\n Unable to get address for this lat-long.";
//                            bundle.putString("address", result);
//                            message.setData(bundle);
//                        }
//                        message.sendToTarget();
//                    }
//                }
//            };
//            thread.start();
//        }
//    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                 showSettingsAlert();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestRuntimePermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
                        }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                setUpdate(false);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    public boolean getUpdate(){
        return update;
    }

    public void setUpdate(boolean update){
        this.update = update;
    }
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        if(!asked) {
            requestRuntimePermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
            requestRuntimePermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);
            asked=true;
        }
        return this.canGetLocation;
    }
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));
                positiveButton.setTextColor(Color.parseColor("#000"));
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private boolean hasRuntimePermission(Context context, String runtimePermission) {
        boolean ret = false;

        // Get current android os version.
        int currentAndroidVersion = Build.VERSION.SDK_INT;

        // Build.VERSION_CODES.M's value is 23.
        if (currentAndroidVersion > Build.VERSION_CODES.M) {
            // Only android version 23+ need to check runtime permission.
            if (ContextCompat.checkSelfPermission(context, runtimePermission) == PackageManager.PERMISSION_GRANTED) {
                ret = true;
            }
        } else {
            ret = true;
        }
        return ret;
    }
//
//    /* Request app user to allow the needed runtime permission.
//       It will popup a confirm dialog , user can click allow or deny. */
    private void requestRuntimePermission(Application activity, String runtimePermission, int requestCode) {
        Log.e(TAG, "requesting" );
        requestPermissions((Activity)mContext, new String[]{runtimePermission}, requestCode);
    }

    @Override
    public void onLocationChanged(Location location) {
        setUpdate(true);
    }
    @Override
    public void onProviderDisabled(String provider) {
// TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
// TODO Auto-generated method stub

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
// TODO Auto-generated method stub
    }
    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }
}

