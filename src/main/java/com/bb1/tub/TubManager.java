package com.bb1.tub;

import com.bb1.tub.api.commands.CommandManager;
import com.bb1.tub.api.custom.CustomManager;
import com.bb1.tub.api.entites.EntityManager;
import com.bb1.tub.api.events.EventsManager;
import com.bb1.tub.api.interfaces.TaskManager;
import com.bb1.tub.api.nbt.NBTManager;
import com.bb1.tub.api.world.WorldManager;

public interface TubManager extends TaskManager, EntityManager, CustomManager, CommandManager, WorldManager, EventsManager, NBTManager {
	
}
