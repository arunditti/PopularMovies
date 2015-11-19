package com.android.arunditti.popularmovies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.arunditti.popularmovies.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arunditti on 11/3/15.
 */


public class PopularMovieAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieItem> movieItems;

    //create a class to hold your exact set of views
    public class ViewHolder {
        public ImageView imageView;
        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        }
    }

    public PopularMovieAdapter(Context mContext, List<MovieItem> movieItems) {
        super();
        this.mContext = mContext;
        this.movieItems = movieItems;
    }

    public void updateMovieList(List<MovieItem> movieItems) {
        this.movieItems = movieItems;
        this.notifyDataSetChanged();
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieItem object from the ArrayAdapter at the appropriate position
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.grid_item_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext)
                .load(movieItems
                        .get(position).mImagePath)
                .fit()
                .into(viewHolder.imageView);

        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return movieItems.get(i);
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}