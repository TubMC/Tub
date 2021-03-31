package com.bb1.tub.api.entites.pathfinder;

import com.bb1.tub.Identifier;

public class PathfinderGoalNearestAttackableTarget implements PathfinderGoal {
	
	private final Identifier entityIdentifier;
	
	public PathfinderGoalNearestAttackableTarget(Identifier entityIdentifier) {
		this.entityIdentifier = entityIdentifier;
	}
	
	public Identifier getTargetIdentifier() {
		return this.entityIdentifier;
	}
	
}
