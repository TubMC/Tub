package com.bb1.tub.api.storage;

import java.util.Set;

import com.bb1.tub.Identifier;
import com.bb1.tub.Tub;
import com.bb1.tub.api.nbt.CompoundTag;

public class NBTStorageContainer extends StorageContainer {
	
	public static NBTStorageContainer inheritFrom(Identifier identifier, JsonStorageContainer container) {
		NBTStorageContainer newContainer = new NBTStorageContainer(identifier);
		container.getJsonObject().entrySet().forEach((e) -> {
			newContainer.put(e.getKey(), e.getValue().getAsString());
		});
		return newContainer;
	}
	
	private CompoundTag nbtTag;
	
	public NBTStorageContainer(Identifier identifier) {
		this(identifier, null);
	}
	
	public NBTStorageContainer(Identifier identifier, String nbt) {
		super(identifier, nbt);
	}
	
	@Override
	public String get(String key) {
		return nbtTag.getString(key);
	}
	
	@Override
	public void put(String key, String value) {
		this.nbtTag.setString(key, value);
	}
	
	@Override
	public boolean has(String key) {
		Object o = this.nbtTag.get(key);
		return (o!=null && o.equals("") && !o.equals(" "));
	}
	
	@Override
	public String toSaveableString() {
		return Tub.getNBTManager().convertCompoundTagToString(nbtTag);
	}
	
	@Override
	protected void fromSaveableString(String string) {
		this.nbtTag = (nbtTag==null) ? Tub.getNBTManager().getNewCompoundTag() : Tub.getNBTManager().convertStringToCompoundTag(string);
	}

	@Override
	public Set<String> getKeys() {
		return this.nbtTag.getKeys();
	}
	
}