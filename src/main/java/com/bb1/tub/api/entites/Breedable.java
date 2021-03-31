package com.bb1.tub.api.entites;

import java.util.UUID;

public interface Breedable {
	
	// Cause
	
	public void setBreedCause(UUID uuid);
	
	public UUID getBreadCause();
	
	// Breading timer
	
	public boolean isBreeding();
	
	public void setBreedTicks(int ticks);
	
	public int getBreedingTicks();
	
}
