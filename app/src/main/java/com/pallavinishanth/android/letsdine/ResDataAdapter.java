package com.pallavinishanth.android.letsdine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pallavinishanth.android.letsdine.Network.Results;

import java.util.List;

/**
 * Created by PallaviNishanth on 7/17/17.
 */

public class ResDataAdapter extends RecyclerView.Adapter<ResDataAdapter.ViewHolder> {

    private Context rContext;

    List<Results> res_results;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ResDataAdapter(Context context, List<Results> resresults) {

        this.rContext = context;
        this.res_results = resresults;

    }

    @Override
    public ResDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View rview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.res_card_view, parent, false);

        return new ViewHolder(rview);

    }

    @Override
    public void onBindViewHolder(ResDataAdapter.ViewHolder holder, int position) {


        if (res_results.get(position).getName() != null) {

            holder.res_name_view.setText(res_results.get(position).getName());
            holder.res_name_view.setContentDescription(res_results.get(position).getName());
        } else {
            holder.res_name_view.setText(R.string.name_not_found);
        }

        if (res_results.get(position).getVicinity() != null) {

            holder.res_address_view.setText(res_results.get(position).getVicinity());
            holder.res_address_view.setContentDescription(res_results.get(position).getVicinity());
        } else {
            holder.res_address_view.setText(R.string.address_not_found);
        }

        if (res_results.get(position).getOpeningHours() != null) {

            if (res_results.get(position).getOpeningHours().getOpenNow() == true) {

                holder.res_opening_hours.setText(R.string.open);
                holder.res_opening_hours.setTextColor(ContextCompat.getColor(rContext, R.color.open_green));
                holder.res_opening_hours.setContentDescription(rContext.getString(R.string.open));

            } else {

                holder.res_opening_hours.setText(R.string.closed);
                holder.res_opening_hours.setTextColor(ContextCompat.getColor(rContext, R.color.close_red));
                holder.res_opening_hours.setContentDescription(rContext.getString(R.string.closed));
            }
        }


        if (res_results.get(position).getPhotos() == null) {

            Glide.with(rContext).load(res_results.get(position).getIcon()).into(holder.rImageView);
        } else {


            Glide.with(rContext).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + res_results.get(position).getPhotos().get(0).getPhotoReference()
                    + "&key=" + BuildConfig.GOOGLE_PLACES_API_KEY).centerCrop().into(holder.rImageView);
        }

        if (res_results.get(position).getRating() == null) {

            holder.res_rating_view.setText(R.string.rating_null);
            holder.res_rating_view.setContentDescription(rContext.getString(R.string.rating_notfound));
        } else {

            holder.res_rating_view.setText("Rating: " + Double.toString(res_results.get(position).getRating()));
            holder.res_rating_view.setContentDescription("Rating" + Double.toString(res_results.get(position).getRating()));
        }

        if (res_results.get(position).getPriceLevel() != null) {

            switch (res_results.get(position).getPriceLevel()) {

                case 0:
                    holder.res_pricelevel_view.setText(R.string.zero);
                    holder.res_pricelevel_view.setContentDescription(rContext.getString(R.string.price_zero));
                    break;
                case 1:
                    holder.res_pricelevel_view.setText(R.string.single_dollar);
                    holder.res_pricelevel_view.setContentDescription(rContext.getString(R.string.single_dollarcd));
                    break;
                case 2:
                    holder.res_pricelevel_view.setText(R.string.double_dollar);
                    holder.res_pricelevel_view.setContentDescription(rContext.getString(R.string.double_dollarcd));
                    break;
                case 3:
                    holder.res_pricelevel_view.setText(R.string.triple_dollar);
                    holder.res_pricelevel_view.setContentDescription(rContext.getString(R.string.triple_dollarcd));
                    break;
                case 4:
                    holder.res_pricelevel_view.setText(R.string.four_dollar);
                    holder.res_pricelevel_view.setContentDescription(rContext.getString(R.string.four_dollarcd));
                    break;
                default:
                    break;
            }
        } else {
            holder.res_pricelevel_view.setText(R.string.noprice);
            holder.res_pricelevel_view.setContentDescription(rContext.getString(R.string.price_notfound));
        }

    }

    @Override
    public int getItemCount() {
        return res_results.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView rImageView;

        TextView res_name_view;
        TextView res_address_view;
        TextView res_pricelevel_view;
        TextView res_rating_view;
        TextView res_opening_hours;

        public ViewHolder(final View view) {
            super(view);

            rImageView = (ImageView) view.findViewById(R.id.res_image);
            res_name_view = (TextView) view.findViewById(R.id.res_name);
            res_address_view = (TextView) view.findViewById(R.id.res_vicinity);
            res_pricelevel_view = (TextView) view.findViewById(R.id.res_price);
            res_rating_view = (TextView) view.findViewById(R.id.res_rating);
            res_opening_hours = (TextView) view.findViewById(R.id.res_opening_hours);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, position);
                    }
                }
            });
        }
    }
}
