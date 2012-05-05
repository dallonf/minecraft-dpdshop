package com.deployd.minecraft.dpdshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.deployd.minecraft.dpdshop.util.InventoryUtils;

public class Shop implements Listener {
	
	private Item preview;
	private Block block;
	private Chest chest;
	
	public Shop(Block block) {
		this.block = block;
	}
	
	public void init() {
		DpdShop.plugin.registerEvents(this);
		
		this.chest = getAdjacentChest(block);
		
		render();
	}
	
	public void cleanup() {
		if (this.preview != null) { this.preview.remove(); }
	}
	
	public void render() {
		render(true);
	}
	
	public void render(boolean alwaysRecreate) {
		int stock = getStock();
		if (this.preview != null 
				&& (alwaysRecreate || stock <= 0) ) { 
			this.preview.remove(); 
			this.preview = null;
		}
		
		if (stock > 0 && this.preview == null) {
			this.preview = block.getWorld().dropItem(block.getLocation().add(0.5f, 1, 0.5f), new ItemStack(Material.DIAMOND, 1));
			preview.setVelocity(new Vector(0, 0, 0));
		}
	}
	
	public int getStock() {
		return InventoryUtils.getAmountOf(chest.getInventory(), Material.DIAMOND);
	}
	
	@EventHandler
	public void onBlockRightClicked(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().equals(block)) {
			DpdShop.log("Clicked");
			transaction(e.getPlayer());
			DpdShop.log(block.getLocation());
			
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void transaction(Player player) {
		int quantity = getStock(); 
		
		String message = ChatColor.BLUE + "1 DIAMOND (" + quantity + " left)" + ChatColor.WHITE + " - ";
		
		if (quantity < 1) {
			message += ChatColor.RED + "Currently out of stock! Please come back later";
		} else {
			ItemStack items = player.getItemInHand();
			if (items.getType() == Material.IRON_INGOT) {
				if (items.getAmount() >= 16) {
					if (items.getAmount() == 16) { player.getInventory().remove(items); }
					else { items.setAmount(items.getAmount() - 16); }
					player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
					
					chest.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 16));
					InventoryUtils.remove(chest.getInventory(), Material.DIAMOND, 1);
					chest.update();
					
					render();
					
					player.updateInventory();
					message = ChatColor.BLUE + "1 DIAMOND (" + getStock() + " left)" + ChatColor.WHITE + " - " +  ChatColor.GREEN + "Paid 16 IRON INGOT. Thank you for your business!";
				} else {
					message += ChatColor.RED + " Costs 16 IRON INGOT";
				}
			} else {
				message += "Select a stack of 16 IRON INGOT to purchase";
			}
		}
		
		player.sendMessage(message);
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
	
	@EventHandler
	public void onChestChanged(InventoryCloseEvent e) {
		if (e.getInventory().getHolder().equals(chest)) {
			render(false);
		}
	}
	
	public static boolean isValidShop(Block block) {
		return getAdjacentChest(block) != null;
	}
	
	public static Chest getAdjacentChest(Block block) {
		BlockFace[] faces = new BlockFace[] {
			BlockFace.NORTH,
			BlockFace.SOUTH,
			BlockFace.EAST,
			BlockFace.WEST,
			BlockFace.UP,
			BlockFace.DOWN
		};
		for (BlockFace blockFace : faces) {
			Block chest = block.getRelative(blockFace);
			if (chest.getType() == Material.CHEST) return (Chest)chest.getState();
		}
		return null;
		
	}
	
	
}
