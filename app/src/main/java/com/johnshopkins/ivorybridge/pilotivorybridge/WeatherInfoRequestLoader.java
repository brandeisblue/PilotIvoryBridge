package com.johnshopkins.ivorybridge.pilotivorybridge;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by skim2 on 10/1/2017.
 */

public class WeatherInfoRequestLoader extends AsyncTaskLoader<List<Weather>> {

    /** Variables to manage HTTP Connection */
    private int READ_TIMEOUT_DURATION = 10000;
    private int CONNECT_TIMEOUT_DURATION = 15000;

    /** Tag for Log Messages */
    private static final String LOG_TAG = WeatherInfoRequestLoader.class.getName();
    /** Query URL */
    private String mUrl;
    /** Weather Forecast JSON in String format */
    private String weatherJSONString;

    /** Constructor for the Loader */
    public WeatherInfoRequestLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Weather> loadInBackground() {
        if (mUrl.isEmpty()) {
            return null;
        }
        URL url = createUrl(mUrl);
        try {
            weatherJSONString = makeHTTPRequest(url);
        } catch (IOException e) {
            return null;
        }
        List<Weather> todaysForecast = parseWeatherData(weatherJSONString);
        return todaysForecast;
    }

    private List<Weather> parseWeatherData(String weatherJson) {
        Log.i(LOG_TAG, "Test: parseWeatherData() called");
        double tempHigh;
        double tempLow;
        String weatherCondition;
        URL imageUrl;
        double aveWind;
        double maxWind;
        double aveHumidity;
        ArrayList<Weather> weatherForecasts = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(weatherJson);
            JSONObject simpleForecast = jsonRootObject.getJSONObject("simpleforecast");
            JSONArray forecastDays = jsonRootObject.getJSONArray("forecastday");
            for (int i = 0; i < forecastDays.length(); i++) {
                JSONObject forecastDay = forecastDays.getJSONObject(i);
                JSONObject dateJsonO = forecastDay.getJSONObject("date");
                JSONObject tempHighJsonO = forecastDay.getJSONObject("high");
                JSONObject tempLowJsonO = forecastDay.getJSONObject("low");
                tempHigh = tempHighJsonO.getDouble("fahrenheit");
                tempLow = tempLowJsonO.getDouble("fahrenheit");
                weatherCondition = forecastDay.getString("conditions");
                imageUrl = createUrl(forecastDay.getString("icon_url"));
                aveWind = forecastDay.getJSONObject("avewind").getDouble("mph");
                maxWind = forecastDay.getJSONObject("maxwind").getDouble("mph");
                aveHumidity = forecastDay.getDouble("avehumidity");
                weatherForecasts.add(new Weather(tempHigh, tempLow, weatherCondition, imageUrl,
                        aveWind, maxWind, aveHumidity));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return weatherForecasts;
        }
        return weatherForecasts;
    }

    /**
     * Helper method to create a URL from a given String
     * @param stringUrl
     * @return URL
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }
        return url;
    }

    /**
     * Helper method to make a HTTP Request with a given URL
     * @param url The URL to make query
     * @return JSON in String
     * @throws IOException
     */
    private String makeHTTPRequest(URL url) throws IOException {
        Log.i(LOG_TAG, "Test: makeHTTPRequest() called");
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(READ_TIMEOUT_DURATION /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT_DURATION /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.i(LOG_TAG, "Error: IOException while establishing url connection");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Helper method to convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     * @param inputStream InputStream to convert to String
     * @return String JSON file in String format
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        Log.i(LOG_TAG, "Test: readFromStream() called");
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
