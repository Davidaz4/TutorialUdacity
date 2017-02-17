package com.davidaz.android.tutorial.popularmovies.utils;

/**
 * Created by d.alvarez.zubeldia on 13/02/2017.
 */

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = "Davidaz Network";
    final static String FILMDB_BASE_URL = "https://api.themoviedb.org/3/";
    final static String DISCOVER = "discover";
    final static String MOVIE = "movie";
    final static String PARAM_API = "api_key";
    final static String PARAM_SORT = "sort_by";

    public static URL buildUrl(String api, String sort) {
        Uri builtUri = Uri.parse(FILMDB_BASE_URL).buildUpon()
                .appendPath(DISCOVER)
                .appendPath(MOVIE)
                .appendQueryParameter(PARAM_API, api)
                .appendQueryParameter(PARAM_SORT, sort)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, "url = " + builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}