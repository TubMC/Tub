package com.bb1.tub.api.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

import com.bb1.tub.Identifier;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class StorageManager {
	
	private final String pathToContainers;
	private Set<StorageContainer> storageContainers;
	
	public StorageManager(String pathToContainers) {
		this.pathToContainers = pathToContainers;
		loadAllSavedStorageContainers(true);
	}
	
	public StorageContainer getStorageContainer(Identifier identifier) {
		for (StorageContainer storageContainer : storageContainers) {
			if (storageContainer.getIdentifier().equals(identifier)) {
				return storageContainer;
			}
		}
		return null;
	}
	
	public void loadStorageContainer(StorageContainer container) {
		this.storageContainers.add(container);
	}
	
	public void removeStorageContainer(Identifier identifier) {
		this.storageContainers.removeIf(new Predicate<StorageContainer>() {

			@Override
			public boolean test(StorageContainer t) {
				return t.getIdentifier().equals(identifier);
			}
			
		});
	}
	
	public StorageContainer createStorageContainer(Identifier identifier) {
		return new JsonStorageContainer(identifier);
	}
	
	public void saveAllStorageContainers() {
		for (StorageContainer storageContainer : storageContainers) {
			final Identifier identifier = storageContainer.getIdentifier();
			File folder = new File(pathToContainers+"\\"+identifier.getName()+"\\");
			File file = new File(pathToContainers+"\\"+identifier.getName()+"\\"+identifier.getKey()+".tdb");// tdb means tub database
			try {
				folder.mkdirs();
				file.createNewFile();
				BufferedWriter b = new BufferedWriter(new PrintWriter(file));
				b.write(getStorageData(storageContainer));
				b.newLine();
				b.write(storageContainer.toSaveableString());
				b.flush();
				b.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	private String getStorageData(StorageContainer storageContainer) {
		// Create new JsonObject
		JsonObject jsonObject = new JsonObject();
		// Give it the identifier key paired to the value of the StorageContainer's Identifier
		jsonObject.addProperty("identifier", storageContainer.getIdentifier().toString());
		// Give it the class key paired to the value of the StorageContainer's class path
		jsonObject.addProperty("class", storageContainer.getClass().getName());
		// The graph ql preference
		jsonObject.addProperty("priv", Boolean.toString(storageContainer.getGraphQLPreference()));
		// Format it into a string and return
		return new GsonBuilder().create().toJson(jsonObject);
	}
	
	private StorageContainer fromStorageFile(Identifier identifier, File file) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Open a scanner on that file
		Scanner s = new Scanner(file);
		// Create an array to house the file's content
		ArrayList<String> r = new ArrayList<>();
		// Loop threw all lines
		while (s.hasNext()) {
			// Add the line to the array
	    	r.add(s.nextLine());
		}
		// Close the scanner
		s.close();
		// Parse first line for tdb info
		JsonObject jsonObject = new JsonParser().parse(r.get(0)).getAsJsonObject();
		// Remove the tdb info 
		r.remove(0);
		// Get the containers class
		Class<?> clazz = Class.forName(jsonObject.get("class").getAsString());
		// Create identifier
		Identifier id = new Identifier(jsonObject.get("identifier").getAsString());
		// Load the priv settings
		boolean priv = Boolean.parseBoolean(jsonObject.get("priv").getAsString());
		// Compare identifiers
		if (!id.equals(identifier)) {
			id = identifier;
			System.err.println("Error while loading the StorageContainer "+file.getAbsolutePath()+" The file name does not match the signed identifer, loading under file identifer.");
		}
		/*
		 * Get the classes constructor that uses an Identifier and a String,
		 * invoke it with an Identifier created from the tdb info and a combined version of every other line
		 */
		Object object = clazz.getConstructor(Identifier.class, String.class).newInstance(id, String.join("", r));
		// Case to StorageContainer
		StorageContainer sc = (StorageContainer) object;
		// Apply priv
		sc.setGraphQLPreference(priv);
		// Return it
		return sc;
	}
	
	public void loadAllSavedStorageContainers() {
		loadAllSavedStorageContainers(true);
	}
	
	public void loadAllSavedStorageContainers(boolean clearAllOldContainers) {
		if (this.storageContainers==null || clearAllOldContainers) {
			this.storageContainers = new HashSet<StorageContainer>();
		}
		// Get all sub-directories
		File[] dirs = new File(this.pathToContainers).listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		if (dirs==null || dirs.length<1) return; // No containers
		for (File dir : dirs) {
			String name = getFileName(dir);
			// Remove all directories from file list
			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return !f.isDirectory();
				}
			});
			if (files==null || files.length<1) return; // No containers
			for (File file : files) {
				try {
					loadStorageContainer(fromStorageFile(new Identifier(name, getFileName(file)), file));
				} catch (IOException | NoSuchMethodException | ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String getFileName(File file) {
		String[] str = file.getName().split(File.pathSeparator);
	    String s = str[str.length-1];
		return s.substring(0, (s.lastIndexOf(".")>-1) ? s.lastIndexOf(".") : s.length());
	}
	
}
