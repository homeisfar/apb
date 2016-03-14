package tla.apb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tla.apb.R;
import tla.apb.model.Animal;

/**
 * Created by Nafeal on 5/6/2015.
 */
public class AnimalArrayAdapter extends ArrayAdapter<Animal> {
    private ArrayList<Animal> animals;

    public AnimalArrayAdapter(Context context, int textViewResourceId, ArrayList<Animal> animals) {
        super(context, textViewResourceId, animals);
        this.animals = animals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater viewInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = viewInflater.inflate(R.layout.fragment_animal_list_cell, null);
        }

        Animal animal = animals.get(position);
        if(animal != null) {
            TextView gender = (TextView) v.findViewById(R.id.gender_cell_text);
            TextView looksLike = (TextView) v.findViewById(R.id.looks_like_cell_text);

            if(gender != null) {
                gender.setText("Gender: " + animal.getSex());
            }
            if(looksLike != null) {
                looksLike.setText("Looks Like: " + animal.getLooksLike());
            }

            //TODO: set imageview
        }
        return v;
    }
}
