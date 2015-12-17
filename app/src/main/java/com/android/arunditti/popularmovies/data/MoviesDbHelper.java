package com.android.arunditti.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.arunditti.popularmovies.data.MoviesContract.MoviesEntry;


/**
 * Created by arunditti on 12/3/15.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_PICTURE_PATH + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_DURATION + " INTEGER NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " INTEGER NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_RATING + " REAL NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_FAVORITE + " INTEGER NOT NULL, " +
                " );";


                //" UNIQUE (" + MoviesEntry.COLUMN_MOVIE_TITLE + ") ON CONFLICT REPLACE);";

                // Set up the location column as a foreign key to location table.
              //  " FOREIGN KEY (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
               // MoviesContract.SortEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
               // " UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
               // WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";


      //  final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + MoviesContract.ReviewsEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
              // MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
               /** MoviesContract.ReviewsEntry.COLUMN_REVIEW_ID + " INTEGER NOT NULL, " +
                MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                MoviesContract.ReviewsEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ")," +
                "UNIQUE (" + MoviesContract.ReviewsEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE);";*/

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        //sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavouriteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
