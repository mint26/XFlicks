package com.when0matters.xflicks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.when0matters.xflicks.adapters.MovieArrayAdapter;
import com.when0matters.xflicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    ListView lvItems;
    ProgressDialog dialog;
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this,movies, this.getResources().getConfiguration().orientation);
        lvItems.setOnItemClickListener(this);
        lvItems.setAdapter(movieArrayAdapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        loadData();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MovieActivity.class);
        Movie movie = movies.get(position);
        intent.putExtra(getResources().getString(R.string.MOVIE_ID), movie.getId());
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        movieArrayAdapter.currentConfigurationOrientation = newConfig.orientation;

    }

    // Write list state to bundle
    @Override
    protected void onSaveInstanceState(Bundle state) {
        mListState = lvItems.onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
        super.onSaveInstanceState(state);
    }

    // Restore list state from bundle
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        mListState = state.getParcelable(LIST_STATE);
        super.onRestoreInstanceState(state);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // make sure data has been reloaded into adapter first
        // ONLY call this part once the data items have been loaded back into the adapter
        // for example, inside a success callback from the network
        if (mListState != null) {
            lvItems.onRestoreInstanceState(mListState);
            mListState = null;
        }
    }

    public void loadData(){
        String url=String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s", getResources().getString(R.string.API_KEY));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJSONResults = null;
                movies.clear();
                try{
                    movieJSONResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJSONResults));
                    movieArrayAdapter.notifyDataSetChanged();
                    Log.d(getResources().getString(R.string.DEBUG),movieJSONResults.toString());
                    dialog.hide();

                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
