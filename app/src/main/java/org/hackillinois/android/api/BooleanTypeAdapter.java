package org.hackillinois.android.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
	@Override
	public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if ("0".equals(json.toString())) {
			return false;
		} else if ("1".equals(json.toString())) {
			return true;
		}
		return json.getAsBoolean();
	}
}