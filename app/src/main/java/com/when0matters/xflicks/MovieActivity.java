package com.when0matters.xflicks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.when0matters.xflicks.models.Movie;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    Movie selectedMovie;
//    @BindView(R.id.ivMovieVideo) ImageView ivMovieVideo;
    @BindView(R.id.tvMovieTitle) TextView tvMovieTitle;
    @BindView(R.id.tvRelease) TextView tvRelease;
    @BindView(R.id.tvGenre) TextView tvGenre;
    @BindView(R.id.tvLanguage) TextView tvLanguage;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvOverview) TextView tvOverview;
//    @BindView(R.id.ivPlayButton) ImageView ivPlayButton;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tableLayout) TableLayout tableLayout;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        ButterKnife.bind(this);

        mContext = this;
        final int selectedMovieId = intent.getIntExtra("movieID", -1);
        GetMovieFromAPITask task;
        setPanelVisibility(View.GONE);
        if (selectedMovieId != -1){

            String url=String.format("https://api.themoviedb.org/3/movie/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US", String.valueOf(selectedMovieId));
            task = new GetMovieFromAPITask();
            task.execute(new String[]{url});

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
        progressBar.setVisibility(visibility);
        tvOverview.setVisibility(visibility);
    }


//    @OnClick(R.id.ivPlayButton)
//    public void playVideo(View view) {
//
////        ivMovieVideo.setVisibility(View.GONE);
////        youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed",
////                new YouTubePlayer.OnInitializedListener() {
////                    @Override
////                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
////                                                        YouTubePlayer youTubePlayer, boolean b) {
////
////                        // do any work here to cue video, play video, etc.
////                        youTubePlayer.cueVideo("6as8ahAr1Uc");
////                    }
////                    @Override
////                    public void onInitializationFailure(YouTubePlayer.Provider provider,
////                                                        YouTubeInitializationResult youTubeInitializationResult) {
////
////                    }
////                });
//    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Bitmap is loaded, use image here
            // Load the image into the view
//            ivMovieVideo.getLayoutParams().height = bitmap.getHeight();
//            ivMovieVideo.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable){

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable){

        }
    };
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
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    selectedMovie = new Movie(json);
                }
                else{
                    isSuccess = false;
                }
            }
            catch (Exception ex){
                Log.d("Debug", ex.getMessage());
            }
            return isSuccess? "Success":"Failure";
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
            Picasso.with(mContext).load(selectedMovie.getBackdropImage()).placeholder(R.drawable.ic_placeholder).into(target);
            setPanelVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                    getFragmentManager().findFragmentById(R.id.youtubeFragment);
            youtubeFragment.initialize("YOUR API KEY",
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo("5xVh-7ywKpE");
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
