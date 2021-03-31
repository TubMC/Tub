package com.bb1.tub.api.events;

import com.bb1.tub.api.entites.Player;
/**
 * An event that a player caused
 */
public abstract class PlayerEvent extends Event {
	
	protected final Player player;
	
	protected PlayerEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() { return this.player; };
	
}
