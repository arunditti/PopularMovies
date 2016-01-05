package com.android.arunditti.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by arunditti on 12/3/15.
 */
public class MoviesContract {

    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "movies";

        // Column with the foreign key into the movies table.
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Column for movie title
        public static final String COLUMN_MOVIE_TITLE = "title";

        // Column for movie picture path
        public static final String COLUMN_PICTURE_PATH = "https://image.tmdb.org/t/p/";

        // Column for movie poster
        public static final String COLUMN_MOVIE_POSTER = "poster_path";

        // Column for duration of movie
        public static final String COLUMN_MOVIE_DURATION = "duration";

        // Column for movie release date
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        // Column for movie vote average
        public static final String COLUMN_MOVIE_RATING = "vote_average";

        // Column for movie overview
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        // Column for favourite movie
        public static final String COLUMN_MOVIE_FAVORITE = "favorite";
    }

    //  Inner class that defines the table contents of the reviews table
    public static final class ReviewsEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "reviews";

        // Column with the foreign key into the movies table.
        public static final String COLUMN_REVIEW_ID = "review_id";

        // Column for movie id
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Column for author
        public static final String COLUMN_REVIEW_AUTHOR = "author";

        // Column for content
        public static final String COLUMN_REVIEW_CONTENT = "content";

    }
}
