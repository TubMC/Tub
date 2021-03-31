package com.bb1.tub.api.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bb1.tub.Identifier;
import com.bb1.tub.Text;
import com.bb1.tub.api.interfaces.Material;
import com.bb1.tub.api.nbt.CompoundTag;

public interface ItemStack {
	
	public Text getName();
	
	public List<Text> getLore();
	
	public Set<Enchantment> getEnchantments();
	
	public Map<Enchantment, Integer> getEnchantmentMap();
	
	// NBT
	
	public CompoundTag getNBT();
	
	public void setNBT(CompoundTag compoundTag);
	
	// Durability
	
	/**
	 * Returns the max durability this {@link ItemStack} can have
	 */
	public int getMaxDurability();
	/**
	 * Returns the durability remaining on this {@link ItemStack}
	 */
	public int getRemainingDurability();
	
	public default void repairDurability(int durability) {
		int i = getRemainingDurability()+durability;
		setRemainingDurability((i>getMaxDurability()) ? getMaxDurability() : i);
	}
	
	public void setRemainingDurability(int durability);
	
	// Amount (count)
	
	public void setAmount(int amount);
	
	public int getAmount();
	
	public int getMaxAmount();
	
	public Material getMaterial();
	
	public Identifier getIdentifier();
	
}