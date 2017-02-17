package com.davidaz.android.tutorial.popularmovies.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by d.alvarez.zubeldia on 17/02/2017.
 */


public class Result implements Parcelable {
    // @SerializedName("")
    private String original_title;
    private String poster_path;
    private String release_date;
    private double vote_average;
    private String overview;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getPoster_uri_path() {
        //TODO Formarlo antes!!!!
        String newPath = null;
        if(poster_path!=null)
            newPath =  "http://image.tmdb.org/t/p/w342/" + poster_path;
        return newPath;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.original_title);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.overview);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.original_title = in.readString();
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.vote_average = in.readDouble();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}