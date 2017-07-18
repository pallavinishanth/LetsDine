package com.pallavinishanth.android.letsdine;

import android.content.Context;
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


    public ResDataAdapter(Context context, List<Results> resresults){

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

        Glide.with(rContext).load(res_results.get(position).getIcon()).into(holder.rImageView);


        holder.res_name_view.setText(res_results.get(position).getName());
        holder.res_address_view.setText(res_results.get(position).getVicinity());

        if(res_results.get(position).getRating() == null){

            holder.res_rating_view.setText("Rating: - -");
        } else{

            holder.res_rating_view.setText("Rating: " + Double.toString(res_results.get(position).getRating()));
        }

        if(res_results.get(position).getPriceLevel() != null) {

            switch (res_results.get(position).getPriceLevel()) {

                case 0:
                    holder.res_pricelevel_view.setText("0");
                    break;
                case 1:
                    holder.res_pricelevel_view.setText("$");
                    break;
                case 2:
                    holder.res_pricelevel_view.setText("$$");
                    break;
                case 3:
                    holder.res_pricelevel_view.setText("$$$");
                    break;
                case 4:
                    holder.res_pricelevel_view.setText("$$$$");
                    break;
                default:
                    break;
            }
        }else{
            holder.res_pricelevel_view.setText("- - - -");
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

        public ViewHolder(final View view) {
            super(view);

            rImageView = (ImageView) view.findViewById(R.id.res_image);
            res_name_view = (TextView) view.findViewById(R.id.res_name);
            res_address_view = (TextView) view.findViewById(R.id.res_vicinity);
            res_pricelevel_view = (TextView) view.findViewById(R.id.res_price);
            res_rating_view = (TextView) view.findViewById(R.id.res_rating);
        }
    }
}
