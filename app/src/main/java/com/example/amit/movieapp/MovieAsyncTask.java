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
import java.util.Objects;

public class MovieAsyncTask extends AsyncTask<Void, Void,Void> {
    MovieApp context;

    public MovieAsyncTask(MovieApp context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("MovieAsyncTask!!", "onPostExecute");

        // trigger adapter;
        if (context.movieAdapter != null) {
            Log.e("MovieAsyncTask!!", "Triggered Movie Adapter");
            context.movieAdapter.notifyDataSetChanged();
            context.gridView.setAdapter( context.movieAdapter);
        }

        if (MovieDetail.reviewAdapter != null) {
            Log.e("MovieAsyncTask!!", "Triggered Review Adapter");
            MovieDetail.reviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MovieInfo movieInfo;
        URL url;
        try {
            if (Settings.selection_types == Selection_Types.Selection_Types_Popularity) {
                url = MovieDbUtils.buildPopularURL();
            } else if (Settings.selection_types == Selection_Types.Selection_Types_HighestRated) {
                url = MovieDbUtils.buildHighestRatedURL();
            } else {
                url = null;
                Log.e("Amit!!", "URL IS NULL");
            }

            if (url != null) {

                String response = MovieDbUtils.getResponseFromHttpUrl(url);
                JSONObject obj = new JSONObject(response);
                JSONArray result = obj.getJSONArray("results");
                MovieDB.movieInfoArrayList.clear();

                Log.e("MovieAsyncTask!!", "Received:" + result.length());

                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject oneObject = result.getJSONObject(i);

                        // Pulling items from the array
                        movieInfo = new MovieInfo();
                        movieInfo.id = oneObject.getString("id");
                        movieInfo.path = oneObject.getString("poster_path");
                        movieInfo.release_date = oneObject.getString("release_date");
                        movieInfo.title = oneObject.getString("title");
                        movieInfo.vote_average = oneObject.getString("vote_average");
                        movieInfo.overview = oneObject.getString("overview");
                        MovieDB.movieInfoArrayList.add(movieInfo);

                        if (i == 0) {
                            Log.e("!!PATH", movieInfo.path);
                        }
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            } else {
                // url is null that means get movie info list from DB and add to list
                Log.e("Amit", "Number of fav entries:" + MovieDB.movieInfoArrayList.size());
                for (int i = 0; i < MovieDB.movieInfoArrayList.size(); i++) {
                    movieInfo = MovieDB.movieInfoArrayList.get(i);
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
    protected void onCancelled() {
        Log.e("FAILED", "Failed");
    }
}
