package me.jpp.MCPortalUtil.commandhandle;


import java.util.List;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import me.jpp.MCPortalUtil.Common;

public class teleportation extends commandhandle{

	public teleportation(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}

	public boolean cmd_tp(CommandSender sender, Command cmd, String commandLabel, String[] args, int override){
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED+"Wrong syntax!");
			return false;
		}
		Player player = (Player)sender;
		List<Player> players = plugin.getServer().matchPlayer(args[0]);
		if(players.size()<1){
			player.sendMessage(ChatColor.RED+"No player matched your query.");
			return true;
		}
		/*if(players.size()>1){
			player.sendMessage(ChatColor.YELLOW+"More than one player matched your input. Picking one of them.");
		}*/
		for(int i=0;i<players.size();i++){
			//go through the list and disallow self-teleportation
			if(players.get(i).getUniqueId()==player.getUniqueId()){
				players.remove(i);
			}
		}
		if(players.size()==0){
			player.sendMessage(ChatColor.RED+"You cannot teleport to yourself!");
			return true;
		}
		Player target = (Player)players.get(0);
		if(common.getUserPropertyInt("tpallow", target)==0 && override==0){
			//tpallow is not 1.
			player.sendMessage(ChatColor.RED+target.getDisplayName()+" has teleportation disabled.");
			return true;
		}
		player.teleport(players.get(0));
		player.sendMessage(ChatColor.AQUA+"Teleported to "+target.getDisplayName()+".");
		return true;
	}
	
	public boolean cmd_tptoggle(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		if(common.getUserPropertyInt("tpallow", player)==0){
			//not allowing teleport. allow it
			if(common.setUserProperty("tpallow", "1", player)){
				player.sendMessage(ChatColor.AQUA+"Players can now teleport to you.");
				return true;
			}
		}else{
			//allowing teleport. disallow it
			if(common.setUserProperty("tpallow", "0", player)){
				player.sendMessage(ChatColor.AQUA+"Players can no longer teleport to you.");
				return true;
			}
			return false; 
		}
		return false;
	}
	
	public boolean cmd_tpcoord(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		if(args.length < 3){
			sender.sendMessage(ChatColor.RED+"Wrong syntax!");
			return false;
		}else{
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			if(args.length==4){
				//world argument
				World world = plugin.getServer().getWorld(args[3]);
				if(world==null){
					sender.sendMessage(ChatColor.RED+"The world "+args[3]+" does not exist.");
					return true;
				}
				Location location = new Location(world, x, y, z);
				player.teleport(location);
				player.sendMessage(ChatColor.AQUA+"Teleported to "+world.getName()+" ("+x+", "+y+", "+z+")");
				return true;
			}else{
				//current world
				Location location = new Location(player.getWorld(), x, y, z);
				player.teleport(location);
				player.sendMessage(ChatColor.AQUA+"Teleported to "+player.getWorld().getName()+" ("+x+", "+y+", "+z+")");
				return true;
			}
			
		}
	}
}
