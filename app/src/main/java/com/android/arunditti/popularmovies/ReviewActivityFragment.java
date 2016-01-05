package com.android.arunditti.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.arunditti.popularmovies.ReviewAdapter;
import com.android.arunditti.popularmovies.MovieReview;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {

    final static private String TAG = ReviewActivityFragment.class.getCanonicalName();
    private ReviewAdapter mReviewAdapter;
    String movieId;

    public ReviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        movieId = getActivity().getIntent().getExtras().getString("Id");

        ArrayList<MovieReview> movieReviews = new ArrayList<>();

        ListView mListView = (ListView) rootView.findViewById(R.id.reviews_list);
        mReviewAdapter = new ReviewAdapter(getActivity(), movieReviews);
        mListView.setAdapter(mReviewAdapter);

        FetchMovieReviewTask movieReviewsTask = new FetchMovieReviewTask();
        movieReviewsTask.execute(movieId);

        return rootView;
    }

    public class FetchMovieReviewTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

        private final String LOG_TAG = FetchMovieReviewTask.class.getSimpleName();

        // These are the names of the JSON objects that need to be extracted.
        final String TRAILER_RESULTS = "results";
        final String MOVIE_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        private ArrayList<MovieReview> getReviewDataFromJson(String ReviewJsonStr)
                throws JSONException {

            ArrayList<MovieReview> resultMovieReviews = new ArrayList<MovieReview>();
            JSONObject ReviewJson = new JSONObject(ReviewJsonStr);
            JSONArray reviewsArray = ReviewJson.getJSONArray(TRAILER_RESULTS);

            resultMovieReviews.clear();
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject movieTrailerJson = reviewsArray.getJSONObject(i);
                String id = movieTrailerJson.getString(MOVIE_ID);
                String author = movieTrailerJson.getString(REVIEW_AUTHOR);
                String content= movieTrailerJson.getString(REVIEW_CONTENT);
                MovieReview mR = new MovieReview(id, author, content);
                resultMovieReviews.add(mR);
            }

            for (MovieReview s : resultMovieReviews) {
                Log.v(LOG_TAG, "Review Entry: " + s);
                Log.v(LOG_TAG, "Author Entry: " + s.mAuthor);
            }

            return resultMovieReviews;

        }

        @Override
        protected ArrayList<MovieReview> doInBackground(String... params) {
            String movieId;

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            movieId = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String ReviewJsonStr = null;

            try {
                // Construct the URL for the picasso popular movies query


                final String POPULAR_MOVIE_BASE_URL= "http://api.themoviedb.org/3/movie";
                final String API_KEY = "api_key";

                //public static final String YOU_TUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";
                // public static final String YOU_TUBE_IMG_URL = "http://img.youtube.com/vi/%s/default.jpg";


                Uri builtUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                        .appendPath(movieId)
                        .appendPath("reviews")
                        .appendQueryParameter(API_KEY, BuildConfig.PICASSO_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

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

                ReviewJsonStr = buffer.toString();

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
                return getReviewDataFromJson(ReviewJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the movie.
            return null;
        }

        @Override
        protected void onPostExecute (ArrayList<MovieReview> result) {
            super.onPostExecute(result);
            if (result != null) {
                mReviewAdapter.movieReviews = result;
                mReviewAdapter.notifyDataSetChanged();
            }

        }
    }

}
