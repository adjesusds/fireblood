package com.arieldiax.codelab.fireblood.services;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.pojos.Place;
import com.arieldiax.codelab.fireblood.utils.QueryUtils;

import java.util.List;

public class PlaceAsyncTaskLoader extends AsyncTaskLoader {

    /**
     * Request URL of the Google Places API.
     */
    private static final String GOOGLE_PLACES_API_REQUEST_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";

    /**
     * Query of the search.
     */
    private String mSearchQuery;

    /**
     * Creates a new PlaceAsyncTaskLoader object.
     *
     * @param context     Instance of the Context class.
     * @param searchQuery Query of the search.
     */
    public PlaceAsyncTaskLoader(Context context, String searchQuery) {
        super(context);
        mSearchQuery = searchQuery;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Place> loadInBackground() {
        Uri requestUri = Uri.parse(GOOGLE_PLACES_API_REQUEST_URL);
        Uri.Builder requestUriBuilder = requestUri.buildUpon();
        requestUriBuilder
                .appendQueryParameter("query", mSearchQuery)
                .appendQueryParameter("key", getContext().getString(R.string.configuration_google_maps_api_key))
        ;
        String requestUrl = requestUriBuilder.toString();
        return QueryUtils.fetchPlaces(requestUrl);
    }
}
