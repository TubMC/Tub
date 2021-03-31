package com.bb1.tub.api.custom;

import com.bb1.tub.api.entites.Entity;
import com.bb1.tub.api.world.Location;
/**
 * This is used to make instantiation of custom entities easier
 */
public abstract class EntityInstantiator {
	
	public abstract Entity instantiateEntity(Location location);
	
}
