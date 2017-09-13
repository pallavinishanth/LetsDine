package com.pallavinishanth.android.letsdine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pallavinishanth.android.letsdine.Network.Reviews;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 8/8/17.
 */

public class ResReviewsAdapter extends RecyclerView.Adapter<ResReviewsAdapter.ViewHolder> {

    private Context rContext;

    ArrayList<Reviews> res_reviews;

    public ResReviewsAdapter(Context context, ArrayList<Reviews> res_reviews) {

        this.rContext = context;
        this.res_reviews = res_reviews;

    }

    @Override
    public void onBindViewHolder(ResReviewsAdapter.ViewHolder holder, int position) {

        if (!res_reviews.get(position).getAuthor_name().isEmpty()) {

            holder.rAuthor.setText(res_reviews.get(position).getAuthor_name());
            holder.rAuthor.setContentDescription(res_reviews.get(position).getAuthor_name());
        } else {
            holder.rAuthor.setText(R.string.Aname_notfound);
            holder.rAuthor.setContentDescription(rContext.getString(R.string.Aname_notfound));
        }

        holder.rRating.setText(R.string.rating + Integer.toString(res_reviews.get(position).getRating()));

        if (!res_reviews.get(position).getText().isEmpty()) {

            holder.rAuthorText.setText(res_reviews.get(position).getText());
            holder.rAuthorText.setContentDescription(res_reviews.get(position).getText());
        } else {
            holder.rAuthorText.setText(R.string.review_notfound);
            holder.rAuthorText.setContentDescription(rContext.getString(R.string.review_notfound));
        }

    }

    @Override
    public int getItemCount() {

        return res_reviews.size();
    }

    @Override
    public ResReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View rview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_view, parent, false);

        return new ViewHolder(rview);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView rAuthor;
        TextView rAuthorText;
        TextView rRating;

        public ViewHolder(final View view) {
            super(view);

            rAuthor = (TextView) view.findViewById(R.id.author_name);
            rAuthorText = (TextView) view.findViewById(R.id.author_text);
            rRating = (TextView) view.findViewById(R.id.author_rating);
        }
    }

}
