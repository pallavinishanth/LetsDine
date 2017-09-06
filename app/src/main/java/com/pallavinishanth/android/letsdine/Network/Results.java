package com.pallavinishanth.android.letsdine.Network;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class Results implements Parcelable{

    private String icon;
    private String id;
    private Geometry geometry;
    private String name;
    private OpeningHours opening_hours;
    private ArrayList<Photos> photos = new ArrayList<Photos>();
    private String place_id;
    private String scope;
    private Integer price_level;
    private Double rating;
    private ArrayList<String> types = new ArrayList<String>();
    private String vicinity;


    /*
     * icon getter
     */
    public String getIcon() {
        return icon;
    }

    /*
     * icon setter
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /*
     * id getter
     */
    public String getId() {
        return id;
    }

    /*
     * id setter
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * geometry getter
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /*
     * geometry setter
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /*
     * name getter
     */
    public String getName() {
        return name;
    }

    /*
     * name setter
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * opening_hours getter
     */
    public OpeningHours getOpeningHours() {
        return opening_hours;
    }

    /*
     * opening_hours setter
     */
    public void setOpeningHours(OpeningHours openingHours) {
        this.opening_hours = openingHours;
    }

    /*
     * photos getter
     */
    public ArrayList<Photos> getPhotos() {
        return photos;
    }

    /*
     * photos setter
     */
    public void setPhotos(ArrayList<Photos> photos) {
        this.photos = photos;
    }

    /*
     * place_id getter
     */
    public String getPlaceId() {
        return place_id;
    }

    /*
     * place_id setter
     */
    public void setPlaceId(String placeId) {
        this.place_id = placeId;
    }

    /*
     * scope setter
     */
    public String getScope() {
        return scope;
    }

    /*
     * scope getter
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /*
     * price_level getter
     */
    public Integer getPriceLevel() {
        return price_level;
    }

    /*
     * price_level setter
     */
    public void setPriceLevel(Integer priceLevel) {
        this.price_level = priceLevel;
    }

    /*
     * rating getter
     */
    public Double getRating() {
        return rating;
    }

    /*
     * rating setter
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /*
     * types getter
     */
    public ArrayList<String> getTypes() {
        return types;
    }

    /*
     * types setter
     */
    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    /*
     * vicinity getter
     */
    public String getVicinity() {
        return vicinity;
    }

    /*
     * vicinity setter
     */
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    private Results(Parcel in){

        icon = in.readString();
        id = in.readString();
        name = in.readString();
        photos = new ArrayList<Photos>();

        in.readTypedList(photos, Photos.CREATOR);
        place_id = in.readString();
        scope = in.readString();
        price_level = in.readInt();
        rating = in.readDouble();
        vicinity = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(icon);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeList(photos);
        parcel.writeString(place_id);
        parcel.writeString(scope);

        if(price_level!=null)
        parcel.writeInt(price_level);

        if(rating!=null)
        parcel.writeDouble(rating);

        parcel.writeString(vicinity);


    }

    public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>(){

        @Override
        public Results createFromParcel(Parcel parcel) {
            return new Results(parcel);
        }

        @Override
        public Results[] newArray(int i) {
            return new Results[i];
        }
    };
}
