package com.bb1.tub.api.loader;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.bb1.tub.Tub;
import com.google.common.io.CharStreams;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class AddonData {
	//static
	protected final File file;
	
	//Changeable
	protected JsonObject jsonObject;
	private String name;
	private String description = "unknown";
	private String version = "1.0";
	private String[] authors;
	private String path;
	
	public AddonData(File file) {
		this.file = file;
		reloadData();
	}
	
	public AddonData(String name, String description, String version, String[] authors, String path) {
		this.file = null;
		this.name = name;
		this.description = description;
		this.version = version;
		this.authors = authors;
		this.path = path;
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", name);
		jsonObject.addProperty("description", description);
		jsonObject.addProperty("version", version);
		jsonObject.addProperty("path", path);
		JsonArray jsonArray = new JsonArray();
		for (String author : authors) {
			jsonArray.add(new JsonPrimitive(author));
		}
		jsonObject.add("authors", jsonArray);
		this.jsonObject = jsonObject;
	}
	
	protected void reloadData() {
		try {
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(file);
			JarEntry jarEntry = jarFile.getJarEntry("addon.json");
			JsonElement jsonElement = new JsonParser().parse(CharStreams.toString(new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)));
			assert(jsonElement.isJsonObject());
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			this.jsonObject = jsonObject;
			JsonElement jsonElement2 = jsonObject.get("name");
			if (jsonElement2==null || jsonElement2.isJsonNull()) {
				this.name = null;
			} else {
				this.name = jsonElement2.getAsString();
			}
			jsonElement2 = jsonObject.get("description");
			if (jsonElement2==null || jsonElement2.isJsonNull()) {
				this.description = "no description";
			} else {
				this.description = jsonElement2.getAsString();
			}
			jsonElement2 = jsonObject.get("version");
			if (jsonElement2==null || jsonElement2.isJsonNull()) {
				this.version = "1.0";
			} else {
				this.version = jsonElement2.getAsString();
			}
			jsonElement2 = jsonObject.get("path");
			if (jsonElement2==null || jsonElement2.isJsonNull()) {
				this.path = null;
			} else {
				this.path = jsonElement2.getAsString();
			}
			jsonElement2 = jsonObject.get("authors");
			if (jsonElement2==null || jsonElement2.isJsonNull()) {
				this.authors = new String[] {"unknown"};
			} else if (jsonElement2.isJsonPrimitive()) {
				this.authors = new String[] {jsonElement2.getAsString()};
			} else {
				List<String> list = new ArrayList<String>();
				for (JsonElement jsonElement4 : jsonElement2.getAsJsonArray()) {
					list.add(jsonElement4.getAsString());
				}
				String[] authors = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					authors[i] = list.get(i);
				}
				this.authors = authors;
			}
			if (this.path==null) {
				this.path = "unknown";
				throw new AddonLoadException("The class path cannot be null!");
			} else if (this.name==null || !isAddonNameAllowed(this.name)) {
				throw new AddonLoadException("The plugin name cannot be "+this.name+"!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddonLoadException(e);
		}
	}
	
	protected static boolean isAddonNameAllowed(String addonName) {
		if (addonName==null) return false;
		final String addonNameLowercase = addonName.toLowerCase().replaceAll(" ", "");
		for (String blacklistedName : Tub.blacklistedAddonNames) {
			if (blacklistedName.toLowerCase().equals(addonNameLowercase)) return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Addon> getAddonClass() {
		try {
			return (Class<? extends Addon>) Class.forName(this.path);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getName() {
		return name;
	}

	public String[] getAuthors() {
		return authors;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}
	
	public String getAsJsonString() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
	}
	
	public JsonObject getAsJsonObject() {
		return jsonObject;
	}
	
}
