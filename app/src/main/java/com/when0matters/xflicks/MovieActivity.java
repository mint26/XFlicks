package com.when0matters.xflicks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.when0matters.xflicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    Movie selectedMovie;
    @BindView(R.id.tvMovieTitle) TextView tvMovieTitle;
    @BindView(R.id.tvRelease) TextView tvRelease;
    @BindView(R.id.tvGenre) TextView tvGenre;
    @BindView(R.id.tvLanguage) TextView tvLanguage;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.tableLayout) TableLayout tableLayout;

    Context mContext;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        ButterKnife.bind(this);

        mContext = this;
        final int selectedMovieId = intent.getIntExtra( getResources().getString(R.string.MOVIE_ID), -1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.show();
        GetMovieFromAPITask task;
        setPanelVisibility(View.GONE);
        if (selectedMovieId != -1){

            String url=String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US", String.valueOf(selectedMovieId), getResources().getString(R.string.API_KEY));
            String movieUrl = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s&language=en-US", String.valueOf(selectedMovieId), getResources().getString(R.string.API_KEY));
            task = new GetMovieFromAPITask();
            task.execute(new String[]{url, movieUrl});

        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void setPanelVisibility(int visibility){
        tvMovieTitle.setVisibility(visibility);
        tableLayout.setVisibility(visibility);
        ratingBar.setVisibility(visibility);
        tvOverview.setVisibility(visibility);
    }


    private class GetMovieFromAPITask extends AsyncTask<String, Void, String> {
        boolean isSuccess = true;
        @Override
        protected String doInBackground(String... urls) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request =
                        new Request.Builder()
                                .url(urls[0])
                                .build();
                Response response = client.newCall(request).execute();

                Request fetchVideoRequest = new Request.Builder().url(urls[1]).build();
                Response fetchVideoResponse = client.newCall(fetchVideoRequest).execute();

                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    selectedMovie = new Movie(json);
                }
                else{
                    isSuccess = false;
                }

                if (fetchVideoResponse.isSuccessful()){
                    String data = fetchVideoResponse.body().string();
                    JSONObject json = new JSONObject(data);
                    JSONArray result = json.getJSONArray("results");
                    if (result.length() > 0)
                        selectedMovie.setMovieKey(result.getJSONObject(0));
                }
                else{
                    isSuccess = false;
                }
            }
            catch (Exception ex){
                Log.d(getResources().getString(R.string.DEBUG), ex.getMessage());
            }
            return isSuccess? getResources().getString(R.string.SUCCESS): getResources().getString(R.string.FAILURE);
    }

    @Override
    protected void onPostExecute(String result) {
        if (selectedMovie != null) {
            tvMovieTitle.setText(selectedMovie.getOriginalTitle());
            tvRelease.setText(selectedMovie.getReleaseDate());
            tvGenre.setText(selectedMovie.getGenres());
            tvLanguage.setText(selectedMovie.getOriginalLanguage());
            tvStatus.setText(selectedMovie.getStatus());
            tvOverview.setText(selectedMovie.getOverview());
            ratingBar.setNumStars(selectedMovie.getNumStars());
            setPanelVisibility(View.VISIBLE);
            progressDialog.hide();

            if (!selectedMovie.getMovieKey().isEmpty()){
            YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                    getFragmentManager().findFragmentById(R.id.youtubeFragment);
            youtubeFragment.initialize(getResources().getString(R.string.API_KEY),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(selectedMovie.getMovieKey());
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
            }
        }
    }
}

}
