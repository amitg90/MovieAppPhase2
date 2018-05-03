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
        }

        if (MovieDetail.reviewAdapter != null) {
            Log.e("MovieAsyncTask!!", "Triggered Review Adapter");
            MovieDetail.reviewAdapter.notifyDataSetChanged();
        }
    }

    void create_youtube_keys(MovieInfo movieInfo) throws IOException, JSONException {
        URL youtubeurl;
        JSONObject obj;
        JSONObject oneObject;
        JSONArray youtuberesult;

        youtubeurl = MovieDbUtils.buildYouTubeQueryKeyURL(movieInfo.id);
        String youtuberesponse = MovieDbUtils.getResponseFromHttpUrl(youtubeurl);

        obj = new JSONObject(youtuberesponse);
        youtuberesult = obj.getJSONArray("results");

        //  Log.e("AMit NEW SIZE!!", String.valueOf(youtuberesult.length()));
        for (int j = 0; j < youtuberesult.length(); j++) {
            try {
                oneObject = youtuberesult.getJSONObject(j);

                // Pulling items from the array
               // Log.e("YOUTUBEKEY", oneObject.getString("key"));

                movieInfo.youtubekeylist.add(oneObject.getString("key"));
            } catch (JSONException e) {
                // Oops
            }
        }
    }

    void create_review_list(MovieInfo movieInfo) throws IOException, JSONException {
        URL reviewurl;
        JSONObject obj;
        JSONObject oneObject;
        JSONArray reviewresult;

        reviewurl = MovieDbUtils.buildReviewKeyGetURL(movieInfo.id);
        String reviewresponse = MovieDbUtils.getResponseFromHttpUrl(reviewurl);

        obj = new JSONObject(reviewresponse);
        reviewresult = obj.getJSONArray("results");

        // Log.e("AMit NEW SIZE!!", String.valueOf(reviewresult.length()));
        ReviewInfo reviewInfo;
        for (int j = 0; j < reviewresult.length(); j++) {
            try {
                oneObject = reviewresult.getJSONObject(j);

                // Pulling items from the array
                //Log.e("YOUTUBEKEY", oneObject.getString("key"));
                reviewInfo = new ReviewInfo();
                reviewInfo.reviewer = oneObject.getString("author");
                reviewInfo.data = oneObject.getString("content");

                // details not needed
                //  review_detail_url = MovieDbUtils.buildReviewDetailKeyGetURL(movieInfo.id, review_id);
                // review_detail_response = MovieDbUtils.getResponseFromHttpUrl(reviewurl);
                //Log.e("AMit AUTHOR!!", reviewInfo.reviewer);

                movieInfo.reviewList.add(reviewInfo);
            } catch (JSONException e) {
                // Oops
            }
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

                        // Read youtube keys
                        create_youtube_keys(movieInfo);

                        // read all reviews of this movie.
                        create_review_list(movieInfo);
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            } else {
                // url is null that means get movie info list from DB and add to list
                Log.e("Amit", "Number of fav entries:" + MovieDB.movieInfoArrayList.size());
                for (int i = 0; i < MovieDB.movieInfoArrayList.size(); i++) {
                    movieInfo = MovieDB.movieInfoArrayList.get(i);

                    // Read youtube keys
                    create_youtube_keys(movieInfo);

                    // read all reviews of this movie.
                    create_review_list(movieInfo);

                    context.movieAdapter = new MovieAdapter(context);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
