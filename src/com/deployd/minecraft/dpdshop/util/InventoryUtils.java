package com.deployd.minecraft.dpdshop.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

	public static void remove(Inventory i, Material m, int quantity) {
		int amount = getAmountOf(i, m);
		i.remove(m.getId());
		if (amount - quantity > 0) addToInventory(i, m, amount - quantity);
	}
	
	public static int getAmountOf(Inventory inv, Material m) {
		ItemStack[] invent = inv.getContents();
		int amount = 0;
		for(ItemStack i : invent){
			if(i != null){
				if(i.getType() == m){
					amount += i.getAmount();
				}
			}
		}
		return amount;
	}
	
	public static void addToInventory(Inventory i, Material m, int quantity) {
		i.addItem(new ItemStack(m, quantity));
	}

}
