package tla.apb.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Nafeal on 4/21/2015.
 */
public class Description extends RealmObject implements Serializable {

    private String animal_id;
    private String animal_name;
    private String animal_description;

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getAnimal_description() {
        return animal_description;
    }

    public void setAnimal_description(String animal_description) {
        this.animal_description = animal_description;
    }
}
