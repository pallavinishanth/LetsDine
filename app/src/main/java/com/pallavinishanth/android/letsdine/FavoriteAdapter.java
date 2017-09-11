package com.pallavinishanth.android.letsdine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by PallaviNishanth on 9/9/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context rContext;
    private String[] resName;
    private String[] resAdd;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public FavoriteAdapter(Context context, String[] res_name, String[] res_add){

        this.rContext = context;
        this.resName = res_name;
        this.resAdd = res_add;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.restextview.setText(resName[position]);
        holder.restextview.setContentDescription(resName[position]);
        holder.resaddtextview.setText(resAdd[position]);
        holder.resaddtextview.setContentDescription(resAdd[position]);

    }

    @Override
    public int getItemCount() {
        return resName.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View rview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_card_view, parent, false);

        return new ViewHolder(rview);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView restextview;
        private final TextView resaddtextview;

        public ViewHolder(final View view) {
            super(view);

            restextview = (TextView) view.findViewById(R.id.fav_res_name);
            resaddtextview = (TextView) view.findViewById(R.id.fav_res_add);

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, position);
                    }
                }
            });
        }

    }
}
