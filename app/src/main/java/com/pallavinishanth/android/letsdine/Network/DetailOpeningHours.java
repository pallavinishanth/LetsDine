package com.pallavinishanth.android.letsdine.Network;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class DetailOpeningHours {

    private Boolean open_now;
    private String[] weekday_text = new String[7];

    /*
     * open_now getter
     */
    public Boolean getOpenNow() {
        return open_now;
    }

    /*
     * open_now setter
     */
    public void setOpenNow(Boolean open_now) {
        this.open_now = open_now;
    }

    public String[] getweekhours(){

        return weekday_text;
    }

    public void setweekhours(String[] weekHours){

        this.weekday_text = weekHours;
    }
}
