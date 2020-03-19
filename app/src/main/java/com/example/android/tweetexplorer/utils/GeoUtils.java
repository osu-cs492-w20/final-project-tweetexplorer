package com.example.android.tweetexplorer.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GeoUtils implements LocationListener {


    private Location getGeoData(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || lm.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("getGeoData", "Permission Granted");
            return l;
        }
    }

    public String calculate(Context context, float  des_lat, float des_lon, String unit){
        Location l = getGeoData(context);
        if (l == null)
            return "Turn on GPS to show the distance";
        else{
            double start_lat = l.getLatitude();
            double start_lon = l.getLongitude();

            Log.d("Calculate", "Current Lat, lon:" + start_lat + " ," + start_lon);

            //Start the calculation

            double theta = start_lon - des_lon;
            double dist = Math.sin(Math.toRadians(start_lat)) * Math.sin(Math.toRadians(des_lat)) + Math.cos(Math.toRadians(start_lat)) * Math.cos(Math.toRadians(des_lat)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("kilometers")) {
                dist = dist * 1.609344;
                dist = Math.round(dist * 100.0) / 100.0;
                return  dist + " km";
            } else if (unit.equals("yards")) {
                dist = dist * 1760;
                dist = Math.round(dist * 100.0) / 100.0;
                return  dist + " yd";
            } else{
                dist = Math.round(dist * 100.0) / 100.0;
                return dist + " mile";
            }
        }
    }





    @Override
    public void onLocationChanged(Location location) {

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
}
