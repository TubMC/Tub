package com.bb1.tub.api.world;

import java.util.Set;

import com.bb1.tub.Identifier;
import com.bb1.tub.api.world.World.Difficulty;

public interface WorldManager {
	/**
	 * Loops threw all words and gets a world with the matching identifier
	 */
	public default World getWorld(Identifier identifier) {
		for (World world : getWorlds()) {
			if (world.getIdentifier().equals(identifier)) {
				return world;
			}
		}
		return null;
	}
	
	public World createWorld(Identifier identifier, Difficulty difficulty); // TODO: add generator options etc
	
	public void deleteWorld(Identifier identifier);
	/**
	 * Returns a list of all worlds
	 */
	public Set<World> getWorlds();
	
	public World getDefaultWorld();
	
}
