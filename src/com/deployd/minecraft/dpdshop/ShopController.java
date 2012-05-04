package com.deployd.minecraft.dpdshop;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopController implements Listener {
	
	private HashMap<Block, Shop> shops = new HashMap<Block, Shop>();
	
	private DpdShop plugin;
	
	public ShopController(DpdShop plugin) {
		this.plugin = plugin;
	}

	public void init() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.STEP && !shops.containsKey(block)) {
			Shop shop = new Shop(block);
			shops.put(block, shop);
			shop.init();
			shop.onBlockRightClicked(e);
		}
	}
	

}
