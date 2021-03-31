package com.bb1.tub.api.entites;

import java.util.UUID;

import com.bb1.tub.Identifier;
import com.bb1.tub.Text;
import com.bb1.tub.api.commands.CommandSender;
import com.bb1.tub.api.entites.pathfinder.PathfinderGoalManager;
import com.bb1.tub.api.interfaces.Locational;
import com.bb1.tub.api.world.Location;

public interface Entity extends CommandSender, Locational {
	// Required by CommandSender
	public Text getName();
	
	public void setName(Text text);
	
	public void setLocation(Location location);
	// Required by Locational
	public Location getLocation();
	
	public UUID getUUID();
	
	public Identifier getIdentifier();
	
	public int getFireTicks();
	
	public void setFireTicks(int ticks);
	
	public PathfinderGoalManager getPathfinderGoalManager();
	
}
