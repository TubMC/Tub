package com.bb1.tub.api.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import com.bb1.tub.Identifier;
import com.bb1.tub.Tub;

public final class CommandBuilder {
	
	private Set<Identifier> commandNames = new HashSet<Identifier>();
	private BiConsumer<CommandSender, String[]> consumer = new BiConsumer<CommandSender, String[]>() {
		
		private final String command = commandNames.toArray()[0].toString();
		
		@Override
		public void accept(CommandSender arg0, String[] arg1) {
			arg0.sendMessage("/"+command);
		}
		
	};
	
	public CommandBuilder(Identifier name, Identifier... alternatives) {
		assert(name!=null);
		commandNames.add(name);
		if (alternatives!=null && alternatives.length>0) {
			for (Identifier alternative : alternatives) {
				commandNames.add(alternative);
			}
		}
	}
	
	public CommandBuilder addAlternativeName(Identifier name) {
		commandNames.add(name);
		return this;
	}
	
	public CommandBuilder setCommandHandler(BiConsumer<CommandSender, String[]> consumer) {
		assert(consumer!=null);
		this.consumer = consumer;
		return this;
	}
	
	public Command[] build() {
		Command[] commands = new Command[commandNames.size()];
		int i = 0;
		for (Identifier name : commandNames) {
			commands[i] = Tub.getCommandManager().createCommand(name, consumer);
			i++;
		}
		return commands;
	}
	
	public Command[] register() {
		Command[] commands = build();
		for (Command command : commands) {
			Tub.getCommandManager().registerCommand(command);
		}
		return commands;
	}
	
}
