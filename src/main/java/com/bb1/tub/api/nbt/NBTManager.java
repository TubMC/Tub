package com.bb1.tub.api.nbt;

public interface NBTManager {
	
	public String convertCompoundTagToString(CompoundTag compoundTag);
	
	public CompoundTag convertStringToCompoundTag(String tag);
	
	public CompoundTag getNewCompoundTag();
	
}
