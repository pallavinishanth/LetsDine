package com.pallavinishanth.android.letsdine.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class Photos {

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
}
