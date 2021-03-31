package com.bb1.tub.api.nbt;

import java.util.Set;

import com.bb1.tub.Tub;
import com.google.gson.JsonObject;

public interface CompoundTag {
	
	public static CompoundTag create() {
		return Tub.getNBTManager().getNewCompoundTag();
	}

	public Set<String> getKeys();

	public Object get(String key) ;

	public String getString(String key);

	public int getInt(String key);

	public boolean getBoolean(String key);

	public CompoundTag getCompound(String key);

	public void setString(String key, String value);

	public void setInt(String key, int value);

	public void setBoolean(String key, boolean value);

	public void setCompound(String key, CompoundTag value);
	
	public JsonObject getJsonObject();

}
