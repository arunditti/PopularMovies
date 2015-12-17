//package com.android.arunditti.popularmovies;

/**import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
import java.util.List;*/

import com.android.arunditti.popularmovies.MovieTrailer;

/**
 * Created by arunditti on 12/8/15.
 */
/**public class FetchMovieTrailerTask extends AsyncTask<String, Void, List<MovieTrailer>>  {
    private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();
    private List<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
    final String TRAILER_RESULTS = "results";
    final String TRAILER_ID = "id";
    final String TRAILER_KEY = "key";
    final String TRAILER_NAME = "name";
    final String TRAILER_SITE = "site";
    final String TRAILER_SIZE = "size";
    final String TRAILER_TYPE = "type";

    private List<MovieTrailer> getTrailerDataFromJson(String TrailerJsonStr)
            throws JSONException {

        JSONObject TrailerJson = new JSONObject(TrailerJsonStr);
        JSONArray trailersArray = TrailerJson.getJSONArray(TRAILER_RESULTS);

        movieTrailers.clear();
        for (int i = 0; i < trailersArray.length(); i++) {
            JSONObject movieTrailerJson = trailersArray.getJSONObject(i);
            String id = movieTrailerJson.getString(TRAILER_ID);
            String key = movieTrailerJson.getString(TRAILER_KEY);
            String name= movieTrailerJson.getString(TRAILER_NAME);
           // String site= movieTrailerJson.getString(TRAILER_SITE);
            //String size= movieTrailerJson.getString(TRAILER_SIZE);
            //String type = movieTrailerJson.getString(TRAILER_TYPE);
            //movieTrailers.add(new MovieTrailer(id, key, name, site, size, type));
            movieTrailers.add(new MovieTrailer(id, key, name));
        }

        return movieTrailers;

    }
    @Override
    protected List<MovieTrailer> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String TrailerJsonStr = null;

        try {
            // Construct the URL for the picasso popular movies query


            final String POPULAR_MOVIE_BASE_URL =  "http://api.themoviedb.org/3/movie";
            final String API_KEY = "api_key";

            //public static final String YOU_TUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";
           // public static final String YOU_TUBE_IMG_URL = "http://img.youtube.com/vi/%s/default.jpg";


            Uri builtUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                    .appendPath("id")
                    .appendPath("videos")
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

            TrailerJsonStr = buffer.toString();

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
                   // Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getTrailerDataFromJson(TrailerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the movie.
        return null;
    }

    @Override
    protected void onPostExecute (List<MovieTrailer> result){
        if (result != null) {
            //mTrailerAdapter.clear();
            for (MovieTrailer trailer : result) {
               // mTrailerAdapter.add(trailer);
            }
        }
    }
}*/
