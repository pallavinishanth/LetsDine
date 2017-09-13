package com.pallavinishanth.android.letsdine;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pallavinishanth.android.letsdine.Network.DetailResult;

/**
 * Created by PallaviNishanth on 8/17/17.
 */

public class ResDetailAdapter extends RecyclerView.Adapter<ResDetailAdapter.ViewHolder> {

    private Context rContext;
    DetailResult detailResult;
    private final String LOG_TAG = ResDetailAdapter.class.getSimpleName();

    public ResDetailAdapter(Context context, DetailResult detail_result) {

        this.rContext = context;
        this.detailResult = detail_result;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (detailResult.getWebsite() != null) {

            Log.v(LOG_TAG, "Place website " + detailResult.getWebsite());
            holder.website_view.setText(detailResult.getWebsite());

        } else {
            holder.website_view.setText(R.string.url_not_found);
        }

        if (detailResult.getPhoneNum() != null) {

            Log.v(LOG_TAG, detailResult.getPhoneNum());
            holder.phoneview.setText(detailResult.getPhoneNum());

        } else {

            holder.phoneview.setText(R.string.phoneNum_not_found);
        }

        if (detailResult.getDetailOpeningHours() != null) {

            for (int i = 0; i < 7; i++) {
                holder.hours_view.append(detailResult.getDetailOpeningHours().getweekhours()[i]);
                holder.hours_view.append("\n");
                Log.v(LOG_TAG, "hours " + (detailResult.getDetailOpeningHours().getweekhours()[i]));
            }

        } else {
            holder.hours_view.setText(R.string.hours_not_found);
        }

        if (detailResult.getAddress() != null) {

            Log.v(LOG_TAG, detailResult.getAddress());
            holder.address.setText(detailResult.getAddress());
        } else {
            holder.address.setText(R.string.address_not_found);
        }

        if (detailResult.getname() != null) {

            holder.collapsingToolbar.setTitle(detailResult.getname());
            holder.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            holder.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        } else {
            holder.collapsingToolbar.setTitle(rContext.getString(R.string.name_not_found));
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View rview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.res_detail_view, parent, false);

        return new ViewHolder(rview);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView hours_view;
        ImageView res_Photo;
        TextView website_view;
        TextView address;
        ImageView backdrop;
        TextView photoHeading;
        TextView reviewHeading;
        ImageView mapImage;
        TextView phoneview;
        ImageView phoneicon;
        CollapsingToolbarLayout collapsingToolbar;

        public ViewHolder(final View view) {
            super(view);


            website_view = (TextView) view.findViewById(R.id.website_url);
            hours_view = (TextView) view.findViewById(R.id.hours);
            address = (TextView) view.findViewById(R.id.address);
            backdrop = (ImageView) view.findViewById(R.id.res_backdrop);
            photoHeading = (TextView) view.findViewById(R.id.photos_heading);
            reviewHeading = (TextView) view.findViewById(R.id.Reviews_heading);
            mapImage = (ImageView) view.findViewById(R.id.map_image);
            phoneview = (TextView) view.findViewById(R.id.phoneNum);
            phoneicon = (ImageView) view.findViewById(R.id.phoneIcon);
            collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        }

    }
}
