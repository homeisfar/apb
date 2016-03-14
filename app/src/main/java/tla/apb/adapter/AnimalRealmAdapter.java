package tla.apb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import tla.apb.R;
import tla.apb.model.Animal;

/**
 * Created by Nafeal on 5/6/2015.
 */
public class AnimalRealmAdapter extends RealmBaseAdapter<Animal> implements ListAdapter {


    public AnimalRealmAdapter(Context context,
                              RealmResults<Animal> realmResults,
                              boolean automaticUpdate, int resId) {
        super(context, realmResults, automaticUpdate);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = viewInflater.inflate(R.layout.fragment_animal_list_cell, null);
        }

        Animal animal = realmResults.get(position);
        if (animal != null) {
            TextView gender = (TextView) v.findViewById(R.id.gender_cell_text);
            TextView looksLike = (TextView) v.findViewById(R.id.looks_like_cell_text);
            TextView animal_id = (TextView) v.findViewById(R.id.animal_id_cell_text);
            ImageView thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

            if(animal_id != null) {
                animal_id.setText("Animal ID: " + animal.getAnimalId());
            }

            if (gender != null) {
                gender.setText("Gender: " + animal.getSex());
            }

            if (looksLike != null) {
                looksLike.setText("Looks Like: " + animal.getLooksLike());
            }
            //TODO: set imageview
            if (thumbnail != null) {
                String imageUrl = animal.getImage().getImageUrl();
                if (imageUrl != null && imageUrl.length() > 0) {
                    Picasso.with(context)
                            .load(imageUrl)
                            .resize(350, 350)
                            .centerInside()
                            .into(thumbnail);
                }
                Log.d("Image", "image not loaded");
            }
        }
        return v;
    }

    public RealmResults<Animal> getRealmResults() {
        return realmResults;
    }
}
