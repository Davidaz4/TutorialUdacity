package com.davidaz.android.tutorial.popularmovies;


    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.davidaz.android.tutorial.popularmovies.utils.Result;
    import com.squareup.picasso.Picasso;

public class FilmDetailActivity extends AppCompatActivity {
        protected static final String RESULT = "Result";
        private static final String TAG = "Davidaz Detail";
    private Result result;

        private TextView mTitle;
        private ImageView mPoster;
        private TextView mAverage;
        private TextView mDate;
        private TextView mSynopsis;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_film_detail);

            mTitle = (TextView) findViewById(R.id.title);
            mPoster = (ImageView) findViewById(R.id.poster);
            mAverage = (TextView) findViewById(R.id.average);
            mSynopsis = (TextView) findViewById(R.id.synopsis);
            mDate = (TextView) findViewById(R.id.release);

            Intent intentThatStartedThisActivity = getIntent();
            if (intentThatStartedThisActivity != null) {
                if (intentThatStartedThisActivity.hasExtra(RESULT)) {
                    result = intentThatStartedThisActivity.getParcelableExtra(RESULT);
                    mTitle.setText(result.getOriginal_title());
                    mAverage.setText(new String(getString(R.string.average)+ String.valueOf(result.getVote_average())));
                    mDate.setText(new String(getString(R.string.release)+result.getRelease_date()));
                    mSynopsis.setText(result.getOverview());
                    if (result.getPoster_uri_path() != null) {
                        Picasso.with(getApplicationContext()).load(result.getPoster_uri_path()).into(mPoster);
                    }
                    else
                        mPoster.setImageResource(R.drawable.img_poster);
                }
            }
        }
    }