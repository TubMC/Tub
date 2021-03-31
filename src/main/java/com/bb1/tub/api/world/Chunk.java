package com.bb1.tub.api.world;

import java.util.Set;

import com.bb1.tub.api.entites.Entity;

public abstract class Chunk {
	
	protected final int x;
	protected final int z;
	
	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public abstract World getWorld();
	
	public abstract Block getBlockAtRelative(int x, int y, int z);
	
	public abstract Biome getBiomeAt(int x, int y, int z);
	
	public abstract Set<Entity> getEntities();
	
	public abstract boolean isLoaded();
	
	public abstract boolean isGenerated();
	
}
