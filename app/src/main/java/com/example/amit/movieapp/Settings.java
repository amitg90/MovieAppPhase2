package com.example.amit.movieapp;

import android.os.Parcelable;

enum Selection_Types {
    Selection_Types_Popularity,
    Selection_Types_HighestRated,
    Selection_Types_Favorite
}

public class Settings {
    public static Selection_Types selection_types = Selection_Types.Selection_Types_Popularity;
    public static Parcelable parcelable = null;
}
