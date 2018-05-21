package com.example.amit.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ImageViewHolder> {
    ArrayList<MovieInfo> arrayList;
    Context context;
    RecyclerView recyclerView = null;
    CustomGridItemClick customGridItemClick;

    public MovieGridAdapter(RecyclerView recyclerView, CustomGridItemClick listener, Context context, ArrayList<MovieInfo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerView = recyclerView;
        this.customGridItemClick = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.grid_view_items, parent, shouldAttachToParentImmediately);
        final ImageViewHolder viewHolder = new ImageViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customGridItemClick.onItemClick(view, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        MovieInfo movieInfo;
        String imageUri;

        movieInfo = arrayList.get(position);

        // set image based on selected text
        imageUri = MovieDbUtils.IMAGE_BASE_URL + movieInfo.path;

       // Log.e("MovieGridAdapter", "Image URI Fetch:" +imageUri);
        Picasso.with(context)
                .load(imageUri)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.grid_image_view);
        }
    }

}
