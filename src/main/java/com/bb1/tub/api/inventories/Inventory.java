package com.bb1.tub.api.inventories;

import java.util.List;
import java.util.Set;

import com.bb1.tub.Text;
import com.bb1.tub.api.items.ItemStack;

public interface Inventory {
	
	public Text getName();
	
	public List<ItemStack> getItems();
	
	public Set<InventoryViewer> getViewers();
	
}
