package com.bb1.tub.api.world;

import com.bb1.tub.api.interfaces.Material;

public interface Block {
	
	public Location getLocation();
	
	public BlockState getBlockState();
	
	public void setBlockState(BlockState blockState);
	
	public static interface BlockState {
		
		public Material getMaterial();
		
	}
	
}
