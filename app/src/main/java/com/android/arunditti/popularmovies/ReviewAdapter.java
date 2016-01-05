package com.android.arunditti.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arunditti on 12/18/15.
 */
public class ReviewAdapter extends BaseAdapter {

    public Context mContext;
    ArrayList<MovieReview> movieReviews;

    public class ViewHolder {
    public TextView reviewAuthor;
    public TextView reviewContent;
    public ViewHolder(View view) {
        reviewAuthor = (TextView) view.findViewById(R.id.review_author);
        reviewContent = (TextView) view.findViewById(R.id.review_content);
    }
}

    public ReviewAdapter(Context mContext, ArrayList<MovieReview> movieReviews) {
        super();
        this.mContext = mContext;
        this.movieReviews = movieReviews;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        MovieReview movieReview = movieReviews.get(position);
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.review_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.reviewAuthor.setText(movieReview.mAuthor);
        viewHolder.reviewContent.setText(movieReview.mContent);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        //return movieTrailers.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if(movieReviews == null) return 0;   //added
        return movieReviews.size();
    }

    @Override
    public long getItemId(int position) {
        //return position;
        return 0;
    }
}
