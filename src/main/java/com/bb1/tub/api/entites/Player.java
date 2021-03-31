package com.bb1.tub.api.entites;

import java.util.Date;

import com.bb1.tub.api.interfaces.XPStorable;
import com.bb1.tub.api.inventories.InventoryHolder;
import com.bb1.tub.api.inventories.InventoryViewer;
import com.google.gson.JsonObject;

public interface Player extends Entity, Damageable, XPStorable, InventoryViewer, InventoryHolder {
	/**
	 * Plays the minecraft credits to the user
	 */
	public void showCredits();
	/**
	 * Shows the minecraft 'demo is over' screen to the user
	 */
	public void showDemoScreen();
	/**
	 * Sets the players hotbar selected slot
	 * 
	 * @param slot The slot of the hotbar to select (0-based indexed)
	 */
	public void setSelectedHotbarSlot(int slot);
	/**
	 * Returns is the player is banned
	 */
	public boolean isBanned();
	/**
	 * Bans the player for the reason provided
	 * 
	 * @param reason The reason the player should be banned
	 */
	public default void ban(String reason) {
		ban(reason, null);
	}
	/**
	 * The same as {@link #ban(String)} but with a time limit
	 * 
	 * @param reason The reason the player should be banned
	 * @param time The time the player should be banned (in ms)
	 */
	public default void ban(String reason, long time) {
		ban(reason, new Date(System.currentTimeMillis()+time));
	}
	/**
	 * The same as {@link #ban(String)} but with a time limit
	 * 
	 * @param reason The reason the player should be banned
	 * @param date The date the player will be banned until
	 */
	public void ban(String reason, Date date);
	/**
	 * Kicks the player for the reason provided
	 * 
	 * @param reason The reason the player should be kicked
	 */
	/**
	 * Bans the player for the reason provided
	 * 
	 * @param reason The reason the player should be banned
	 */
	public default void banIP(String reason) {
		banIP(reason, null);
	}
	/**
	 * The same as {@link #ban(String)} but with a time limit
	 * 
	 * @param reason The reason the player should be banned
	 * @param time The time the player should be banned (in ms)
	 */
	public default void banIP(String reason, long time) {
		banIP(reason, new Date(System.currentTimeMillis()+time));
	}
	/**
	 * The same as {@link #ban(String)} but with a time limit
	 * 
	 * @param reason The reason the player should be banned
	 * @param date The date the player will be banned until
	 */
	public void banIP(String reason, Date date);
	/**
	 * Kicks the player for the reason provided
	 * 
	 * @param reason The reason the player should be kicked
	 */
	public void kick(String reason);
	/**
	 * Unbans the player
	 */
	public void pardon();
	/**
	 * Returns if the player is whitelisted
	 */
	public boolean isWhitelisted();
	/**
	 * Converts all of the information about a player into a nice {@link JsonObject}
	 */
	public default JsonObject toJsonObject() {
		JsonObject playerJson = new JsonObject();
		playerJson.addProperty("name", getName().toLitteralText().toString());
		playerJson.addProperty("uuid", getUUID().toString());
		playerJson.addProperty("isWhitelisted", isWhitelisted());
		playerJson.addProperty("isBanned", isBanned());
		playerJson.addProperty("xp", getXP());
		playerJson.addProperty("xpLevel", getXPLevel());
		playerJson.addProperty("health", getHealth());
		playerJson.addProperty("maxHealth", getMaxHealth());
		playerJson.addProperty("fireTicks", getFireTicks());
		playerJson.add("location", getLocation().toJsonObject());
		return playerJson;
	}
}
