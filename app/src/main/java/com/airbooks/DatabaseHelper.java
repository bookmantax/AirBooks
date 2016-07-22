package com.airbooks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * Extends SQLiteOpenHelper
 *
 * This class will create the database with one table and handle all the database interactions
 * requested from all other clases.
 *
 * Airports Database Table contains 6977 airports spanning the globe, as shown in the map above. Each entry contains the following information:
 * Airport ID	Unique OpenFlights identifier for this airport.
 * Name	Name of airport. May or may not contain the City name.
 * City	Main city served by airport. May be spelled differently from Name.
 * Country	Country or territory where airport is located.
 * IATA/FAA	3-letter FAA code, for airports located in Country "United States of America".
 * 3-letter IATA code, for all other airports.
 * Blank if not assigned.
 * ICAO	4-letter ICAO code.
 * Blank if not assigned.
 * Latitude	Decimal degrees, usually to six significant digits. Negative is South, positive is North.
 * Longitude	Decimal degrees, usually to six significant digits. Negative is West, positive is East.
 * Altitude	In feet.
 * Timezone	Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
 * DST	Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown). See also: Help: Time
 * Tz database time zone	Timezone in "tz" (Olson) format, eg. "America/Los_Angeles".
 * The data is ISO 8859-1 (Latin-1) encoded, with no special characters.
 * Note: Rules for daylight savings time change from year to year and from country to country. The current data is an approximation for 2009, built on a country level. Most airports in DST-less regions in countries that generally observe DST (eg. AL, HI in the USA, NT, QL in Australia, parts of Canada) are marked incorrectly.
 * Sample entries
 * 507,"Heathrow","London","United Kingdom","LHR","EGLL",51.4775,-0.461389,83,0,"E","Europe/London"
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // User Table Variables Declaration
    static final String _ID = "_ID"; //PRIMARY KEY AUTOINCREMENT
    static final String NAME = "name";
    static final String ADDRESS = "address";
    static final String AIRLINE = "airline";
    static final String BASE = "base";
    static final String EMAIL = "email";
    static final String PHONE = "phone";

    // Airports Table Variables Declaration
    static final String AIRPORT_NAME = "airport_name";
    static final String AIRPORT_CITY = "airport_city";
    static final String AIRPORT_COUNTRY = "airport_country";
    static final String AIRPORT_IATA_FAA = "airport_iata_faa";
    static final String AIRPORT_ICAO = "airport_icao";
    static final String AIRPORT_LATITUDE = "airport_latitude";
    static final String AIRPORT_LONGITUDE = "airport_longitude";
    static final String AIRPORT_ALTITUDE = "airport_altitude";
    static final String AIRPORT_TIMEZONE = "airport_timezone";
    static final String AIRPORT_DST = "airport_dst";
    static final String AIRPORT_DTZ = "airport_dtz";

    // Per Diem Table Variables Declaration
    static final String PER_DIEM_COUNTRY = "per_diem_country";
    static final String PER_DIEM_STATE = "per_diem_state";
    static final String PER_DIEM_CITY = "per_diem_city";
    static final String PER_DIEM_COUNTY = "per_diem_county";
    static final String PER_DIEM_SEASON_CODE = "per_diem_season_code";
    static final String PER_DIEM_SEASON_START_DATE = "per_diem_season_start_date";
    static final String PER_DIEM_SEASON_END_DATE = "per_diem_season_end_date";
    static final String PER_DIEM_LODGING = "per_diem_lodging";
    static final String PER_DIEM_MEALS_AND_INCIDENTALS = "per_diem_meals_and_incidentals";
    static final String PER_DIEM_PER_DIEM = "per_diem_per_diem";
    static final String PER_DIEM_EFFECTIVE_DATE = "per_diem_effective_date";
    static final String PER_DIEM_FOOT_NOTE_REFERENCE = "per_diem_foot_note_reference";
    static final String PER_DIEM_LOCATION_CODE = "per_diem_location_code";

    //Trip History Variables Declaration
    static final String LOCATION = "location";
    static final String DATE_IN = "date_in";
    static final String DATE_OUT = "date_out";
    static final String PER_DIEM = "per_diem";


    // Database name and table
    private static final String DATABASE_NAME = "FCT_db";
    private static final String USER_TABLE_NAME = "user_info_table";
    private static final String AIRPORTS_TABLE_NAME = "airports_table";
    private static final String PER_DIEM_TABLE_NAME = "per_diem_table";
    private static final String TRIP_HISTORY_TABLE_NAME = "trip_history_table";
    private static final int DATABASE_VERSION = 1;

    // Other Variables
    int perDiem;

    // On Create Method
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the user_info_table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, " +
                "ADDRESS TEXT, " +
                "AIRLINE TEXT, " +
                "BASE TEXT(4), " +
                "EMAIL TEXT, " +
                "PHONE INT(10))");

        // Creates the airports_table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AIRPORTS_TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "AIRPORT_NAME TEXT, " +
                "AIRPORT_CITY TEXT, " +
                "AIRPORT_COUNTRY TEXT, " +
                "AIRPORT_IATA_FAA TEXT(3), " +
                "AIRPORT_ICAO TEXT(4), " +
                "AIRPORT_LATITUDE REAL, " +
                "AIRPORT_LONGITUDE REAL, " +
                "AIRPORT_ALTITUDE INT(5), " +
                "AIRPORT_TIMEZONE NUMERIC(4), " +
                "AIRPORT_DST TEXT(1), " +
                "AIRPORT_DTZ TEXT) ");

        // Creates the per_diem_table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PER_DIEM_TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PER_DIEM_COUNTRY TEXT, " +
                "PER_DIEM_STATE TEXT, " +
                "PER_DIEM_CITY TEXT, " +
                "PER_DIEM_COUNTY TEXT, " +
                "PER_DIEM_SEASON_CODE TEXT(2), " +
                "PER_DIEM_SEASON_START_DATE TEXT(6), " +
                "PER_DIEM_SEASON_END_DATE TEXT(6), " +
                "PER_DIEM_LODGING INT(4), " +
                "PER_DIEM_MEALS_AND_INCIDENTALS INT(4), " +
                "PER_DIEM_PER_DIEM INT(4), " +
                "PER_DIEM_EFFECTIVE_DATE INT(8), " +
                "PER_DIEM_FOOT_NOTE_REFERENCE TEXT(5), " +
                "PER_DIEM_LOCATION_CODE INT(6)) ");

        //Creates the trip history table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TRIP_HISTORY_TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LOCATION TEXT, " +
                "DATE_IN TEXT, " +
                "DATE_OUT TEXT, " +
                "PER_DIEM TEXT) ");
    }

    // DB Constructor
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        SQLiteDatabase db = this.getReadableDatabase();
    }

    /*
         * Add Airport values to Airports Table
         */

    public String fillAirportsTable(Context ctx, int resId) {


        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            while (( line = bufferedreader.readLine()) != null)
            {
                String[] lines = line.split(",");

                ContentValues airportValues = new ContentValues();

                airportValues.put(AIRPORT_NAME, lines[0]);
                airportValues.put(AIRPORT_CITY, lines[1]);
                airportValues.put(AIRPORT_COUNTRY, lines[2]);
                airportValues.put(AIRPORT_IATA_FAA, lines[3]);
                airportValues.put(AIRPORT_ICAO, lines[4]);
                airportValues.put(AIRPORT_LATITUDE, lines[5]);
                airportValues.put(AIRPORT_LONGITUDE, lines[6]);
                airportValues.put(AIRPORT_ALTITUDE, lines[7]);
                airportValues.put(AIRPORT_TIMEZONE, lines[8]);
                airportValues.put(AIRPORT_DST, lines[9]);
                airportValues.put(AIRPORT_DTZ, lines[10]);

                SQLiteDatabase db = this.getWritableDatabase();
                db.insert(AIRPORTS_TABLE_NAME, null, airportValues);
            }
        }
        catch (IOException e)
        {
            return null;
        }
        return stringBuilder.toString();
    }

    /*
     * Add Per Diem Values to per Diem table
     */
    public String fillPerDiemTable(Context ctx, int resId) {

        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            while (( line = bufferedreader.readLine()) != null)
            {
                String[] lines = line.split(",");

                ContentValues perDiemValues = new ContentValues();

                perDiemValues.put(PER_DIEM_COUNTRY, lines[0]);
                perDiemValues.put(PER_DIEM_STATE, lines[1]);
                perDiemValues.put(PER_DIEM_CITY, lines[2]);
                perDiemValues.put(PER_DIEM_COUNTY, lines[3]);
                perDiemValues.put(PER_DIEM_SEASON_CODE, lines[4]);
                perDiemValues.put(PER_DIEM_SEASON_START_DATE, lines[5]);
                perDiemValues.put(PER_DIEM_SEASON_END_DATE, lines[6]);
                perDiemValues.put(PER_DIEM_LODGING, lines[7]);
                perDiemValues.put(PER_DIEM_MEALS_AND_INCIDENTALS, lines[8]);
                perDiemValues.put(PER_DIEM_PER_DIEM , lines[9]);
                perDiemValues.put(PER_DIEM_EFFECTIVE_DATE, lines[10]);
                perDiemValues.put(PER_DIEM_FOOT_NOTE_REFERENCE, lines[11]);
                perDiemValues.put(PER_DIEM_LOCATION_CODE, lines[12]);

                SQLiteDatabase db = this.getWritableDatabase();
                db.insert(PER_DIEM_TABLE_NAME, null, perDiemValues);
            }
        }
        catch (IOException e)
        {
            return null;
        }
        return stringBuilder.toString();
    }

    //On Upgrade Method to drop the table if exist
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + AIRPORTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + PER_DIEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + TRIP_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    /*
     * Add data to the DataBase
     */
    public boolean insertData(String name, String address, String airline, String base, String email, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select _ID from " + USER_TABLE_NAME
                + " where _ID = '1'", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("_ID"));
            String[] args = {String.valueOf(id)};
            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, name);
            contentValues.put(ADDRESS, address);
            contentValues.put(AIRLINE, airline);
            contentValues.put(BASE, base);
            contentValues.put(EMAIL, email);
            contentValues.put(PHONE, phone);
            // to check if data was updated
            long result = db.update(USER_TABLE_NAME, contentValues, "_ID=?", args);
            return result != -1;
        }
        return false;
    } // end of insertData

    /*
    * Add trip to the Database
    */
    public boolean insertTrip(String location, String dateIn, String dateOut, String perDiem){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION, location);
        contentValues.put(DATE_IN, dateIn);
        contentValues.put(DATE_OUT, dateOut);
        contentValues.put(PER_DIEM, perDiem);
        long result = db.insert(TRIP_HISTORY_TABLE_NAME, null, contentValues);
        return result != -1;
    }


    /*
     * Pulls all data from the DataBase table. and assign it to the variable result
     */
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + USER_TABLE_NAME, null);
        return result;
    } // End of getAllData

    /*
     * Pulls all data from the DataBase table. and assign it to the variable result
     */
    public Cursor getAllTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TRIP_HISTORY_TABLE_NAME, null);
        return result;
    } // End of getAllData

    /*
        * Check if a table is empty and return a int
        */
    public boolean isEmpty(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + table;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount != 0) {
            return false;
        } else {
            return true;
        }
    }

    /*
    * Check if a table is empty and return a int
    */
    public boolean isTripsEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TRIP_HISTORY_TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertAirports(String airports_table_name, Object o, ContentValues airportValues) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = airportValues;
        // to check if data was inserted
        long result = db.insert(airports_table_name, null, contentValues);
        return result != -1;
    }

    /*
     * Search methods used by specific reference
     */
    public double allPerDiem() {
        double total = 0.00;
        Boolean moreRows;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT PER_DIEM FROM trip_history_table",null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            total += Double.parseDouble(cursor.getString(cursor.getColumnIndex("PER_DIEM")));
            cursor.moveToNext();
        }
        return total;
    }

    public int getPerDiemByCountry(String country){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_PER_DIEM from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_COUNTRY = '" + country + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("PER_DIEM_PER_DIEM"));
        }
        return 0;
    }

    public int getPerDiemBySate(String state){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_PER_DIEM from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_STATE = '" + state + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("PER_DIEM_PER_DIEM"));
        }
        return 0;
    }

    public int getPerDiemByCity(String city){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_PER_DIEM from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_CITY = '" + city + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("PER_DIEM_PER_DIEM"));
        }
        return 0;
    }
    public Double getMealsByCountry(String country){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_MEALS_AND_INCIDENTALS from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_COUNTRY = '" + country + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(cursor.getColumnIndex("PER_DIEM_MEALS_AND_INCIDENTALS"));
        }
        return 0.0;
    }

    public Double getMealsBySate(String state){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_MEALS_AND_INCIDENTALS from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_STATE = '" + state + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(cursor.getColumnIndex("PER_DIEM_MEALS_AND_INCIDENTALS"));
        }
        return 0.0;
    }

    public Double getMealsByCity(String city){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_MEALS_AND_INCIDENTALS from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_CITY = '" + city + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(cursor.getColumnIndex("PER_DIEM_MEALS_AND_INCIDENTALS"));
        }
        return 0.0;
    }

    public Double getLodgingByCountry(String country){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_MEALS_AND_INCIDENTALS from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_COUNTRY = '" + country + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(cursor.getColumnIndex("PER_DIEM_MEALS_AND_INCIDENTALS"));
        }
        return 0.0;
    }

    public Double getLodgingBySate(String state){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_LODGING from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_STATE = '" + state + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(cursor.getColumnIndex("PER_DIEM_LODGING"));
        }
        return 0.0;
    }

    public Double getLodgingByCity(String city){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select PER_DIEM_LODGING from " + PER_DIEM_TABLE_NAME
                + " where PER_DIEM_CITY = '" + city + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(cursor.getColumnIndex("PER_DIEM_LODGING"));
        }
        return 0.0;
    }

		// Return home location from user info table base airport
	    public String getHomeLocation(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select BASE from " + USER_TABLE_NAME, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String base = cursor.getString(cursor.getColumnIndex("BASE"));
			return base;
        }
        return null;
    }

    public Double getHomeLatitude_IATA(String base){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select AIRPORT_LATITUDE from " + AIRPORTS_TABLE_NAME
                + " where AIRPORT_IATA_FAA = '" + base + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            Double latitude = cursor.getDouble(cursor.getColumnIndex("AIRPORT_LATITUDE"));
            return latitude;
        }
        return 0.0;
    }

    public Double getHomeLongitude_IATA(String base){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select AIRPORT_LONGITUDE from " + AIRPORTS_TABLE_NAME
                + " where AIRPORT_IATA_FAA = '" + base + "'", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            Double longitude = cursor.getDouble(cursor.getColumnIndex("AIRPORT_LONGITUDE"));
            return longitude;
        }
        return 0.0;
    }

    public Double getHomeLatitude_ICAO(String base){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select AIRPORT_LATITUDE from " + AIRPORTS_TABLE_NAME
                + " where AIRPORT_ICAO = '" + base + "'", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            Double latitude = cursor.getDouble(cursor.getColumnIndex("AIRPORT_LATITUDE"));
            return latitude;
        }
        return 0.0;
    }

    public Double getHomeLongitude_ICAO(String base){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select AIRPORT_LONGITUDE from " + AIRPORTS_TABLE_NAME
                + " where AIRPORT_ICAO = '" + base + "'", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            Double longitude = cursor.getDouble(cursor.getColumnIndex("AIRPORT_LONGITUDE"));
            return longitude;
        }
        return 0.0;
    }

    public Cursor getNearAirports(String country){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select AIRPORT_LATITUDE, AIRPORT_LONGITUDE, AIRPORT_CITY from "
                + AIRPORTS_TABLE_NAME + " where AIRPORT_COUNTRY = '" + country + "'", null);
        if(cursor.getCount() > 0) {
            return cursor;
        }
        return null;
    }

}
