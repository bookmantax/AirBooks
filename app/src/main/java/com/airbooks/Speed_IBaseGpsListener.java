/**
 * AirBooks app
 * Speed_IBaseGpsListener class :
 * This class makes and interface for the speed
 * works in conjunction with Speed_CLocation.java and SpeedManager.java
 * Not used currently but already developed for future app capabilities.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public interface Speed_IBaseGpsListener extends LocationListener, GpsStatus.Listener {

    public void onLocationChanged(Location location);

    public void onProviderDisabled(String provider);

    public void onProviderEnabled(String provider);

    public void onStatusChanged(String provider, int status, Bundle extras);

    public void onGpsStatusChanged(int event);

}
