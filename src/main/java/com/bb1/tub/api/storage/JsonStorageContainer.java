package com.bb1.tub.api.storage;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.bb1.tub.Identifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class JsonStorageContainer extends StorageContainer {
	/**
	 * Static and final because it should never change and can be used by everything
	 */
	private static final Gson BUILDER = new GsonBuilder().setPrettyPrinting().create();
	
	private JsonObject jsonObject;
	
	public JsonStorageContainer(Identifier identifier) {
		this(identifier, null);
	}
	
	public JsonStorageContainer(Identifier identifier, String json) {
		super(identifier, json);
	}
	
	@Override
	public String get(String key) {
		JsonElement jsonElement = this.jsonObject.get(key);
		return (jsonElement==null) ? null : this.jsonObject.get(key).getAsString();
	}
	
	@Override
	public void put(String key, String value) {
		this.jsonObject.addProperty(key, value);
	}
	
	@Override
	public boolean has(String key) {
		return this.jsonObject.has(key);
	}
	
	@Override
	public String toSaveableString() {
		return BUILDER.toJson(jsonObject);
	}
	
	@Override
	protected void fromSaveableString(String string) {
		try {
			this.jsonObject = new JsonParser().parse(string).getAsJsonObject();
		} catch (Exception e) {
			this.jsonObject = new JsonObject();
		}
	}
	
	public JsonObject getJsonObject() {
		return this.jsonObject;
	}

	@Override
	public Set<String> getKeys() {
		Set<String> set = new HashSet<String>();
		for (Entry<String, JsonElement> entry : this.jsonObject.entrySet()) {
			set.add(entry.getKey());
		}
		return set;
	}

	@Override
	public void remove(String key) {
		this.jsonObject.remove(key);
	}
	
}
