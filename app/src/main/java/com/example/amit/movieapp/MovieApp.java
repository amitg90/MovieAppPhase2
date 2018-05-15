package com.example.amit.movieapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class MovieApp extends Activity {
    public MovieAdapter movieAdapter = null;
    public MovieAsyncTask asyncTask;
    public static final String EXTRA_MESSAGE = "com.example.amit.movieapp.MESSAGE";
    public static Database database = null;
    public static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_app);

        // create ASYNC tasks to fetch movies and store in some Custom Array
        asyncTask = new MovieAsyncTask(this);
        asyncTask.execute();

        // database init
        database = new Database(this);
        database.getReadableDatabase();

        // connect adapter to your grid view
        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get position and trigger detail activity view
                Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
                intent.putExtra(EXTRA_MESSAGE, position);
                startActivity(intent);
            }
        });


        // Set custom adapter (GridAdapter) to gridview
        movieAdapter = new MovieAdapter( this, MovieDB.movieInfoArrayList);
        gridView.setAdapter( movieAdapter);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("SCROLL_POSITION");
            gridView.setSelection(index);
        }

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

                    Toast.makeText(getBaseContext(),
                            "DB Query happened", Toast.LENGTH_LONG).show();

                }
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
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = gridView.getFirstVisiblePosition();
        outState.putInt("SCROLL_POSITION", index);
    }
}
