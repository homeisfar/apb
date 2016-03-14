package tla.apb.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Nafeal on 4/19/2015.
 */
public class FoundAddress extends RealmObject implements Serializable{

    private int zip;
    private String address;
    private String city;
    private String state;

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

