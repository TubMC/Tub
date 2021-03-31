package com.bb1.tub.api.events;

import com.bb1.tub.Tub;

public abstract class Event {
	/**
	 * A subroutine that just calls {@link EventsManager#runEvent(Event)}
	 */
	public void callEvent() {
		Tub.getEventsManager().runEvent(this);
	}
	
}
