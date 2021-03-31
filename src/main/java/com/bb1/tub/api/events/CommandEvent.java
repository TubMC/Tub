package com.bb1.tub.api.events;

import com.bb1.tub.api.commands.CommandSender;

public abstract class CommandEvent extends Event implements Cancelable {
	
	protected final CommandSender sender;
	protected final String command;
	protected boolean cancel = false;
	
	public CommandEvent(CommandSender sender, String command) {
		this.sender = sender;
		this.command = command;
	}
	
	@Override
	public boolean isCanceled() {
		return this.cancel;
	}

	@Override
	public void setCanceled(boolean cancel) {
		this.cancel = cancel;
	}
	
}
