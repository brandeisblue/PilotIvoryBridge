package com.johnshopkins.ivorybridge.pilotivorybridge;

import java.net.URL;

/**
 * Weather Class
 * It holds weather data for a daily forecast, retrieved from WeatherUnderground.
 * It also contains getter methods for each attribute.
 */

public class Weather {

    private double mTempHigh;
    private double mTempLow;
    private String mWeatherCondition;
    private URL mImageUrl;
    private double mAveWind;
    private double mMaxWind;
    private double mAveHumidity;
    private String mDay;
    private String mDate;

    /** Constructor for Weather class. */
    public Weather(double tempHigh, double tempLow, String weatherCondition,
                   URL imageRrl, double aveWind, double maxWind, double aveHumidity) {

        mTempHigh = tempHigh;
        mTempLow = tempLow;
        mWeatherCondition = weatherCondition;
        mImageUrl = imageRrl;
        mAveWind = aveWind;
        mMaxWind = maxWind;
        mAveHumidity = aveHumidity;
    }

    public double getTempHigh() {
        return mTempHigh;
    }

    public double getTempLow() {
        return mTempLow;
    }

    public String getWeatherCondition() {
        return mWeatherCondition;
    }

    public URL getUrl() {
        return mImageUrl;
    }

    public double getAveWind() {
        return mAveWind;
    }

    public double getMaxWind() {
        return mMaxWind;
    }

    public double getAveHumidity() {
        return mAveHumidity;
    }

    public String getDay() {
        return mDay;
    }

    public String getDate() {
        return mDate;
    }
}
