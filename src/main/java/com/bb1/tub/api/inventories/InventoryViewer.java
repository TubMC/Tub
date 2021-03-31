package com.bb1.tub.api.inventories;

import com.bb1.tub.Text;

public interface InventoryViewer {
	/**
	 * The name of the inventory viewer
	 */
	public Text getName();
	
	public Inventory getOpenInventory();
	/**
	 * The name of the inventory (can be custom per viewer)
	 */
	public Text getOpenInventoryName();
	
}
