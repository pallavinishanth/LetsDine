package com.pallavinishanth.android.letsdine.Network;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class DetailPhotos implements Parcelable {

    private String photo_reference;
    private Integer height;
    private Integer width;
    private List<String> htmlAttributions = new ArrayList<String>();

    /*
     * photo_reference getter
     */
    public String getPhotoReference() {
        return photo_reference;
    }

    /*
     * photo_reference setter
     */
    public void setPhotoReference(String photoReference) {
        this.photo_reference = photoReference;
    }

    /*
     * height getter
     */
    public Integer getHeight() {
        return height;
    }

    /*
     * height setter
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /*
     * width getter
     */
    public Integer getWidth() {
        return width;
    }

    /*
     * width setter
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /*
     * htmlAttributions getter
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    /*
     * html_attributions setter
     */
    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private DetailPhotos(Parcel in){
        photo_reference = in.readString();
        height = in.readInt();
        width = in.readInt();
        htmlAttributions = new ArrayList<String>();
        in.readStringList(htmlAttributions);

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(photo_reference);
        parcel.writeInt(height);
        parcel.writeInt(width);
        parcel.writeStringList(htmlAttributions);

    }

    public static final Creator<DetailPhotos> CREATOR = new Creator<DetailPhotos>(){

        @Override
        public DetailPhotos createFromParcel(Parcel parcel) {
            return new DetailPhotos(parcel);
        }

        @Override
        public DetailPhotos[] newArray(int i) {
            return new DetailPhotos[i];
        }
    };
}
