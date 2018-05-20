package com.example.amit.movieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {
    Context context;
    ArrayList<MovieInfo> movieInfoArrayList;

    public MovieAdapter(Context context, ArrayList<MovieInfo> movieInfoArrayList) {
        this.context = context;
        this.movieInfoArrayList = movieInfoArrayList;
    }

    @Override
    public int getCount() {
        //Log.d("MovieAdapter", "!!!Size:" + movieInfoArrayList.size());
        return movieInfoArrayList.size();
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
            gridView = inflater.inflate( R.layout.movie_items , null);
        } else {
            gridView = convertView;
        }

        // set value into imageview
        //Log.e("MovieAdapter Query Position:", String.valueOf(position));
        movieInfo = movieInfoArrayList.get(position);
        imageView = gridView.findViewById(R.id.imageView);

        // set image based on selected text
        imageUri = MovieDbUtils.IMAGE_BASE_URL + movieInfo.path;
        //Log.e("MovieAdapter", "Image URI Fetch:" +imageUri);
        Picasso.with(context)
                .load(imageUri)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imageView);

        return gridView;
    }
}
