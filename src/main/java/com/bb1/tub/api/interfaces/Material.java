package com.bb1.tub.api.interfaces;

import com.bb1.tub.Identifier;
import com.bb1.tub.api.world.Block.BlockState;
/**
 * Interface to allow for block and item materials
 */
public interface Material {
	
	public float getBlastResistance();
	
	public float getSlipperiness();
	
	public BlockState getDefaultState();
	
	public boolean isItem();
	
	public Identifier getIdentifier();
	
}
