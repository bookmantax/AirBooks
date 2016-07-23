/**
 * AirBooks app
 * TripItem class :
 * Help to manage a trip information.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

public class TripItem {
    public final String location;
    public final String dates;
    public final String perDiem;

    public TripItem(String location, String dates, String perDiem){
        this.location = location;
        this.dates = dates;
        this.perDiem = perDiem;
    }
}
