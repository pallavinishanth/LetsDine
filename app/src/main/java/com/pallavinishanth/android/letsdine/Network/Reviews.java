package com.pallavinishanth.android.letsdine.Network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PallaviNishanth on 7/26/17.
 */

public class Reviews implements Parcelable {

    private String author_name;
    private int rating;
    private String text;

    public String getAuthor_name() {

        return author_name;
    }

    public void setAuthor_name(String authorName) {

        this.author_name = authorName;
    }

    public int getRating() {

        return rating;
    }

    public void setRating(int rating) {

        this.rating = rating;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Reviews(Parcel in) {

        author_name = in.readString();
        rating = in.readInt();
        text = in.readString();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(author_name);
        parcel.writeInt(rating);
        parcel.writeString(text);
    }

    public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {

        @Override
        public Reviews createFromParcel(Parcel parcel) {
            return new Reviews(parcel);
        }

        @Override
        public Reviews[] newArray(int i) {
            return new Reviews[i];
        }
    };
}
