package com.bb1.tub.api.commands;

import java.util.UUID;

import com.bb1.tub.LitteralText;
import com.bb1.tub.Text;
import com.bb1.tub.Tub;
import com.bb1.tub.api.interfaces.Locational;
import com.bb1.tub.api.world.Location;

/**
 * Interface to represent anything that can send/receive messages
 */
public interface CommandSender {
	
	public default boolean isPlayer() {
		return false;
	}
	
	public Text getName();
	
	public default void sendMessage(String message) {
		sendMessage(new LitteralText(message));
	}
	
	public default void sendMessage(Text message) {
		sendMessage(message, null);
	}
	
	public void sendMessage(Text message, UUID sender);
	
	public static class ConsoleSender implements CommandSender, Locational {

		@Override
		public Text getName() {
			return new LitteralText("Console");
		}

		@Override
		public void sendMessage(Text message, UUID sender) {
			System.out.println(message.toLitteralText().toString());
		}
		/**
		 * Console will default to overworld 0,0,0
		 */
		@Override
		public Location getLocation() {
			return new Location(Tub.getWorldManager().getDefaultWorld(), 0, 0, 0);
		}
		
	}
	
}
