package com.bb1.tub.api.entites.pathfinder;

import java.util.Map;

public interface PathfinderGoalManager {
	
	public Map<Integer, PathfinderGoal> getGoals();
	/**
	 * Adds the provided {@link PathfinderGoal} to the designated priority, if priority is null just adds to the end
	 */
	public void setGoal(Integer priority, PathfinderGoal pathfinderGoal);
	
}
