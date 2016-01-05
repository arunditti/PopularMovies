package com.android.arunditti.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arunditti on 12/8/15.
 *
 *
 */

public class TrailerAdapter extends BaseAdapter {


    //public class TrailerAdapter extends ArrayAdapter<MovieTrailer> {
   public Context mContext;
    //int layoutResourceId;
    public ArrayList<MovieTrailer> movieTrailers;

    //create a class to hold your exact set of views
    public class ViewHolder {
        //public ImageView trailerImage;
        public TextView trailerTitle;
        public ViewHolder(View view) {
            //trailerImage = (ImageView) view.findViewById(R.id.trailer_image);
            trailerTitle = (TextView) view.findViewById(R.id.trailer_title);
        }
    }

    //public TrailerAdapter(Context mContext, int layoutResourceId, ArrayList<MovieTrailer> movieTrailers) {
    public TrailerAdapter(Context mContext, ArrayList<MovieTrailer> movieTrailers) {
        //super(mContext, R.layout.fragment_detail, movieTrailers);
        super();
        this.mContext = mContext;
        //this.layoutResourceId = layoutResourceId;
        this.movieTrailers = movieTrailers;
    }

   /** public void updateTrailerList(ArrayList<MovieTrailer> movieTrailers) {
        this.movieTrailers = movieTrailers;
        this.notifyDataSetChanged();
    }*/

   /** public void add(MovieTrailer movieTrailers) {
                mMovieTrailers.add(movieTrailers);
        notifyDataSetChanged();
    }*/

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieTrailer object from the ArrayAdapter at the appropriate position
        ViewHolder viewHolder;
        MovieTrailer movieTrailer = movieTrailers.get(position);
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.trailer_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

       //viewHolder.trailerTitle.setText(movieTrailers.get(position).getTrailerName());
        viewHolder.trailerTitle.setText(movieTrailer.mName);

      /** String yt_thumbnail_url = "http:/ TextView trailerName = (TextView) view.findViewById(R.id.listView_trailers);/img.youtube.com/vi/" + movieTrailers.get(position).getKey() + "/0.jpg";
       Picasso.with(mContext)
                .load(yt_thumbnail_url)
                .fit()
                .into(viewHolder.trailerImage);*/
       /** Picasso.with(mContext)
                .load(movieTrailer.getTrailerImage())
                .fit()
                .into(viewHolder.trailerImage);
*/
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        //return movieTrailers.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if(movieTrailers == null) return 0;   //added
        return movieTrailers.size();
    }

    @Override
    public long getItemId(int position) {
        //return position;
        return 0;
    }

}
