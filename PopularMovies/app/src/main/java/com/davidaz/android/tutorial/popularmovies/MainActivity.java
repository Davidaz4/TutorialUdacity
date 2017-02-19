package com.davidaz.android.tutorial.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davidaz.android.tutorial.popularmovies.PosterAdapter.ListItemClickHandler;
import com.davidaz.android.tutorial.popularmovies.utils.NetworkUtils;
import com.davidaz.android.tutorial.popularmovies.utils.MoviesJsonUtils;
import com.davidaz.android.tutorial.popularmovies.utils.Result;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListItemClickHandler {
    private String apiKey = "YOURAPIKEY";
    private static final String TAG = "Davidaz main";
    private String sortage = "popular";
    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;
    private TextView mErrorMessageDisplay;
    private boolean wasOffline = true;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isOnline()){
            setContentView(R.layout.activity_main);
            wasOffline = false;
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(getBaseContext()));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mPosterAdapter = new PosterAdapter(this, this);
            mRecyclerView.setAdapter(mPosterAdapter);
            loadMoviePosters();
        }
        else {
            setContentView(R.layout.fail);
            Log.d(TAG, "FAIL!!!");
        }
    }

    //Code from stackoverflow
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private void loadMoviePosters() {
        showPosterDataView();
        new FetchPostersTask().execute(apiKey, sortage);
    }


    @Override
    public void onClick(Result result) {
        Context context = this;
        Class destinationClass = FilmDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(FilmDetailActivity.RESULT, result);
        startActivity(intentToStartDetailActivity);
    }
	
	//This function is used to see if there is any internet connection. I take it from stackoverflow

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showPosterDataView() {
        Log.d(TAG, "Mostrando grid");
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchPostersTask extends AsyncTask<String, Void, ArrayList<Result>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Result> doInBackground(String... params) {
            String api = params[0];
            Log.d(TAG, "Api = " + api);
            String sort = params[1];
            Log.d(TAG, "Sort = " + sort);
            URL posterRequestUrl = NetworkUtils.buildUrl(api, sort);

            try {
                String jsonPosterResponse = NetworkUtils
                        .getResponseFromHttpUrl(posterRequestUrl);
                Log.d(TAG, "Request = "+ jsonPosterResponse);
                ArrayList<Result> simpleJsonPosterData = MoviesJsonUtils
                        .getResults(MainActivity.this, jsonPosterResponse);

                return simpleJsonPosterData;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "FAILLLLL");
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Result> posterData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (posterData != null) {
                showPosterDataView();
                mPosterAdapter.setPosterData(posterData);
            } else {
                showErrorMessage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
    return true;
    }

    private void restart(){
        setContentView(R.layout.activity_main);
        wasOffline = false;
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(getBaseContext()));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPosterAdapter = new PosterAdapter(this, this);
        mRecyclerView.setAdapter(mPosterAdapter);
        loadMoviePosters();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(isOnline()) {
            if(wasOffline)
                restart();
            if (id == R.id.sorting) {
                if (getString(R.string.sortPop).equals(item.getTitle())) {
                    sortage = "top_rated";
                    item.setTitle(getString(R.string.sortAvg));
                } else {
                    sortage = "popular";
                    item.setTitle(getString(R.string.sortPop));
                }
                loadMoviePosters();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}