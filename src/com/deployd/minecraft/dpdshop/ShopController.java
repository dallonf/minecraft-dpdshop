package com.deployd.minecraft.dpdshop;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopController implements Listener {
	
	private HashMap<Block, Shop> shops = new HashMap<Block, Shop>();
	
	private DpdShop plugin;
	
	public ShopController(DpdShop plugin) {
		this.plugin = plugin;
	}

	public void init() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		loadShops();
	}
	
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Shop shop = createShop(block); 
			if (shop != null) shop.onBlockRightClicked(e);
		}
	}
	
	public void cleanup() {
		for (Shop shop : shops.values()) {
			shop.cleanup();
		}
	}
	
	public void loadShops() {
		DpdShop.server.getScheduler().scheduleAsyncDelayedTask(DpdShop.plugin, new LoadShopsRunnable());
	}
	
	public Shop createShop(Block block) {
		if (block.getType() == Material.STEP && !shops.containsKey(block) && Shop.isValidShop(block)) {
			DpdShop.log("Creating shop");
			Shop shop = new Shop(block);
			shops.put(block, shop);
			shop.init();
			return shop;
		}
		return null;
	}
	
	class LoadShopsRunnable implements Runnable {
		@Override
		public void run() {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://dpdshop.deploydapp.com/shops");
			try {
				HttpResponse response = httpClient.execute(request);
				JSONArray json = new JSONArray(EntityUtils.toString(response.getEntity()));
				DpdShop.server.getScheduler().scheduleSyncDelayedTask(DpdShop.plugin, new LoadedShopsRunnable(json));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	class LoadedShopsRunnable implements Runnable {
		JSONArray json;
		public LoadedShopsRunnable(JSONArray json) {
			this.json = json;
		}

		@Override
		public void run() {
			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject shop = json.getJSONObject(i);
					DpdShop.log(shop);
					Block shopBlock = DpdShop.plugin.getOverworld().getBlockAt(
						shop.getInt("x"),
						shop.getInt("y"),
						shop.getInt("z"));
					createShop(shopBlock);
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
		}
		
	}
	

}
