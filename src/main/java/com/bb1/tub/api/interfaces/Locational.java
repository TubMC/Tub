package com.bb1.tub.api.interfaces;

import com.bb1.tub.api.world.Location;
import com.bb1.tub.api.world.World;
/**
 * Used by any class that holds a location
 */
public interface Locational {
	
	public Location getLocation();
	
	public default World getWorld() {
		return getLocation().getWorld();
	}
	
}
