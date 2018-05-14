package com.example.amit.movieapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MovieDetail extends Activity {

    public static ReviewAdapter reviewAdapter = null;

    public void update_review_trailer_link(MovieInfo movieInfo) {
        // setup listview for trailers
        ListView trailerListView = findViewById(R.id.youtube_list_view);

        // setup review list adapter
        ListView reviewListView = findViewById(R.id.review_list_view);

        // set button onclick listener to update database through content Provider
        final Button favButton = findViewById(R.id.favorite_label_tv);

        TextView trailerTextView = findViewById(R.id.trailer_label_tv);

//        /* No need to shows trailers and reviews if in favroite mode
//         */
//        if (Settings.selection_types == Selection_Types.Selection_Types_Favorite) {
//            // hide trailer and reviews.
//            trailerListView.setVisibility(View.INVISIBLE);
//            reviewListView.setVisibility(View.INVISIBLE);
//            favButton.setVisibility(View.INVISIBLE);
//            trailerTextView.setVisibility(View.INVISIBLE);
//            return;
//        } else {
//            trailerListView.setVisibility(View.VISIBLE);
//            reviewListView.setVisibility(View.VISIBLE);
//            favButton.setVisibility(View.VISIBLE);
//            trailerTextView.setVisibility(View.VISIBLE);
//        }

        //Log.e("MovieDetailActivty", String.valueOf(movieInfo.youtubekeylist.size()));

        MovieTrailersListAdapter adapter = new MovieTrailersListAdapter(this, movieInfo.youtubekeylist);
        trailerListView.setAdapter(adapter);

        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = MovieTrailersListAdapter.trailerlist.get(position);

                Log.e("MovieDetail", key);
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });


        Log.e("MovieDetailActivity", String.valueOf(movieInfo.reviewList.size()));

        reviewAdapter = new ReviewAdapter(this, movieInfo.reviewList);
        reviewListView.setAdapter(reviewAdapter);

        favButton.setTag(movieInfo);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MovieDetail", "Button pressed");

                // Add a entry in DB using content provider
                ContentValues values = new ContentValues();

                MovieInfo movieInfo = (MovieInfo)view.getTag();

                // find the movie ID it belongs too
                values.put(MovieContentProvider.ID, movieInfo.id);
                values.put(MovieContentProvider.PATH, movieInfo.path);
                values.put(MovieContentProvider.NAME, movieInfo.title);
                values.put(MovieContentProvider.OVERVIEW, movieInfo.overview);
                values.put(MovieContentProvider.RELEASE_DATE, movieInfo.release_date);
                values.put(MovieContentProvider.VOTE_AVG, movieInfo.vote_average);

                Uri uri = getContentResolver().insert(
                        MovieContentProvider.CONTENT_URI, values);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MovieInfo movieInfo;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int position = intent.getIntExtra(MovieApp.EXTRA_MESSAGE, -1);

        if (position == -1) {
            Toast.makeText(this, "Invalid Position entered", Toast.LENGTH_LONG);
            return;
        }

        movieInfo = MovieDB.movieInfoArrayList.get(position);

        MovieInfo[] movieInfos = { movieInfo };
        MovieReviewsAsyncTask movieReviewsAsyncTask = new MovieReviewsAsyncTask(this);
        movieReviewsAsyncTask.execute(movieInfos);

        // Capture the layout's TextView and set the string as its text
        TextView textView;
        textView = findViewById(R.id.title_label_tv);
        textView.setText(movieInfo.title);

        ImageView imageView;
        String imageUri;

        movieInfo = MovieDB.movieInfoArrayList.get(position);
        imageView = findViewById(R.id.image_view_label_tv);

        // set image based on selected text
        imageUri = MovieDbUtils.IMAGE_BASE_URL + movieInfo.path;
        Picasso.with(this)
                .load(imageUri)
                .into(imageView);

        textView = findViewById(R.id.overview_tv);
        textView.setText(movieInfo.overview);

        // release date
        textView = findViewById(R.id.release_date_label_tv);
        textView.setText("Release Date: " + movieInfo.release_date);

        // vote average
        textView = findViewById(R.id.vote_label_tv);
        textView.setText("Average Vote: " + movieInfo.vote_average);

    }

}