package com.bb1.tub;

import java.io.Serializable;

public final class Identifier implements Serializable {
	
	private static final long serialVersionUID = -5164618410275442121L;
	
	private final String name;
	private final String key;
	
	public Identifier(String name, String key) {
		this.name = removeIllegalCharacters(name);
		this.key = removeIllegalCharacters(key);
	}
	
	public Identifier(String identifier) {
		String[] s = identifier.split(":");
		this.name = removeIllegalCharacters(s[0]);
		this.key = removeIllegalCharacters(s[1]);
	}

	public String getName() {
		return this.name;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public String toString() {
		return this.name+':'+this.key;
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Identifier) ? ((this.name.equals(((Identifier)obj).getName())) && (this.key.equals(((Identifier)obj).getKey()))) : false);
	}
	
	public boolean isMinecraft() {
		return this.name.equalsIgnoreCase("minecraft");
	}

	public boolean isTub() {
		return this.name.equalsIgnoreCase("tub");
	}

	@Override
	protected Object clone() {
		return new Identifier(name, key);
	}

	private String removeIllegalCharacters(String input) {
		return input.replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "").toLowerCase();
	}
}
