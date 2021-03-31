package com.bb1.tub.api.events;

import com.bb1.tub.api.entites.Player;

public class PlayerDeathEvent extends PlayerEvent implements Cancelable {
	
	protected String message;
	protected boolean cancel = false;
	
	public PlayerDeathEvent(Player player, String message) {
		super(player);
		this.message = message;
	}
	
	@Override
	public boolean isCanceled() {
		return this.cancel;
	}

	@Override
	public void setCanceled(boolean cancel) {
		this.cancel = cancel;
	}
	
}
