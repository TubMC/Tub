package com.bb1.tub.api.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bb1.tub.api.loader.Addon;

public interface EventsManager {
	
	static final Map<Addon, Set<EventHandler<?>>> addonsToEventHandlersMap = new HashMap<Addon, Set<EventHandler<?>>>();
	
	public default void registerEvent(Addon addon, EventHandler<?> eventHandler) {
		Set<EventHandler<?>> set = addonsToEventHandlersMap.get(addon);
		addonsToEventHandlersMap.remove(addon);
		if (set==null) set = new HashSet<EventHandler<?>>();
		set.add(eventHandler);
		addonsToEventHandlersMap.put(addon, set);
	}
	
	public void runEvent(Event event);
	
}
