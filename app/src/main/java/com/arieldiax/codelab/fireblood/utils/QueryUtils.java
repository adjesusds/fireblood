package com.arieldiax.codelab.fireblood.utils;

import android.text.TextUtils;

import com.arieldiax.codelab.fireblood.models.pojos.Place;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class QueryUtils {

    /**
     * Creates a new QueryUtils object (no, it won't).
     */
    private QueryUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Fetches the instances of the Place class.
     *
     * @param requestUrl URL of the request.
     * @return The instances of the Place class.
     */
    public static List<Place> fetchPlaces(String requestUrl) {
        URL url = getUrl(requestUrl);
        String jsonString = getHttpResponse(url);
        return getPlaces(jsonString);
    }

    /**
     * Gets the instance of the URL class.
     *
     * @param urlString String of the URL.
     * @return The instance of the URL class.
     */
    private static URL getUrl(String urlString) {
        if (TextUtils.isEmpty(urlString)) {
            return null;
        }
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        return url;
    }

    /**
     * Gets the HTTP response of the given URL.
     *
     * @param url Instance of the URL class.
     * @return The HTTP response of the given URL.
     */
    private static String getHttpResponse(URL url) {
        if (url == null) {
            return null;
        }
        String httpResponse = null;
        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setConnectTimeout(15000);
            httpUrlConnection.setReadTimeout(10000);
            httpUrlConnection.connect();
            if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpUrlConnection.getInputStream();
                httpResponse = getFormattedInputStream(inputStream);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return httpResponse;
    }

    /**
     * Gets the formatted version of the input stream.
     *
     * @param inputStream Instance of the InputStream class.
     * @return The formatted version of the input stream.
     */
    private static String getFormattedInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the instances of the Place class.
     *
     * @param jsonString String of the JSON.
     * @return The instances of the Place class.
     */
    private static List<Place> getPlaces(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        List<Place> places = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            JSONArray results = rootObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String placeName = result.getString("name");
                JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                double placeLatitude = location.getDouble("lat");
                double placeLongitude = location.getDouble("lng");
                places.add(new Place()
                        .setName(placeName)
                        .setLatitude(placeLatitude)
                        .setLongitude(placeLongitude)
                );
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return places;
    }
}
