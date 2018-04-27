package com.example.amit.movieapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MovieAsyncTask extends AsyncTask {
    MovieApp context;

    public MovieAsyncTask(MovieApp context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        MovieInfo movieInfo;
        URL url;
        try {
            if (Settings.selection_types == Selection_Types.Selection_Types_Popularity) {
                url = MovieDbUtils.buildPopularURL();
            } else {
                url = MovieDbUtils.buildHighestRatedURL();
            }
            String response = MovieDbUtils.getResponseFromHttpUrl(url);
            JSONObject obj = new JSONObject(response);
            JSONArray result = obj.getJSONArray("results");
            MovieDB.movieInfoArrayList.clear();

            Log.e("AMit SIZE!!", String.valueOf(result.length()));
            for (int i = 0; i < result.length(); i++) {
                try {
                    JSONObject oneObject = result.getJSONObject(i);

                    // Pulling items from the array
                    movieInfo = new MovieInfo();
                    movieInfo.path = oneObject.getString("poster_path");
                    movieInfo.release_date = oneObject.getString("release_date");
                    movieInfo.title = oneObject.getString("title");
                    movieInfo.vote_average = oneObject.getString("vote_average");
                    MovieDB.movieInfoArrayList.add(movieInfo);

                    if (i == 0)
                    {
                        Log.e("!!PATH", movieInfo.path);
                    }
                } catch (JSONException e) {
                    // Oops
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        Log.e("MovieAsyncTask!!", "Trigger Adapter");

        // trigger adapter;
        context.movieAdapter.notifyDataSetChanged();
    }
}
