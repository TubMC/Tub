package com.bb1.tub.api.items;

import com.bb1.tub.Identifier;

public abstract class Enchantment {
	
	protected Enchantment() {
		
	}
	
	public abstract String getName();
	
	public abstract Identifier getIdentifier();
	
}
