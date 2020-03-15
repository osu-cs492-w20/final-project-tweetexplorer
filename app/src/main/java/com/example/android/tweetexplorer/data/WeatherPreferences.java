package com.example.android.tweetexplorer.data;

public class WeatherPreferences {
    private static final String DEFAULT_FORECAST_LOCATION = "Corvallis,OR,US";
    private static final String DEFAULT_TEMPERATURE_UNITS = "imperial";
    private static String DEFAULT_TEMPERATURE_UNITS_ABBR = "F";

    public static void setDefaultTemperatureUnits(String units){
        DEFAULT_TEMPERATURE_UNITS_ABBR = units;
    }

    public static String getDefaultForecastLocation() {
        return DEFAULT_FORECAST_LOCATION;
    }

    public static String getDefaultTemperatureUnits() {
        return DEFAULT_TEMPERATURE_UNITS;
    }

    public static String getDefaultTemperatureUnitsAbbr() {
        return DEFAULT_TEMPERATURE_UNITS_ABBR;
    }
}
