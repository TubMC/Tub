package com.bb1.tub.api.entites;

import com.bb1.tub.Identifier;
import com.bb1.tub.api.custom.EntityInstantiator;
import com.bb1.tub.api.world.Location;

public interface EntityManager {
	
	public default Entity spawnEntityAt(Identifier identifier, Location location) {
		Entity entity = getEntityInstantiator(identifier).instantiateEntity(location);
		entity.setLocation(location); // Just in-case
		return entity;
	}
	
	public EntityInstantiator getEntityInstantiator(Identifier identifier);
	
	public void registerEntity(Identifier entityIdentifier, EntityInstantiator entityInstantiator);
	
}
