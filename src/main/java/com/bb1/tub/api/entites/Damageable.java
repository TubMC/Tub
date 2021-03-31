package com.bb1.tub.api.entites;

public interface Damageable {
	
	public double getMaxHealth();
	
	public void setMaxHealth(double amount);
	
	public double getHealth();
	
	public void heal(double amount);
	
}
