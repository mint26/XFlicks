package com.when0matters.xflicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.when0matters.xflicks.R;
import com.when0matters.xflicks.models.Movie;

import java.util.List;

/**
 * Created by dongdong on 2/11/2017.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public int currentConfigurationOrientation;

    public MovieArrayAdapter(Context context, List<Movie> movies, int configurationOrientation){
        super(context,android.R.layout.simple_list_item_1,movies);
        currentConfigurationOrientation = configurationOrientation;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for position
        Movie movie = getItem(position);
        // Get the data item type for this position
        int type = getItemViewType(position);
        //check the existing view being reused
        if (convertView == null) {
            // Inflate XML layout based on the type
            convertView = getInflatedLayoutForType(type);
        }

        //find the image view
        if (type == Movie.MovieType.Normal.ordinal()){
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            //clear the resource
            ivImage.setImageResource(0);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            tvTitle.setText(movie.getOriginalTitle());
            tvOverview.setText(movie.getOverview());

            if (currentConfigurationOrientation == Configuration.ORIENTATION_PORTRAIT)
                Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.drawable.ic_placeholder).into(ivImage);
            else
                Picasso.with(getContext()).load(movie.getBackdropImage()).placeholder(R.drawable.ic_placeholder).into(ivImage);
        }
        else{
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            //clear the resource
            ivImage.setImageResource(0);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            tvTitle.setText(movie.getOriginalTitle());
            Picasso.with(getContext()).load(movie.getBackdropImage()).placeholder(R.drawable.ic_placeholder).into(ivImage);
        }
        return convertView;
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return Movie.MovieType.values().length;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        // Return an integer here representing the type of View.
        // Note: Integers must be in the range 0 to getViewTypeCount() - 1
        return getItem(position).getMovieType().ordinal();
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        if (type == Movie.MovieType.Popular.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, null);
        }
        else {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        }
    }


}
