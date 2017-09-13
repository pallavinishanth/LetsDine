package com.pallavinishanth.android.letsdine.Network;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 7/26/17.
 */

public class DetailResult implements Parcelable {

    private String formatted_address;

    private String international_phone_number;

    private String name;

    private DetailOpeningHours opening_hours;

    private String website;

    private String icon;

    private ArrayList<Reviews> reviews = new ArrayList<Reviews>();

    private ArrayList<DetailPhotos> photos = new ArrayList<DetailPhotos>();

    public String getAddress() {

        return formatted_address;
    }

    public void setAddress(String address) {

        this.formatted_address = address;
    }

    public String getPhoneNum() {

        return international_phone_number;
    }

    public void setPhoneNum(String PhNo) {

        this.international_phone_number = PhNo;
    }

    public String getname() {

        return name;
    }

    public void setname(String name) {

        this.name = name;
    }

    /*
     * opening_hours getter
     */
    public DetailOpeningHours getDetailOpeningHours() {
        return opening_hours;
    }

    /*
     * opening_hours setter
     */
    public void setDetailOpeningHours(DetailOpeningHours openingHours) {
        this.opening_hours = openingHours;
    }

    public String getWebsite() {

        return website;
    }

    public void setWebsite(String website) {

        this.website = website;
    }

    public String geticon() {

        return icon;
    }

    public void seticon(String icon) {

        this.icon = icon;
    }

    public ArrayList<Reviews> getReviews() {

        return reviews;
    }

    public void setReviews(ArrayList<Reviews> reviews) {

        this.reviews = reviews;
    }

    /*
     * photos getter
     */
    public ArrayList<DetailPhotos> getPhotos() {
        return photos;
    }

    /*
     * photos setter
     */
    public void setPhotos(ArrayList<DetailPhotos> photos) {
        this.photos = photos;
    }

    private DetailResult(Parcel in) {

        formatted_address = in.readString();
        international_phone_number = in.readString();
        name = in.readString();
        opening_hours = (DetailOpeningHours) in.readValue(DetailOpeningHours.class.getClassLoader());
        website = in.readString();
        icon = in.readString();

        reviews = new ArrayList<Reviews>();
        in.readTypedList(reviews, Reviews.CREATOR);

        photos = new ArrayList<DetailPhotos>();
        in.readTypedList(photos, DetailPhotos.CREATOR);
    }

    public DetailResult() {

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(formatted_address);
        parcel.writeString(international_phone_number);
        parcel.writeString(name);
        parcel.writeValue(opening_hours);
        parcel.writeString(website);
        parcel.writeString(icon);
        parcel.writeList(reviews);
        parcel.writeList(photos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DetailResult> CREATOR = new Parcelable.Creator<DetailResult>() {

        @Override
        public DetailResult createFromParcel(Parcel parcel) {
            return new DetailResult(parcel);
        }

        @Override
        public DetailResult[] newArray(int i) {
            return new DetailResult[i];
        }
    };
}
