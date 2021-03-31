package com.bb1.tub.api.custom;

public interface CustomManager {
	/**
	 * The same as {@link #register(CustomEntity, boolean)} but automatically selects auto-registering
	 */
	public default EntityInstantiator register(CustomEntity customEntity) {
		return register(customEntity, true);
	}
	
	public EntityInstantiator register(CustomEntity customEntity, boolean autoRegister);
	
}
