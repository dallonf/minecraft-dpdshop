package com.deployd.minecraft.dpdshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Shop implements Listener {
	
	private Item preview;
	private Block block;
	
	public Shop(Block block) {
		this.block = block;
	}
	
	public void init() {
		DpdShop.plugin.registerEvents(this);
		
		this.preview = block.getWorld().dropItem(block.getLocation().add(0.5f, 1, 0.5f), new ItemStack(Material.DIAMOND, 1));
		preview.setVelocity(new Vector(0, 0, 0));
	}
	
	@EventHandler
	public void onBlockRightClicked(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().equals(block)) {
			e.getPlayer().sendMessage(ChatColor.BLUE + "1 DIAMOND (64 left)");
			
			ItemStack items = e.getPlayer().getItemInHand();
			if (items.getType() == Material.IRON_INGOT) {
				if (items.getAmount() >= 16) {
					if (items.getAmount() == 16) { e.getPlayer().getInventory().remove(items); }
					else { items.setAmount(items.getAmount() - 16); }
					e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
					e.getPlayer().updateInventory();
					e.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for your business! Paid 16 IRON INGOT");
				} else {
					e.getPlayer().sendMessage(ChatColor.RED + "Costs 16 IRON INGOT");
				}
			} else {
				e.getPlayer().sendMessage("Select a stack of 16 IRON INGOT to purchase");
			}
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPreviewDespawn(ItemDespawnEvent e) {
		if (e.getEntity().equals(preview)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPreviewPickedUp(PlayerPickupItemEvent e) {
		if (e.getItem().equals(preview)) {
			e.setCancelled(true);
		}
	}
	
	
	
}
