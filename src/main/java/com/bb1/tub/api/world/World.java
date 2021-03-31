package com.bb1.tub.api.world;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.bb1.tub.Identifier;
import com.bb1.tub.api.entites.Entity;
import com.bb1.tub.api.entites.Player;

public interface World {
	
	public String getWorldName();
	
	public UUID getWorldUUID();
	
	public default Identifier getIdentifier() {
		return new Identifier("minecraft", getWorldName());
	}
	
	// Chunks, Blocks etc
	
	public Chunk getChunk(int x, int z);
	/**
	 * Generated the chunk at the coordinates given
	 * @return The generated chunk
	 */
	public Chunk generateChunk(int x, int z);
	
	public Set<Chunk> getAllLoadedChunks();
	
	public Block getBlockAt(int x, int y, int z);
	
	public default Block getBlockAt(Location location) {
		return getBlockAt((int)location.getX(), (int)location.getY(), (int)location.getZ());
	}
	
	// Entities
	
	public default Set<Entity> getEntities() {
		Set<Entity> set = new HashSet<>();
		for (Chunk chunk : getAllLoadedChunks()) {
			set.addAll(chunk.getEntities());
		}
		return set;
	}
	
	public Set<Player> getPlayers();
	
	// Difficulty
	
	public void setDifficulty(Difficulty difficulty);
	
	public Difficulty getDifficulty();
	
	public static enum Difficulty {
		PEACEFUL,
		EASY,
		NORMAL,
		HARD,
		;
	}
	
	// Time
	
	public void setTime(int time);
	
	public int getTime();
	
}
