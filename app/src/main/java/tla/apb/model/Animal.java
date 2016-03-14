package tla.apb.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nafeal on 4/19/2015.
 */
public class Animal extends RealmObject implements Serializable {
    @PrimaryKey
    @SerializedName("animal_id")
    private String animalId;

    @SerializedName("location")
    private FoundLocation foundLocation;

    @SerializedName("at_aac")
    private boolean atAac;

    @SerializedName("intake_date")
    private Date intakeDate;

    private String type;

    @SerializedName("looks_like")
    private String looksLike;

    private String color;

    private String sex;

    private String age;

    private Url image;

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public FoundLocation getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(FoundLocation foundLocation) {
        this.foundLocation = foundLocation;
    }

    public boolean getAtAac() {
        return atAac;
    }

    public void setAtAac(boolean atAac) {
        this.atAac = atAac;
    }

    public Date getIntakeDate() {
        return intakeDate;
    }

    public void setIntakeDate(Date intakeDate) {
        this.intakeDate = intakeDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLooksLike() {
        return looksLike;
    }

    public void setLooksLike(String looksLike) {
        this.looksLike = looksLike;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Url getImage() {
        return image;
    }

    public void setImage(Url image) {
        this.image = image;
    }
}
