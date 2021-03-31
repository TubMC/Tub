package com.bb1.tub.api.storage;

import java.util.Set;

import com.bb1.tub.Identifier;
import com.bb1.tub.Tub;
/**
 * Simple container for data
 */
public abstract class StorageContainer {
	
	protected Identifier identifier;
	private boolean priv = true; // If this can be accessed by graphQL
	
	protected StorageContainer(Identifier identifier, String string) {
		this.identifier = identifier;
		fromSaveableString(string);
	}
	
	public Identifier getIdentifier() {
		return this.identifier;
	}
	
	public void setGraphQLPreference(boolean preference) {
		this.priv = preference;
	}
	
	public boolean getGraphQLPreference() {
		return this.priv;
	}
	
	public abstract Set<String> getKeys();
	
	public abstract String get(String key);
	
	public String getOrDefault(String key, String defaultValue) {
		return has(key) ? get(key) : defaultValue;
	}
	
	public abstract void put(String key, String value);
	
	public boolean has(String key) {
		return get(key)!=null;
	}
	
	public abstract void remove(String key);
	
	public abstract String toSaveableString();
	
	protected abstract void fromSaveableString(String string);
	
	public void applyChanges() {
		StorageManager storageManager = Tub.getStorageManager();
		storageManager.removeStorageContainer(getIdentifier());
		storageManager.loadStorageContainer(this);
	}
	
}
