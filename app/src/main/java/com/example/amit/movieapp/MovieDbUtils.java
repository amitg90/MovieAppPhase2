package com.example.amit.movieapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MovieDbUtils {
    final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    final static String POPULAR_URL = "popular?";
    final static String HIGHEST_RATED_URL = "top_rated?";
    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    final static String API_KEY_URL = "api_key=";
    final static String API_KEY = "";
    final static String TAG = "MovieDbUtils";
    final static String FINAL_POPULAR_URL = BASE_URL + POPULAR_URL + API_KEY_URL + API_KEY;
    final static String FINAL_HIGHEST_RATED_URL = BASE_URL + HIGHEST_RATED_URL + API_KEY_URL + API_KEY;

    public static URL buildPopularURL() {
        Uri builtUri = Uri.parse(FINAL_POPULAR_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildHighestRatedURL() {
        Uri builtUri = Uri.parse(FINAL_HIGHEST_RATED_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "!!!!Built URI " + url);

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
