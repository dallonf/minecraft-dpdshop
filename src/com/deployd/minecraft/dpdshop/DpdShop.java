package com.deployd.minecraft.dpdshop;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DpdShop extends JavaPlugin {
	
	public static Logger logger;
	
	public static DpdShop plugin;
	
	public static Server server;
	
	private ShopController shopController;


	public void onEnable(){
		logger = this.getLogger();
		logger.info(getName() + " enabled");
		
		server = getServer();
		plugin = this;
		
		shopController = new ShopController(this);
		shopController.init();
	}
	
	
	public void onDisable(){ 
		shopController.cleanup();
		getServer().getScheduler().cancelTasks(this);
	}
	
	public static void log(Object... params) {
		for (Object object : params) {
			logger.info(object.toString());
		}
	}
	
	public void registerEvents(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	public World getOverworld() {
		return getServer().getWorlds().get(0);
	}
	 
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("basic")){ // If the player typed /basic then do the following...
			// doSomething
			return true;
		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}
	
}
