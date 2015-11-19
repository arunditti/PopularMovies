package com.android.arunditti.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.arunditti.popularmovies.MovieItem;

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


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private PopularMovieAdapter mPopularMovieAdapter;
    private List<MovieItem> movieItems = new ArrayList<MovieItem>();

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        mPopularMovieAdapter = new PopularMovieAdapter(getActivity().getApplicationContext(), movieItems);
        gridView.setAdapter(mPopularMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem movieItem = (MovieItem)mPopularMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("MovieItem", movieItem);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateMovieList() {
        FetchPopularMoviesTask movieTask = new FetchPopularMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_default));
        movieTask.execute(sort_by);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    public class FetchPopularMoviesTask extends AsyncTask<String, Void, List<MovieItem>> {

        private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();

        // These are the names of the JSON objects that need to be extracted.
        final String PMD_LIST = "results";
        final String PMD_TITLE = "title";
        final String PMD_POSTER = "poster_path";
        final String PMD_PICTURE_PATH = "https://image.tmdb.org/t/p/";
        final String PMD_OVERVIEW = "overview";
        final String PMD_RATING = "vote_average";
        final String PMD_RELEASE_DATE = "release_date";
        final String PMD_PICTURE_SIZE    = "w185";

        private List<MovieItem> getPopularMoviesDataFromJson(String PopularMoviesJsonStr)
                throws JSONException {

            JSONObject PopularMoviesJson = new JSONObject(PopularMoviesJsonStr);
            JSONArray moviesArray = PopularMoviesJson.getJSONArray(PMD_LIST);

            movieItems.clear();
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject popularMovies = moviesArray.getJSONObject(i);
                String title = popularMovies.getString(PMD_TITLE);
                String releaseDate = popularMovies.getString(PMD_RELEASE_DATE);
                String movieOverview= popularMovies.getString(PMD_OVERVIEW);
                String rating= popularMovies.getString(PMD_RATING);
                String imagePath = PMD_PICTURE_PATH + PMD_PICTURE_SIZE + popularMovies.getString(PMD_POSTER);
                movieItems.add(new MovieItem(title, releaseDate, movieOverview, rating, imagePath));
            }

            return movieItems;

        }

        @Override
        protected List<MovieItem> doInBackground(String... params) {

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
        protected void onPostExecute (List<MovieItem> result){
            if (result != null) {
                mPopularMovieAdapter.updateMovieList(movieItems);
            }
        }
    }
}