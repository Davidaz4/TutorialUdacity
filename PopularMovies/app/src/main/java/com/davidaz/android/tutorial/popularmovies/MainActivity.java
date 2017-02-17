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
    private String sortage = "popularity";
    private String order = ".desc";
    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isOnline()){
            setContentView(R.layout.activity_main);
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            GridLayoutManager layoutManager
                    = new GridLayoutManager(this,2);
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


    private void loadMoviePosters() {
        showPosterDataView();
        new FetchPostersTask().execute(apiKey, sortage + order);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sorting) {
            if (getString(R.string.sortPop).equals(item.getTitle())) {
                sortage = "vote_average";
                item.setTitle(getString(R.string.sortAvg));
            }
            else{
                sortage = "popularity";
                item.setTitle(getString(R.string.sortPop));
            }
            loadMoviePosters();
            return true;
        }
        if (id == R.id.ascdesc) {
            if (getString(R.string.asc).equals(item.getTitle())) {
                order = ".desc";
                item.setTitle(R.string.desc);
            }
            else {
                order = ".asc";
                item.setTitle(R.string.asc);
            }
            loadMoviePosters();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}