package com.bb1.tub.api.custom;
/**
 * This class, once passed to the entity manager, will be wrapped into an entity
 */

import java.util.Map;

import com.bb1.tub.Identifier;
import com.bb1.tub.Text;
import com.bb1.tub.api.entites.pathfinder.PathfinderGoal;

public abstract class CustomEntity {
	/**
	 * The {@link Identifier} of the entity
	 */
	protected final Identifier identifier;
	/**
	 * The {@link Identifier} of the entity's parental entity
	 */
	protected final Identifier parentalIdentifier;
	/**
	 * The maximum HP this entity can have
	 */
	protected final double maxHealth;
	/**
	 * The default name of the entity
	 */
	protected final Text defaultName;
	/**
	 * Holds a list of {@link PathfinderGoal}'s and their index to be inserted in
	 */
	protected Map<Integer, PathfinderGoal> pathfinderMap;
	
	protected CustomEntity(Text defaultName, Identifier identifier, Identifier parentalIdentifier, double maxHealth) {
		this.defaultName = defaultName;
		this.identifier = identifier;
		this.parentalIdentifier = parentalIdentifier;
		this.maxHealth = maxHealth;
	}
	
	protected CustomEntity(Text defaultName, Identifier identifier, Identifier parentalIdentifier, double maxHealth, Map<Integer, PathfinderGoal> pathfinderMap) {
		this.defaultName = defaultName;
		this.identifier = identifier;
		this.parentalIdentifier = parentalIdentifier;
		this.maxHealth = maxHealth;
		this.pathfinderMap = pathfinderMap;
	}
	
	public Identifier getParentalIdentifier() {
		return this.parentalIdentifier;
	}
	
	public Identifier getIdentifier() {
		return this.identifier;
	}
	
	public double getMaxHealth() {
		return this.maxHealth;
	}
	
	public Text getDefaultName() {
		return this.defaultName;
	}
	
	public Map<Integer, PathfinderGoal> getPathfinderGoals() {
		return this.pathfinderMap;
	}
	
}
