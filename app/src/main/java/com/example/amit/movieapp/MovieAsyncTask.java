package com.example.amit.movieapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MovieAsyncTask extends AsyncTask<Void, Void,Void> {
    private MovieApp context;

    public MovieAsyncTask(MovieApp context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("MovieAsyncTask", "onPostExecute");

        if (context.movieGridAdapter != null) {
            Log.d("MovieAsyncTask", "Triggered Movie Adapter Num Entries:" + MovieDB.movieInfoArrayList.size());
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // connect adapter to your grid view

                    Log.d("MovieAsyncTask", "Updating Movie Adapter");
                    context.movieGridAdapter.notifyDataSetChanged();
                    //gridView.setAdapter( context.movieAdapter);
                    if (Settings.parcelable != null) {
                        Log.d("MovieAsyncTask", "Setting Grid Position to:" + Settings.parcelable);
                        MovieApp.recyclerView.getLayoutManager().onRestoreInstanceState(Settings.parcelable);
                        Settings.parcelable = null;
                    }
                }
            });
        }

        if (MovieDetail.reviewAdapter != null) {
            Log.d("MovieAsyncTask", "Triggered Review Adapter");
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MovieDetail.reviewAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MovieInfo movieInfo;
        URL url;
        try {
            if (Settings.selection_types == Selection_Types.Selection_Types_Popularity) {
                Log.d("MovieAsyncAdapter:", "Requesting Popularity");
                url = MovieDbUtils.buildPopularURL();
            } else if (Settings.selection_types == Selection_Types.Selection_Types_HighestRated) {
                Log.d("MovieAsyncAdapter:", "Requesting HighestRated");
                url = MovieDbUtils.buildHighestRatedURL();
            } else {
                url = null;
                Log.d("MovieAsyncTask", "URL IS NULL");
            }

            if (url != null) {

                String response = MovieDbUtils.getResponseFromHttpUrl(url);
                JSONObject obj = new JSONObject(response);
                JSONArray result = obj.getJSONArray("results");
                Log.d("MovieAsyncTask!!", "Received:" + result.length());

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
                            Log.d("!!PATH", movieInfo.path);
                        }
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            } else {
                // url is null that means get movie info list from DB and add to list
                Log.d("MovieAsyncTask", "Number of fav entries:" + MovieDB.movieInfoArrayList.size());
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
        Log.d("FAILED", "Failed");
    }
}
