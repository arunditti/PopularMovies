package com.android.arunditti.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arunditti on 12/8/15.
 */
public class MovieTrailer implements Parcelable{
    public static final String SITE_YOUTUBE = "YouTube";
    public static final String TYPE_TRAILER = "Trailer";

        String movieId;
        String mKey;
        String mName;
        //String mSite;
        //String mSize;
       // String mType;

   // public MovieTrailer(String id, String key, String name, String site, String size, String type) {
   public MovieTrailer(String id, String key, String name) {
        this.movieId = id;
        this.mKey = key;
        this.mName = name;
       // this.mSite = site;
        //this.mSize = size;
        //this.mType = type;
    }

    public MovieTrailer(Parcel in) {
        movieId = in.readString();
        mKey = in.readString();
        mName = in.readString();
       // mSite = in.readString();
        //mSize = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        //return mId + mKey + mName + mSite + mSize;
        return movieId + mKey + mName;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(movieId);
        dest.writeString(mKey);
        dest.writeString(mName);
       // dest.writeString(mSite);
        //dest.writeString(mSize);
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel parcel) {
            return new MovieTrailer(parcel);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getItemId() {
        return movieId;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getTrailerName() {
        return mName;
    }

    public void setTrailerName(String mName) {
        this.mName = mName;
    }

   /** public String getSite() {
        return mSite; }

    public String getType() {
        return mType; }*/

    public String getTrailerImage() {
        return "http://img.youtube.com/vi/" + mKey + "/0.jpg";
    }
}


