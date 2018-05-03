package com.example.amit.movieapp;

import java.util.ArrayList;

public class MovieInfo {
    String id;
    String path;
    String title;
    String release_date;
    String vote_average;
    String overview;
    ArrayList<String> youtubekeylist;
    ArrayList<ReviewInfo> reviewList;

    public MovieInfo()
    {
        youtubekeylist = new ArrayList<String>();
        reviewList = new ArrayList<ReviewInfo>();
    }
}
