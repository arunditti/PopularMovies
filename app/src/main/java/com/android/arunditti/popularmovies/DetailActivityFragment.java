package com.android.arunditti.popularmovies;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.arunditti.popularmovies.MovieItem;
import com.android.arunditti.popularmovies.MovieTrailer;
import com.android.arunditti.popularmovies.TrailerAdapter;
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

    public static final String YOUTUBE_VIDEO_ID = "VIDEO_ID";
    public static final String YOUTUBE_URI = "vnd.youtube:";
    final String TRAILER_KEY = "key";
    private static final String YOUTUBE_IMAGE_URL_PREFIX = "http://img.youtube.com/vi/";
    private static final String YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg";
    ListView listView;

    private TrailerAdapter mTrailerAdapter;
    private List<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView movieImage = (ImageView) rootView.findViewById(R.id.movie_image);

        MovieItem movieItem = (MovieItem)getActivity().getIntent().getParcelableExtra("MovieItem");
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

        MovieTrailer movieTrailer = (MovieTrailer)getActivity().getIntent().getParcelableExtra("MovieTrailer");
//        new FetchMovieTrailerTask().execute(movieTrailer.getItemId());
        ((TextView) rootView.findViewById(R.id.trailer_title))
                .setText("Trailer: \n" + movieTrailer.mName);


        //new FetchMovieTrailerTask().execute(movieItem.getItemId());

           /** ((TextView) rootView.findViewById(R.id.movie_trailers_title))
                .setText("Trailer: \n" + movieItem.mMovieTitle);*/


        // Get a reference to the ListView, and attach this adapter to it.

        ListView listView = ((ListView) rootView.findViewById(R.id.listview_trailers));
        mTrailerAdapter = new TrailerAdapter(getActivity().getApplicationContext(), movieTrailers);

        listView.setAdapter(mTrailerAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    String youTubeKey = TRAILER_KEY;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youTubeKey));
                    intent.putExtra("VIDEO_ID", youTubeKey);
                    startActivity(intent);
                }
            });

      //  new FetchMovieTrailerTask().execute(movieItem.getItemId());

       //String[] trailerui = {"http://api.themoviedb.org/3/movie/" + movieItem.getItemId() + "/videos?api_key=PICASSO_API_KEY"};
       //new FetchMovieTrailerTask().execute(trailerui);

        return rootView;
    }

    private void updateTrailerList() {
       // MovieTrailer movieTrailer = (MovieTrailer)getActivity().getIntent().getParcelableExtra("MovieTrailer");
        FetchMovieTrailerTask movieTask = new FetchMovieTrailerTask();
       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String youtube_URI = prefs.getString(getString(R.string.pref_location_key),
          //      getString(R.string.pref_location_default));
        //String[] trailerParam = {"http://api.themoviedb.org/3/movie/" + movieItem.getItemId() + "/videos?api_key=PICASSO_API_KEY"};
        // new FetchMovieTrailerTask().execute(trailerParam);
        movieTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTrailerList();
    }

    public class FetchMovieTrailerTask extends AsyncTask<String, Void, List<MovieTrailer>> {

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

        private List<MovieTrailer> getTrailerDataFromJson(String TrailerJsonStr)
                throws JSONException {

            List<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
            JSONObject TrailerJson = new JSONObject(TrailerJsonStr);
            JSONArray trailersArray = TrailerJson.getJSONArray(TRAILER_RESULTS);

            movieTrailers.clear();
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
                movieTrailers.add(mT);
            }

            for (MovieTrailer s : movieTrailers) {
                Log.v(LOG_TAG, "Trailer Entry: " + s);
            }

            return movieTrailers;

        }

    @Override
        protected List<MovieTrailer> doInBackground(String... params) {

            MovieItem movieItem = (MovieItem)getActivity().getIntent().getParcelableExtra("MovieItem");
           // MovieTrailer movieTrailer = (MovieTrailer)getActivity().getIntent().getParcelableExtra("MovieTrailer");

            // If there's no zip code, there's nothing to look up.  Verify size of params.
      /**   if (params.length == 0) {
            return null;
        }*/

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
                         .appendPath(movieItem.mMovieId)
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
        protected void onPostExecute (List<MovieTrailer> result) {
      super.onPostExecute(result);
      ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();

        if (result != null) {
            movieTrailers = new ArrayList<>();
             mTrailerAdapter.updateTrailerList(movieTrailers);
      }
  }


  }
}
