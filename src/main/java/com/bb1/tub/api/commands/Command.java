package com.bb1.tub.api.commands;

import java.util.ArrayList;
import java.util.List;

import com.bb1.tub.Identifier;
import com.bb1.tub.Text;

public abstract class Command {
	
	protected final boolean isPlayerOnly;
	protected final Identifier commandIdentifier;
	
	protected Command(Identifier commandIdentifier) {
		this(commandIdentifier, false);
	}
	
	protected Command(Identifier commandIdentifier, boolean isPlayerOnly) {
		this.commandIdentifier = commandIdentifier;
		this.isPlayerOnly = isPlayerOnly;
	}
	
	public Identifier getIdentifier() {
		return this.commandIdentifier;
	}
	
	public void onCalled(CommandSender commandSender, String args) {
		onCalled(commandSender, args.split(" "));
	}
	
	public abstract void onCalled(CommandSender commandSender, String[] args);
	
	public List<Text> onTab(CommandSender commandSender, String[] args) { return new ArrayList<Text>(); }; // Optional
	
}
