package com.when0matters.xflicks.helpers;

import org.json.JSONArray;

/**
 * Created by dongdong on 2/12/2017.
 */

public class MovieAPIHelper{

    static JSONArray results;
    static String API_KEY = "7e42e01c92d8fc627c0711916b6c016a";
    //static OkHttpClient client;

    private static MovieAPIHelper instance = null;

    protected MovieAPIHelper() {
        //OkHttpClient client = new OkHttpClient();


    }

    public static MovieAPIHelper getInstance() {
        if(instance == null) {
            instance = new MovieAPIHelper();
        }
        return instance;
    }

    public JSONArray getNowPlayingMovies(){
        String url = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s", API_KEY);
        return getResult(url);
    }


    public JSONArray getMovieByID(int movieId){
        String url = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US", movieId, API_KEY);
        return getResult(url);
    }

    private synchronized JSONArray getResult(String url){

//        client.get(url, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode,headers,response);
//                try{
//                    results = response.getJSONArray("results");
//                    if (results != null)
//                        Log.d("Debug",results.toString());
//                }catch (JSONException ex){
//                    ex.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                results = null;
//                Log.d("Debug", errorResponse.toString());
//            }
//        });

        return results;
    }
}
