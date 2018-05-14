package com.example.amit.movieapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MovieReviewsAsyncTask  extends AsyncTask<MovieInfo, Void,Void> {
    public MovieInfo movieInfo;
    public MovieDetail parentActivity;

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

        Log.e("MovieReviewsAsyncTask!!", "Reviews List:" + String.valueOf(reviewresult.length()));
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

    public MovieReviewsAsyncTask(MovieDetail parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    protected Void doInBackground(MovieInfo... movieInfos) {
        movieInfo = movieInfos[0];

        try {
            // Read youtube keys
            create_youtube_keys(movieInfo);

            // Read Reviews
            create_review_list(movieInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        parentActivity.update_review_trailer_link(movieInfo);
    }
}
