package com.android.arunditti.popularmovies;

import android.app.usage.UsageEvents;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.android.arunditti.popularmovies.PopularMovieAdapter;


/**
 * Created by arunditti on 12/7/15.
 */

// This application is not using this file. so can be deleted
public class FetchPopularMoviesTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {
    public FetchPopularMoviesTask() {

    }

    private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();

    private PopularMovieAdapter mPopularMovieAdapter;
    private ArrayList<MovieItem> movieItems = new ArrayList<MovieItem>();

    // These are the names of the JSON objects that need to be extracted.
    final String PMD_ID = "id";
    final String PMD_LIST = "results";
    final String PMD_TITLE = "title";
    final String PMD_POSTER = "poster_path";
    final String PMD_PICTURE_PATH = "https://image.tmdb.org/t/p/";
    final String PMD_OVERVIEW = "overview";
    final String PMD_RATING = "vote_average";
    final String PMD_RELEASE_DATE = "release_date";
    final String PMD_PICTURE_SIZE    = "w185";

    private ArrayList<MovieItem> getPopularMoviesDataFromJson(String PopularMoviesJsonStr)
            throws JSONException {

        JSONObject PopularMoviesJson = new JSONObject(PopularMoviesJsonStr);
        JSONArray moviesArray = PopularMoviesJson.getJSONArray(PMD_LIST);

        movieItems.clear();
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject popularMovies = moviesArray.getJSONObject(i);
            String id = popularMovies.getString(PMD_ID);
            String title = popularMovies.getString(PMD_TITLE);
            String releaseDate = popularMovies.getString(PMD_RELEASE_DATE);
            String movieOverview= popularMovies.getString(PMD_OVERVIEW);
            String rating= popularMovies.getString(PMD_RATING);
            String imagePath = PMD_PICTURE_PATH + PMD_PICTURE_SIZE + popularMovies.getString(PMD_POSTER);
            movieItems.add(new MovieItem(id, title, releaseDate, movieOverview, rating, imagePath));
        }

        return movieItems;

    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String PopularMoviesJsonStr = null;

        try {
            // Construct the URL for the picasso popular movies query

            final String POPULAR_MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY = "sort_by";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY, params[0])
                    .appendQueryParameter(API_KEY, BuildConfig.PICASSO_API_KEY)
                    .build();
            URL url = new URL(builtUri.toString());

            // Create the request to Picasso, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            PopularMoviesJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movies data, there's no point in attemping
            // to parse it.
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getPopularMoviesDataFromJson(PopularMoviesJsonStr);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the movie.
        return null;
    }

    @Override
    protected void onPostExecute (ArrayList<MovieItem> result){
        if (result != null) {
            mPopularMovieAdapter.updateMovieList(movieItems);
        }
    }
}