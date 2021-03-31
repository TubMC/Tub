package com.bb1.tub.api.loader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.bb1.tub.Identifier;
import com.bb1.tub.Tub;
import com.bb1.tub.api.events.EventHandler;
import com.bb1.tub.api.storage.JsonStorageContainer;
import com.bb1.tub.api.storage.StorageContainer;

public abstract class Addon {
	
	private final String addonName;
	
	protected Addon(String addonName) {
		this.addonName = addonName.replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase();
	}
	
	public final String getName() {
		return this.addonName;
	}
	
	public abstract void onLoad();
	
	public abstract void onUnload();
	
	public void onReload() {
		onUnload();
		onLoad();
	};
	
	public void registerEventHandler(EventHandler<?> eventHandler) {
		Tub.getEventsManager().registerEvent(this, eventHandler);
	}
	
	public final Identifier getIdentifier(String key) {
		return new Identifier(getName(), key);
	}
	
	public StorageContainer getContainer(String name) {
		return Tub.getStorageManager().getStorageContainer(getIdentifier(name));
	}
	
	public final AddonData getData() {
		return Tub.getAddonManager().getAddonData(this);
	}
	
	public StorageContainer getConfig() {
		try {
			Scanner s = new Scanner(getConfigFile());
			ArrayList<String> r = new ArrayList<>();
			while (s.hasNext()) {
				r.add(s.nextLine());
			}
			s.close();
			return new JsonStorageContainer(getIdentifier("config"), String.join("\n", r)); // TODO make this a custom container :)
		} catch (Exception e) {
			return new JsonStorageContainer(getIdentifier("config"));
		}
	}
	
	public void saveConfig(StorageContainer storageContainer) {
		try {
			File file = getConfigFile();
			BufferedWriter b = new BufferedWriter(new PrintWriter(new FileOutputStream(file, true)));
			b.write(storageContainer.toSaveableString());
			b.flush();
			b.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	public File getConfigFile() {
		return new File(Tub.getAddonManager().getConfigPath()+addonName+".json");
	}
	
}
