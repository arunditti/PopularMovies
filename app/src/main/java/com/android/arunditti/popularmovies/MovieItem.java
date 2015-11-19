package com.android.arunditti.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by arunditti on 11/3/15.
 */

public class MovieItem implements Parcelable{

    String mMovieTitle;
    String mMovieReleaseDate;
    String mOverview;
    String mRating;
    String mImagePath;

    public MovieItem(String title, String releaseDate, String movieOverview, String rating, String imagePath) {
        this.mMovieTitle = title;
        this.mMovieReleaseDate = releaseDate;
        this.mOverview = movieOverview;
        this.mImagePath = imagePath;
        this.mRating = rating;
    }

    private MovieItem(Parcel in) {
        mMovieTitle = in.readString();
        mMovieReleaseDate = in.readString();
        mOverview = in.readString();
        mImagePath = in.readString();
        mRating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return mMovieTitle + mMovieReleaseDate + mOverview + mImagePath + mRating;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mMovieTitle);
        dest.writeString(mMovieReleaseDate);
        dest.writeString(mOverview);
        dest.writeString(mImagePath);
        dest.writeString(mRating);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}