package com.bb1.tub.api.commands;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.bb1.tub.Identifier;
import com.bb1.tub.Text;

public interface CommandManager {
	
	public Command createCommand(Identifier commandIdentifier, BiConsumer<CommandSender, String[]> consumer);
	
	public Command createCommand(Identifier commandIdentifier, BiConsumer<CommandSender, String[]> consumer, BiFunction<CommandSender, String[], List<Text>> function);
	
	public void registerCommand(Command command);
	
	public void executeAsConsole(Text command);
	
}
