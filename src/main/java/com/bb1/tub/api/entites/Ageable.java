package com.bb1.tub.api.entites;

public interface Ageable {
	
	public void setAge(int age);
	
	public int getAge();
	
	// Locking
	
	public void setLockAge(boolean lockAge);
	
	public boolean isAgeLocked();
}
