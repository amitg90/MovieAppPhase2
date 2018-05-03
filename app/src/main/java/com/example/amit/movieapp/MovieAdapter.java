package com.example.amit.movieapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MovieAdapter extends BaseAdapter {
    Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        Log.e("MovieAdapter", "Size:" + MovieDB.movieInfoArrayList.size());
        return MovieDB.movieInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        MovieInfo movieInfo;
        String imageUri;
        ImageView imageView;

        if (convertView == null) {
            // get layout from grid_item.xml ( Defined Below )
            Log.e("MovieAdapter", "New Inflate:");
            gridView = inflater.inflate( R.layout.movie_items , null);
        } else {
            gridView = (View) convertView;
        }

        // set value into imageview
        movieInfo = MovieDB.movieInfoArrayList.get(position);
        imageView = gridView.findViewById(R.id.imageView);

        // set image based on selected text
        imageUri = MovieDbUtils.IMAGE_BASE_URL + movieInfo.path;
        Picasso.with(context)
                .load(imageUri)
                .into(imageView);

        return gridView;
    }
}
