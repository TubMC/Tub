package com.bb1.tub.api.events;
/**
 * Interface to allow for actions to be cancelled easily
 */
public interface Cancelable {
	
	public boolean isCanceled();
	
	public void setCanceled(boolean cancel);
	
}
