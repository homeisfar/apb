package tla.apb.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Nafeal on 4/19/2015.
 */
public class FoundLocation extends RealmObject implements Serializable {

    @SerializedName("needs_recoding")
    private boolean needsRecoding;
    private double longitude;
    private double latitude;

    @SerializedName("human_address")
    private FoundAddress humanFoundAddress;

    public boolean getNeedsRecoding() {
        return needsRecoding;
    }

    public void setNeedsRecoding(boolean needsRecoding) {
        this.needsRecoding = needsRecoding;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public FoundAddress getHumanFoundAddress() {
        return humanFoundAddress;
    }

    public void setHumanFoundAddress(FoundAddress humanFoundAddress) {
        this.humanFoundAddress = humanFoundAddress;
    }

}
