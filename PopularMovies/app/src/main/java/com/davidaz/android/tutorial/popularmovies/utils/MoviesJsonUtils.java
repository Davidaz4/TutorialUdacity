package com.davidaz.android.tutorial.popularmovies.utils;

/**
 * Created by d.alvarez.zubeldia on 13/02/2017.
 */


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public final class MoviesJsonUtils {

    private static final String TAG = "Davidaz Json";

    public static ArrayList<Result> getResults(Context context, String moviesJsonStr) throws JSONException{
        Gson gson = new Gson();
        JSONObject movies = new JSONObject(moviesJsonStr);
        JSONArray jsonArray = movies.getJSONArray("results");
        Type listType = new TypeToken<ArrayList<Result>>(){}.getType();
        ArrayList<Result> myModelList = gson.fromJson(jsonArray.toString(), listType);
        return myModelList;
    }

}