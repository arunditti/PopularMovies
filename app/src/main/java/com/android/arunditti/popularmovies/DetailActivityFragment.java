package com.android.arunditti.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.arunditti.popularmovies.MovieItem;
import com.android.arunditti.popularmovies.MovieTrailer;
import com.android.arunditti.popularmovies.TrailerAdapter;
import com.android.arunditti.popularmovies.DetailActivity;
import com.android.arunditti.popularmovies.ReviewActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public static final String YOU_TUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";
    public static final String YOUTUBE_URI = "vnd.youtube:";
    final String TRAILER_KEY = "key";
    private static final String YOUTUBE_IMAGE_URL_PREFIX = "http://img.youtube.com/vi/";
    private static final String YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg";
    private MovieItem movieItem;
    ListView listView;
    private PopularMovieAdapter mPopularMovieAdapter;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
   // private List<MovieTrailer> movieTrailers;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null || !savedInstanceState.containsKey("movieTrailers")) {
            movieTrailers = new ArrayList<MovieTrailer>();
        } else {
            movieTrailers = savedInstanceState.getParcelableArrayList("movieTrailers");
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

       /** Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }*/

        ImageView movieImage = (ImageView) rootView.findViewById(R.id.movie_image);

       final MovieItem movieItem = (MovieItem)getActivity().getIntent().getParcelableExtra("MovieItem");
        ((TextView) rootView.findViewById(R.id.movie_title))
                .setText("Movie Title: " + movieItem.mMovieTitle);
        ((TextView) rootView.findViewById(R.id.movie_release_date))
                .setText("Movie Release Date: " + movieItem.mMovieReleaseDate);
        ((TextView) rootView.findViewById(R.id.movie_rating))
                .setText("Movie Rating: " + movieItem.mRating);
        ((TextView) rootView.findViewById(R.id.movie_overview))
                .setText("Movie Overview: \n" + movieItem.mOverview);

        Picasso.with(getActivity()).load(movieItem.mImagePath).fit().into(movieImage);

       // Picasso.with(getActivity()).load(movieTrailer.getTrailerImage()).fit().into(trailerImage);


        // Get a reference to the ListView, and attach this adapter to it.

        ListView listView = ((ListView) rootView.findViewById(R.id.listview_trailers));
        mTrailerAdapter = new TrailerAdapter(getActivity(), movieTrailers);
        listView.setAdapter(mTrailerAdapter);


          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String VideoKey = mTrailerAdapter.movieTrailers.get(position).mKey;

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URI + VideoKey));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOU_TUBE_VIDEO_URL + VideoKey));
                        startActivity(intent);
                    }
                }
            });

        Button mReviewsButton = (Button) rootView.findViewById(R.id.movie_reviews);
        mReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("Id", movieItem.movieId);
                startActivity(intent);
            }
        });

        FetchMovieTrailerTask movieTrailersTask = new FetchMovieTrailerTask();
        movieTrailersTask.execute(movieItem.movieId);


        

        return rootView;
    }

    public class FetchMovieTrailerTask extends AsyncTask<String, Void, ArrayList<MovieTrailer>> {

        private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();
    //ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();

        //private TrailerAdapter mTrailerAdapter;
       //private List<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();

        // These are the names of the JSON objects that need to be extracted.
        final String TRAILER_RESULTS = "results";
        final String MOVIE_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
      //  final String TRAILER_SITE = "site";
        //final String TRAILER_SIZE = "size";
        //final String TRAILER_TYPE = "type";

        private ArrayList<MovieTrailer> getTrailerDataFromJson(String TrailerJsonStr)
                throws JSONException {

            ArrayList<MovieTrailer> resultMovieTrailers = new ArrayList<MovieTrailer>();
            JSONObject TrailerJson = new JSONObject(TrailerJsonStr);
            JSONArray trailersArray = TrailerJson.getJSONArray(TRAILER_RESULTS);

            resultMovieTrailers.clear();
            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject movieTrailerJson = trailersArray.getJSONObject(i);
                String id = movieTrailerJson.getString(MOVIE_ID);
                String key = movieTrailerJson.getString(TRAILER_KEY);
                String name= movieTrailerJson.getString(TRAILER_NAME);
              //  String site= movieTrailerJson.getString(TRAILER_SITE);
                //String size= movieTrailerJson.getString(TRAILER_SIZE);
                //String type = movieTrailerJson.getString(TRAILER_TYPE);
               // movieTrailers.add(new MovieTrailer(id, key, name, site, size, type));
               MovieTrailer mT = new MovieTrailer(id, key, name);
                resultMovieTrailers.add(mT);
            }

            for (MovieTrailer s : resultMovieTrailers) {
                Log.v(LOG_TAG, "Trailer Entry: " + s);
                Log.v(LOG_TAG, "Key: " + s.mKey);
            }

            return resultMovieTrailers;

        }

    @Override
        protected ArrayList<MovieTrailer> doInBackground(String... params) {
String movieId;

           // MovieItem movieItem = (MovieItem)getActivity().getIntent().getParcelableExtra("MovieItem");

            // If there's no zip code, there's nothing to look up.  Verify size of params.
         if (params.length == 0) {
            return null;
        }

        movieId = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String TrailerJsonStr = null;

            try {
                 // Construct the URL for the picasso popular movies query


               final String POPULAR_MOVIE_BASE_URL= "http://api.themoviedb.org/3/movie";
                final String API_KEY = "api_key";

                //public static final String YOU_TUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";
                // public static final String YOU_TUBE_IMG_URL = "http://img.youtube.com/vi/%s/default.jpg";


                Uri builtUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                         .appendPath(movieId)
                        .appendPath("videos")
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
            protected void onPostExecute (ArrayList<MovieTrailer> result) {
            super.onPostExecute(result);
            if (result != null) {
                 mTrailerAdapter.movieTrailers = result;
                 mTrailerAdapter.notifyDataSetChanged();
          }

      }
  }
}
