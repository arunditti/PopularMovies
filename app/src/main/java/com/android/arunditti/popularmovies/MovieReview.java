package com.android.arunditti.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arunditti on 12/21/15.
 */
public class MovieReview implements Parcelable {

    String movieId;
    String mAuthor;
    String mContent;
    //String mSite;
    //String mSize;
    // String mType;

    // public MovieTrailer(String id, String key, String name, String site, String size, String type) {
    public MovieReview(String id, String author, String content) {
        this.movieId = id;
        this.mAuthor = author;
        this.mContent = content;
        // this.mSite = site;
        //this.mSize = size;
        //this.mType = type;
    }

    public MovieReview(Parcel in) {
        movieId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        // mSite = in.readString();
        //mSize = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return movieId + mAuthor + mContent;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(movieId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel parcel) {
            return new MovieReview(parcel);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getItemId() {
        return movieId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mName) {
        this.mContent = mName;
    }
}
