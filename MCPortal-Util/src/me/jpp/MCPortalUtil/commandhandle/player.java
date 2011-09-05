package me.jpp.MCPortalUtil.commandhandle;

import me.jpp.MCPortalUtil.Common;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class player extends commandhandle{

	public player(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}
	
	public boolean cmd_player(CommandSender sender, Command cmd, String commandLabel, String[] args, int override){
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED+"Wrong syntax!");
			return false;
		}
		Player target = plugin.getServer().getPlayer(args[0]);
		if(target==null){
			
		}
		return false;
	}
	
}
