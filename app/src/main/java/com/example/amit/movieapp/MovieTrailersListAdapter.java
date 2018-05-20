package com.example.amit.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MovieTrailersListAdapter extends BaseAdapter {
    Context context;
    public static ArrayList<String> trailerlist;

    public MovieTrailersListAdapter(Context context, ArrayList<String> trailerlist)
    {
        this.trailerlist = trailerlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trailerlist.size();
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

        String imageUri;
        ImageView imageView;
        View view;

        if (convertView == null) {
            // get layout from grid_item.xml ( Defined Below )
            view = inflater.inflate( R.layout.movie_detail_list_item , null);
        } else {
            view = convertView;
        }

        // set value into textView
        TextView textView = view.findViewById(R.id.trailer_num_label_tv);
        textView.setText("Trailer #" + String.valueOf(position));

        return view;
    }
}
