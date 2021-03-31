package com.bb1.tub.api.interfaces;
/**
 * Interface to allow for things to store XP values
 */
public interface XPStorable {
	
	public int getXP();
	
	public default void giveXP(int amount) {
		setXP(getXP()+amount);
	}
	
	public default void takeXP(int amount) {
		giveXP(-amount);
	}
	
	public void setXP(int amount);
	
	public int getXPLevel();
	
	public default void giveXPLevel(int amount) {
		setXPLevel(getXPLevel()+amount);
	}
	
	public default void takePLevel(int amount) {
		giveXPLevel(-amount);
	}
	
	public void setXPLevel(int amount);
	
}
