package com.pallavinishanth.android.letsdine.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class Results {

    private String icon;
    private String id;
    private Geometry geometry;
    private String name;
    private OpeningHours opening_hours;
    private List<Photos> photos = new ArrayList<Photos>();
    private String place_id;
    private String scope;
    private Integer price_level;
    private Double rating;
    private List<String> types = new ArrayList<String>();
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
    public List<Photos> getPhotos() {
        return photos;
    }

    /*
     * photos setter
     */
    public void setPhotos(List<Photos> photos) {
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
    public List<String> getTypes() {
        return types;
    }

    /*
     * types setter
     */
    public void setTypes(List<String> types) {
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


}
