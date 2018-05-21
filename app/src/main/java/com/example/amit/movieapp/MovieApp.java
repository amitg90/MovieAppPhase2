package com.example.amit.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

public class MovieApp extends Activity implements CustomGridItemClick {
    public MovieGridAdapter movieGridAdapter = null;
    public MovieAsyncTask asyncTask;
    public static Database database = null;
    public static RecyclerView recyclerView;
    public static final String EXTRA_MESSAGE = "com.example.amit.movieapp.MESSAGE";
    Parcelable parcelable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_app);

        // database init
        database = new Database(this);
        database.getReadableDatabase();

        if (savedInstanceState != null) {
            Settings.parcelable = savedInstanceState.getParcelable("SAVED_LAYOUT_MANAGER");
            Log.d("MovieApp", "!!Setting POSITION:");
        }

        movieGridAdapter = new MovieGridAdapter(recyclerView, this, this, MovieDB.movieInfoArrayList);
        recyclerView = findViewById(R.id.rv_numbers);
        recyclerView.setAdapter(movieGridAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);


        Log.d("MovieApp", "!!MOVIE LIST SIZE:" + MovieDB.movieInfoArrayList.size());

        // set adapter onclick listener to display details of movie
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // depending on what is selected update AsyncTask
                if (position == 0) {
                    Settings.selection_types = Selection_Types.Selection_Types_Popularity;
                } else if (position == 1) {
                    Settings.selection_types = Selection_Types.Selection_Types_HighestRated;
                } else {
                    Settings.selection_types = Selection_Types.Selection_Types_Favorite;

                    // trigger DB read.
                    getContentResolver().query(
                            MovieContentProvider.CONTENT_URI, null, null, null, null);
                }

                // clear DB only if we are not fetching fav
                if (Settings.selection_types != Selection_Types.Selection_Types_Favorite) {
                    MovieDB.movieInfoArrayList.clear();
//                    movieAdapter.notifyDataSetChanged();
                }
                Log.d("MovieApp", "Tab Changed: Trigger asyncTask!!!");
                asyncTask = new MovieAsyncTask(MovieApp.this);
                asyncTask.execute();
                Log.d("MovieApp", "Creating new tasks");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] options = getResources().getStringArray(R.array.spinner_list_item_array);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SAVED_LAYOUT_MANAGER", recyclerView.getLayoutManager().onSaveInstanceState());
        Log.d("MovieApp", "!!!Saving Position:");
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        startActivity(intent);
    }
}
