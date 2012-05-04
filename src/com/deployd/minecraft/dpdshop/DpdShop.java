package com.deployd.minecraft.dpdshop;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DpdShop extends JavaPlugin {
	
	public static Logger logger;
	
	public static DpdShop plugin;
	
	private ShopController shopController;

	public void onEnable(){ 
		logger = this.getLogger();
		logger.info("DpdShop enabled");
		
		shopController = new ShopController(this);
		shopController.init();
		
		plugin = this;
	}
	
	
	public void onDisable(){ 
	}
	
	public static void log(Object... params) {
		for (Object object : params) {
			logger.info(object.toString());
		}
	}
	
	public void registerEvents(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}
	 
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("basic")){ // If the player typed /basic then do the following...
			// doSomething
			return true;
		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}
}
