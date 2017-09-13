package com.pallavinishanth.android.letsdine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pallavinishanth.android.letsdine.Network.DetailPhotos;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 7/24/17.
 */

public class ResPhotoAdapter extends RecyclerView.Adapter<ResPhotoAdapter.ViewHolder> {

    private Context rContext;

    ArrayList<DetailPhotos> res_photos;

    public ResPhotoAdapter(Context context, ArrayList<DetailPhotos> resPhotos) {

        this.rContext = context;
        this.res_photos = resPhotos;

    }

    @Override
    public void onBindViewHolder(ResPhotoAdapter.ViewHolder holder, int position) {

        Glide.with(rContext).load("https://maps.googleapis.com/maps/api/place/photo?maxheight=300&maxwidth=300&photoreference="
                + res_photos.get(position).getPhotoReference()
                + "&key=" + BuildConfig.GOOGLE_PLACES_API_KEY).centerCrop().into(holder.rphoto);
    }

    @Override
    public int getItemCount() {

        return res_photos.size();
    }

    @Override
    public ResPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View rview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo, parent, false);

        return new ViewHolder(rview);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView rphoto;


        public ViewHolder(final View view) {
            super(view);

            rphoto = (ImageView) view.findViewById(R.id.photo);

        }
    }
}
