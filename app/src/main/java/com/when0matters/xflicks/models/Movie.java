package com.when0matters.xflicks.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dongdong on 2/11/2017.
 */

public class Movie {

    String posterPath;
    String originalTitle;
    String overview;
    String backdropImage;
    String status;
    String genres;
    String originalLanguage;
    String movieKey;
    double voteAverage;
    Date releaseDate;
    int id;



    public Movie(JSONObject jsonObject) throws JSONException{
         this.id = jsonObject.getInt("id");
         this.posterPath = jsonObject.getString("poster_path");
         this.originalTitle = jsonObject.getString("original_title");
         this.overview = jsonObject.getString("overview");
         this.backdropImage = jsonObject.getString("backdrop_path");
         this.voteAverage = jsonObject.getDouble("vote_average");
         if (jsonObject.has("spoken_language"))
            this.originalLanguage = jsonObject.getJSONArray("spoken_language") != null ? jsonObject.getJSONObject("spoken_language").getString("name") : "";
         if (jsonObject.has("status"))
            this.status = jsonObject.getString("status");
         if (jsonObject.has("release_date"))
            this.setReleaseDate(jsonObject.getString("release_date"));
         if (jsonObject.has("genres"))
            this.genres = jsonObject.getJSONArray("genres") != null ? ((JSONObject)jsonObject.getJSONArray("genres").get(0)).getString("name") : "";

    }

    public Movie(){}

    public String getMovieKey() {
        return movieKey;
    }

    public void setMovieKey(JSONObject jsonObject) {
        try{
            if (jsonObject.has("key"))
            this.movieKey = jsonObject.getString("key");
        }catch (Exception ex){}
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getReleaseDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        return sdf.format(releaseDate);

    }

    public void setReleaseDate(String releaseDate) {
        try{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        this.releaseDate = sdf.parse(releaseDate);

        }
        catch(Exception ex)
        {
            Log.d("debug", "setReleaseDate method : " + ex.getMessage());
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MovieType getMovieType(){
        if (getVoteAverage() >= 7)
            return MovieType.Popular;
        else
            return MovieType.Normal;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVote_average(double vote_average) {
        this.voteAverage = vote_average;
    }

    public int getNumStars(){

        return (int)Math.round(getVoteAverage()/2);
    }

    public String getBackdropImage() {
        return String.format("https://image.tmdb.org/t/p/w780/%s",backdropImage);
    }

    public String getLargeBackdropImage() {
        return String.format("https://image.tmdb.org/t/p/w780/%s",backdropImage);
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }


    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s",posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array){
        ArrayList<Movie> result = new ArrayList<Movie>();

        for (int i = 0; i < array.length(); i++){
            try {
                result.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;
    }


    public enum MovieType {
        Popular, Normal
    }

}
