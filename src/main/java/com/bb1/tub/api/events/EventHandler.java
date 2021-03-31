package com.bb1.tub.api.events;

import java.lang.reflect.Method;

public interface EventHandler<E extends Event> {
	
	public default boolean canHandle(Event event) {
		for (Method method : getClass().getMethods()) {
			if (method.getName().equals("onEvent")) {
				return (event.getClass().isAssignableFrom(method.getParameterTypes()[0]));
			}
		}
		return false;
	}
	
	public void onEvent(E event);
	
}
