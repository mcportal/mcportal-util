package me.jpp.MCPortalUtil.commandhandle;

import java.util.ArrayList;
import java.util.List;

import me.jpp.MCPortalUtil.Common;
import me.jpp.MCPortalUtil.MCPortalUtil;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class god extends commandhandle{
	public god(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}

	public boolean cmd_god(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		World world = player.getWorld();
		if(args.length==0){
			if(MCPortalUtil.god.containsKey(player)){
				List <World> godworlds = MCPortalUtil.god.get(player);
				if(godworlds.contains(world)){
					//already a god on that world
					player.sendMessage(ChatColor.RED+"You are already a god on this world! Use /god off to stop being a god.");
					return true;
				}else{
					//become god on this world
					godworlds.add(world);
					MCPortalUtil.god.put(player, godworlds);
					player.sendMessage(ChatColor.GOLD+"You are now a god of "+world.getName()+" until you disconnect.");
					mcplog(player.getDisplayName()+" has became a god of "+world.getName()+" until session end/reload.");
					return true;
				}
			}else{
				//become god on this world
				List <World> godworlds = new ArrayList <World>();
				godworlds.add(world);
				MCPortalUtil.god.put(player, godworlds);
				player.sendMessage(ChatColor.GOLD+"You are now a god of "+world.getName()+" until you disconnect.");
				mcplog(player.getDisplayName()+" has became a god of "+world.getName()+" until session end/reload.");
				return true;
			}
		}
		
		if(args[0].equalsIgnoreCase("off")){
			//Disable god on this world
			if(MCPortalUtil.god.containsKey(player)){
				List <World> godworlds = MCPortalUtil.god.get(player);
				if(godworlds.contains(world)){
					godworlds.remove(world);
					MCPortalUtil.god.put(player, godworlds);
					player.sendMessage(ChatColor.GOLD+"You are no longer a god of "+world.getName()+".");
					mcplog(player.getDisplayName()+" is no longer a god of "+world.getName()+".");
					return true;
				}else{
					player.sendMessage(ChatColor.RED+"You are not a god of this world!");
					return true;
				}
			}
		}
		
		if(args[0].equalsIgnoreCase("info")){
			if(MCPortalUtil.god.containsKey(player)){
				List <World> godworlds = MCPortalUtil.god.get(player);
				String worldlist = "";
				for(int i=0;i<godworlds.size();i++){
					worldlist += godworlds.get(i).getName()+", ";
				}
				worldlist = worldlist.substring(0, worldlist.length()-2); //get rid of the last ", "
				player.sendMessage(ChatColor.GOLD+"You are a god of worlds: "+worldlist);
			}else{
				player.sendMessage(ChatColor.GOLD+"You are not a god of any worlds.");
				return true;
				}
		}
		
		return false;
	
	}
}
