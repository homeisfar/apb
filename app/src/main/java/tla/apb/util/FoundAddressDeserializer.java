package tla.apb.util;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

import tla.apb.model.FoundAddress;

/**
 * Created by Nafeal on 4/20/2015.
 */
public class FoundAddressDeserializer implements JsonDeserializer<FoundAddress> {

    public FoundAddress deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

        FoundAddress addressToDeserialize = new FoundAddress();

        //Turn that address String into JsonObject
        JsonObject jsonObject = new JsonParser().parse(json.getAsString()).getAsJsonObject();

        //Individually add the values as needed
        addressToDeserialize.setAddress(jsonObject.get("address").getAsString());
        addressToDeserialize.setCity(jsonObject.get("city").getAsString());
        addressToDeserialize.setState(jsonObject.get("state").getAsString());
        addressToDeserialize.setZip(jsonObject.get("zip").getAsInt());

        return addressToDeserialize;
    }


}
