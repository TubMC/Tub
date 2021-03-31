package com.bb1.tub;

import java.io.File;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.bb1.tub.api.commands.CommandManager;
import com.bb1.tub.api.commands.CommandSender;
import com.bb1.tub.api.custom.CustomManager;
import com.bb1.tub.api.entites.EntityManager;
import com.bb1.tub.api.entites.Player;
import com.bb1.tub.api.events.EventsManager;
import com.bb1.tub.api.interfaces.TaskManager;
import com.bb1.tub.api.loader.Addon;
import com.bb1.tub.api.loader.AddonManager;
import com.bb1.tub.api.nbt.NBTManager;
import com.bb1.tub.api.storage.StorageContainer;
import com.bb1.tub.api.storage.StorageManager;
import com.bb1.tub.api.web.WebManager;
import com.bb1.tub.api.world.World;
import com.bb1.tub.api.world.WorldManager;

public final class Tub {
	/**Holds the instance of Tub*/
	private static Tub instance;
	/**An array of names that an addon cannot be named*/
	public static String[] blacklistedAddonNames = {"tub", "api", "", "null"};
	
	public static AddonManager getAddonManager() {
		return instance.addonManager;
	}
	
	public static EventsManager getEventsManager() {
		return instance.tubManager;
	}
	
	public static CommandManager getCommandManager() {
		return instance.tubManager;
	}
	
	public static StorageManager getStorageManager() {
		return instance.storageManager;
	}
	
	public static WorldManager getWorldManager() {
		return instance.tubManager;
	}
	
	public static TaskManager getTaskManager() {
		return instance.tubManager;
	}
	
	public static WebManager getWebManager() {
		return instance.webManager;
	}
	
	public static EntityManager getEntityManager() {
		return instance.tubManager;
	}
	
	public static CustomManager getCustomManager() {
		return instance.tubManager;
	}
	
	public static NBTManager getNBTManager() {
		return instance.tubManager;
	}
	
	public static void createCommand(Identifier commandIdentifier, BiConsumer<CommandSender, String[]> consumer) {
		instance.tubManager.registerCommand(instance.tubManager.createCommand(commandIdentifier, consumer));
	}
	
	public static StorageContainer getDefaults() {
		Identifier defaultIdentifier = new Identifier("tub", "defaults");
		StorageContainer storageContainer = instance.storageManager.getStorageContainer(defaultIdentifier);
		return (storageContainer==null) ? instance.storageManager.createStorageContainer(defaultIdentifier) : storageContainer;
	}
	
	public static StorageContainer getWebDefaults() {
		Identifier defaultIdentifier = new Identifier("tub", "web");
		StorageContainer storageContainer = instance.storageManager.getStorageContainer(defaultIdentifier);
		return (storageContainer==null) ? instance.storageManager.createStorageContainer(defaultIdentifier) : storageContainer;
	}
	/**
	 * Gets and returns a player with the name specified
	 * 
	 * @param name The name of the player
	 * @return The player with the name specified
	 */
	public static Player getOnlinePlayer(String name) {
		LitteralText t = new LitteralText(name);
		for (Player player : getAllOnlinePlayers()) {
			if (player.getName().equals(t)) {
				return player;
			}
		}
		return null;
	}
	/**
	 * Gets and returns all online players
	 * 
	 * @return All online players
	 */
	public static Set<Player> getAllOnlinePlayers() {
		Set<Player> set = new HashSet<Player>();
		for (World world : getWorldManager().getWorlds()) {
			set.addAll(world.getPlayers());
		}
		return set;
	}
	
	private final AddonManager addonManager;
	private TubManager tubManager;
	private final StorageManager storageManager;
	private final WebManager webManager;
	private final String rootPath;
	private final String tubsPath;
	private final String containerPath;
	private final String configPath;
	private final double version;
	private final Random random = new Random();
	
	public Tub(String rootPath, double version, TubManager tubManager) {
		System.out.println("[Tub] Initializing Tub");
		if (instance!=null) {
			throw new RuntimeException("Tub has already been initialized!");
		}
		instance = this;
		// Files
		this.rootPath = rootPath;
		this.tubsPath = rootPath+"tubs\\";
		this.containerPath = this.tubsPath+"containers\\";
		this.configPath = this.tubsPath+"config\\";
		// Version control
		this.version = version;
		// Managers
		this.tubManager = tubManager;
		// Provided managers
		this.addonManager = new AddonManager(this.tubsPath, this.configPath);
		this.storageManager = new StorageManager(this.containerPath);
		// Load manager related tasks
		createTubFolder();
		this.storageManager.loadAllSavedStorageContainers(true);
		// Setup default configuration
		StorageContainer storageContainer = getDefaults();
		if (!storageContainer.has("serverUuid")) {
			storageContainer.put("serverUuid", UUID.randomUUID().toString());
		}
		if (!storageContainer.has("enableBStats")) { // TODO: make this acc disable all bstats stuff
			storageContainer.put("enableBStats", Boolean.toString(true));
		}
		StorageContainer storageContainer2 = getWebDefaults();
		if (!storageContainer2.has("enableWebServices")) {
			storageContainer2.put("enableWebServices", Boolean.toString(true));
		}
		if (!storageContainer2.has("webServicePort")) {
			storageContainer2.put("webServicePort", Integer.toString(24530));
		}
		if (!storageContainer2.has("authToken")) {
			storageContainer2.put("authToken", generateRandomToken(64)); // Required for web put requests
		}
		if (!storageContainer2.has("canTokenBeChangedWithPutRequest")) {
			storageContainer2.put("canTokenBeChangedWithPutRequest", Boolean.toString(false));
		}
		// Setup timed web manager startup
		if (Boolean.parseBoolean(storageContainer2.getOrDefault("enableWebServices", Boolean.toString(true)))) {
			// Load webManager based on provided port
			this.webManager = new WebManager(Integer.parseUnsignedInt(storageContainer2.getOrDefault("webServicePort", Integer.toString(24530))), true);
		} else {
			this.webManager = new WebManager(Integer.parseUnsignedInt(storageContainer2.getOrDefault("webServicePort", Integer.toString(24530))), false);
		}
		storageContainer.applyChanges();
		storageContainer2.setGraphQLPreference(false);
		storageContainer2.applyChanges();
		// Create the tub command
		createCommand(new Identifier("tub", "tub"), new BiConsumer<CommandSender, String[]>() {

			@Override
			public void accept(CommandSender t, String[] u) {
				if (getAddonManager().getAddons().size()<=0) {
					t.sendMessage("There is currently no addons");
					return;
				}
				t.sendMessage("There is currently §6"+getAddonManager().getAddons().size()+"§r addons!");
				String msg = "";
				for (Addon addon : getAddonManager().getAddons()) {
					msg += (addon.getName()+",");
				}
				t.sendMessage(msg.substring(0, msg.length()-1)); // Cut last char off
			}
			
		});
		// Print that we are finished
		System.out.println("[Tub] Done!");
	}
	/**
	 * Returns a randomly generated token
	 */
	public String generateRandomToken() {
		byte[] bytes = new byte[65535];
		this.random.nextBytes(bytes);
		return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
	}
	/**
	 * The same as {@link #generateRandomToken()} but cuts of at the desired length
	 */
	public String generateRandomToken(int length) {
		length = length-1; // Tick down for 0 based indexing
		final String string = generateRandomToken();
		return (string.length()>=length) ? string : (length>string.length()) ? (string+generateRandomToken(length-string.length())) : string.substring(0, length);
	}
	/**
	 * Called to stop the Tub API
	 */
	public void onStop() {
		this.webManager.stop();
		this.storageManager.saveAllStorageContainers();
	}
	/**
	 * Returns the version of minecraft the current server is on<br>
	 * For example 1.16.5 will return 16.5<br>
	 * We ignore the 1 at the start as its not needed!<br>
	 * For version specific stuff
	 */
	public static double getVersion() {
		return instance.version;
	}
	
	private void createTubFolder() {
		File file = new File(tubsPath);
		System.out.println("[Tub] Creating tub directory at "+tubsPath);
		file.mkdir();
	}
	
	public void registerAddons() {
		System.out.println("[Tub] Getting addons");
		this.addonManager.registerAddonsFromDir(tubsPath);
	}
	
	public void loadAddons() {
		for (Addon addon : this.addonManager.getAddons()) {
			try {
				addon.onLoad();
			} catch (Exception e) {
				System.err.println("[Tub] Disabling the tub \'"+addon.getName()+"\' due to an exception");
				e.printStackTrace();
				this.addonManager.unregisterAddon(addon);
			}
		}
	}

	public String getRootPath() {
		return rootPath;
	}
	
}
