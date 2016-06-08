package com.oakcentralnetwork.kits;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Kits extends JavaPlugin {
	
	public void onEnable() {
		try {
			saveConfig();
			setupConfig(getConfig());
			saveConfig();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void onDisable() {
		
	}
	
	/**
	 * Makes it where a reset of your config can be done
	 * automatically by just deleting the RESET.FILE file.
	 * I don't like having a RESET.FILE but I will keep it
	 * for convenience.
	 */
	private void setupConfig(FileConfiguration config) throws IOException {
		if(!new File(getDataFolder(), "RESET.FILE").exists()) {
			new File(getDataFolder(), "RESET.FILE").createNewFile();
			
			config.set("Kits.Example.Items", "50-64,278-1,277-4");
			config.set("Kits.Example.Items", "64-64,1-1,5-4");
			
			config.set("Kits.Names", "Example,Example2");
			
			new File(getDataFolder(), "RESET.FILE").createNewFile();
		}
	}
	
	/**
	 * TODO: Make this better
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kit")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0) {
					String[] kits = getConfig().getString("Kits.Names").split(",");
					for(String s : kits) {
						if(s != null) {
							player.sendMessage(ChatColor.BLUE + "[KitCreator] " + ChatColor.GREEN + "/kit " + s + " gives you the kit " + s);
						}
					}
				} else {
					
					for(String s : getConfig().getConfigurationSection("Kits").getKeys(false)) {
						if(args[0].equalsIgnoreCase(s)) {
							try {
								String items = getConfig().getString("Kits." + s + ".Items");
								String[] indiItems = items.split(",");
								for(String s1 : indiItems) {
									String[] itemAmounts = s1.split("-");
									@SuppressWarnings("deprecation")
									ItemStack item = new ItemStack(Integer.valueOf(itemAmounts[0]), Integer.valueOf(itemAmounts[1]));
									player.getInventory().addItem(item);
								}
								player.updateInventory();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							if(args[0].equalsIgnoreCase("create")) {
								FileConfiguration config = getConfig();
								config.set("Kits." + args[1] + ".Items", args[2]);
								saveConfig();
							}
						}
					}
				}
			}
		}
		return true;
	}
}