package com.pallavinishanth.android.letsdine.Network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class DetailOpeningHours implements Parcelable {

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

    public String[] getweekhours() {

        return weekday_text;
    }

    public void setweekhours(String[] weekHours) {

        this.weekday_text = weekHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private DetailOpeningHours(Parcel in) {

        open_now = in.readByte() != 0;

        String[] weekday_text = new String[7];
        in.readStringArray(weekday_text);

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeByte((byte) (open_now ? 1 : 0));
        parcel.writeStringArray(weekday_text);
    }

    public static final Parcelable.Creator<DetailOpeningHours> CREATOR = new Parcelable.Creator<DetailOpeningHours>() {

        @Override
        public DetailOpeningHours createFromParcel(Parcel parcel) {
            return new DetailOpeningHours(parcel);
        }

        @Override
        public DetailOpeningHours[] newArray(int i) {
            return new DetailOpeningHours[i];
        }
    };
}
