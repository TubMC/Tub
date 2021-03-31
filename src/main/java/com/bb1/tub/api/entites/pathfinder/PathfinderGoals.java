package com.bb1.tub.api.entites.pathfinder;

public enum PathfinderGoals {
	;
	
	private final PathfinderGoal goal;
	
	PathfinderGoals(PathfinderGoal goal) {
		this.goal = goal;
	}
	
	public PathfinderGoal get() {
		return this.goal;
	}
}
