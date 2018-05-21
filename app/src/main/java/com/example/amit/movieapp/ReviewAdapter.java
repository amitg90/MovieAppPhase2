package com.example.amit.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {
    Context context;
    public static ArrayList<ReviewInfo> trailerList;

    public ReviewAdapter(Context context, ArrayList<ReviewInfo> trailerList)
    {
        this.trailerList = trailerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trailerList.size();
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

        View view;
        ReviewInfo reviewInfo = trailerList.get(position);

        //Log.d("!!!ReviewAdapter", "Called with position:" + String.valueOf(position));

        if (convertView == null) {
            // get layout from grid_item.xml ( Defined Below )
            view = inflater.inflate( R.layout.card_view_layout, null);
        } else {
            view = convertView;
        }

        // set reviewer name
        TextView textView = view.findViewById(R.id.reviewer_name);
        textView.setText(reviewInfo.reviewer);

        // set value into textView
        textView = view.findViewById(R.id.info_text);
        textView.setText(reviewInfo.data);
        //Log.d("ReviewAdapter", reviewInfo.data);

        return view;
    }
}
