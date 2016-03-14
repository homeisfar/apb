package tla.apb.util;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;

import tla.apb.model.Description;
import tla.apb.model.Url;

/**
 * Created by Nafeal on 4/20/2015.
 */
public class UrlDeserializer implements JsonDeserializer<Url> {
    public static final String TAG_URL = "URLDESERIALIZER";
    public static final boolean DEBUG = false;

    public Url deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

        Url urlToDeserialize = new Url();
        String urlContainingInformation = json.getAsJsonObject().get("url").getAsString();
        urlToDeserialize.setUrl(urlContainingInformation);
        urlToDeserialize.setImageUrl("http://petharbor.com/get_image.asp?RES=Detail&ID="
                                    + urlContainingInformation.substring(urlContainingInformation.lastIndexOf('.')+1)
                                    + "&LOCATION=ASTN");
//        try {

//            Document document = Jsoup.connect(urlContainingInformation).get();
//            Elements imageElements = document.select("img");
//            Elements descriptionElements = document.select("td.DetailDesc");

//            if (imageElements.size() > 0) {
//                urlToDeserialize.setImageUrl("http://petharbor.com/" + imageElements.get(0).attr("src"));
//            }

//            if (descriptionElements.size() > 0) {
//                //Needed to whitelist br to split string easier
//                Whitelist whitelist = Whitelist.none();
//                whitelist.addTags("br");
//                String unformattedDescription = Jsoup.clean(descriptionElements.get(0).toString(), whitelist);
//
//                //Whole description text had newlines, had to remove it and then split it with <br><br> as key
//                String[] separatedUnformattedDescription = unformattedDescription.replace("\n", "").split("<br><br>");
//                //Create description object to add to url object
//                Description animalDescription = new Description();
//
//                animalDescription.setAnimal_name(separatedUnformattedDescription[0].substring(0, separatedUnformattedDescription[0].indexOf("&nbsp;")));
//                animalDescription.setAnimal_id(separatedUnformattedDescription[0].substring(separatedUnformattedDescription[0].lastIndexOf("&nbsp;") + "&nbsp;".length()));
//
//                StringBuilder resultDescription = new StringBuilder();
//                for(int i = 1; i < separatedUnformattedDescription.length; ++i ){
//                    resultDescription.append(separatedUnformattedDescription[i]);
//                    if(i != separatedUnformattedDescription.length){
//                       resultDescription.append(" ");
//                    }
//                }
//                animalDescription.setAnimal_description(resultDescription.toString());
//
//                //Add description object to url object
//                urlToDeserialize.setDescription(animalDescription);
//            }
//        } catch (IOException e) {
//            Log.e(TAG_URL, "Failed to connect to Url for parsing with error: " + e);
//        }
//
//        if(DEBUG)
//        Log.d("UrlDeserializer", "Url: " + urlContainingInformation + "\n" +
//                                 "Image Url: " + urlToDeserialize.getImageUrl() + "\n" +
//                                 "Description: " + urlToDeserialize.getDescription() + "\n");

        return urlToDeserialize;
    }


}
