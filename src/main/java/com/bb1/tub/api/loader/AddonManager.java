package com.bb1.tub.api.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AddonManager {
	
	protected final Map<Addon, AddonData> addons = new HashMap<Addon, AddonData>();
	
	private static boolean loaded = false;
	private final String pathToConfigs;
	
	protected static void setLoaded(boolean value) {
		loaded = value;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	private final String pathToDirectory;
	
	public static synchronized void loadLibraries(File... jars) {
		for (File jar : jars) {
			loadLibrary(jar);
		}
    }
	
	public static synchronized void loadLibrary(File jar) {
        try {
            URLClassLoader loader = (URLClassLoader) AddonManager.class.getClassLoader(); //ClassLoader.getSystemClassLoader();
            URL url = jar.toURI().toURL();
            if(Arrays.asList(loader.getURLs()).contains(url)) {
            	return;
            }
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{java.net.URL.class});
            method.setAccessible(true);
            method.invoke(loader, new Object[]{url});
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            e.printStackTrace();
            return;
        }
    }
	
	protected static AddonData getAddonData(File f) {
		AddonData addonData;
		try {
			@SuppressWarnings("resource")
			JarFile jar = new JarFile(f);
			assert(jar!=null);
			JarEntry jarEntry = jar.getJarEntry("addon.json");
			assert(jarEntry!=null && !jarEntry.isDirectory());
			addonData = new AddonData(f);
		} catch (Exception e) {
			throw new AddonLoadException(e);
		}
		return addonData;
	}
	
	public AddonManager(String pathToDirectory, String pathToConfigs) {
		this.pathToDirectory = pathToDirectory;
		this.pathToConfigs = pathToConfigs;
	}
	
	public AddonData getAddonData(Addon addon) {
		return addons.get(addon);
	}
	
	public Addon getAddon(String name) {
		final String lowercaseName = name.toLowerCase();
		for (Addon addon : addons.keySet()) {
			if (addon.getName().toLowerCase().equals(lowercaseName)) {
				return addon;
			}
		}
		return null;
	}
	
	public String getConfigPath() {
		return this.pathToConfigs;
	}
	
	public void registerAddonsFromDir() {
		registerAddonsFromDir(pathToDirectory);
	}
	
	public void registerAddonsFromDir(String pathToDirectory) {
		setLoaded(false);
		File addonsFiles[] = new File(pathToDirectory).listFiles();
		for (File addon : addonsFiles) {
			if (addon.isFile()) {
				System.out.println("[Tub] Found the jar "+addon.getName());
				loadLibrary(addon);
				AddonData addonData = new AddonData(addon);
				try {
					this.addons.put(addonData.getAddonClass().newInstance(), addonData);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new AddonLoadException(e);
				}
			}
		}
		setLoaded(true);
	}
	
	public void registerAddon(Addon addon, AddonData addonData) {
		System.out.println("[Tub] Registered the addon "+addonData.getName());
		this.addons.put(addon, addonData);
	}
	
	public void unregisterAddon(Addon addon) {
		System.out.println("[Tub] Unregistered the addon "+addon.getName());
		addon.onUnload();
		this.addons.remove(addon);
	}
	
	public Set<Addon> getAddons() {
		return this.addons.keySet();
	}
	
}
