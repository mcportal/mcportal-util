package me.jpp.MCPortalUtil.commandhandle;

import me.jpp.MCPortalUtil.Common;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class hub extends commandhandle{

	public hub(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}
	
	public boolean cmd_hub(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		if(!common.configKeyExists("hub_x")){ //if one of the hub keys are not in the configdb, there is no hub location.
			player.sendMessage(ChatColor.RED+"The hub has not been defined. Please ask an admin to define it.");
			return true;
		}
		Double x = Double.parseDouble(common.getConfigValue("hub_x"));
		Double y = Double.parseDouble(common.getConfigValue("hub_y"));
		Double z = Double.parseDouble(common.getConfigValue("hub_z"));
		World world = plugin.getServer().getWorld(common.getConfigValue("hub_world"));
		float pitch = Float.parseFloat(common.getConfigValue("hub_pitch"));
		float yaw = Float.parseFloat(common.getConfigValue("hub_yaw"));
		if(world==null){
			sender.sendMessage(ChatColor.RED+"The world "+common.getConfigValue("hub_world")+" does not exist. This is bad! Tell an admin!");
			return true;
		}
		Location location = new Location(world, x, y, z, yaw, pitch);
		player.teleport(location);
		player.sendMessage(ChatColor.AQUA+"Teleported to the Portal Hub.");
		return true;
	}
	
	public boolean cmd_sethub(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		Location loc = player.getLocation();
		if(!common.setConfigValue("hub_x", Double.toString(loc.getX())) || 
		!common.setConfigValue("hub_y", Double.toString(loc.getY())) ||
		!common.setConfigValue("hub_z", Double.toString(loc.getZ())) ||
		!common.setConfigValue("hub_world", loc.getWorld().getName()) ||
		!common.setConfigValue("hub_pitch", Float.toString(loc.getPitch())) ||
		!common.setConfigValue("hub_yaw", Float.toString(loc.getYaw()))){
			player.sendMessage(ChatColor.RED+"Error setting config values! Consult the log file.");
			return true;
		}else{
			//set!
			player.sendMessage(ChatColor.GREEN+"The Portal hub location has been set to "+loc.getWorld().getName()+" ("+Double.toString(loc.getX())+", "+Double.toString(loc.getY())+", "+Double.toString(loc.getZ())+")");
			return true;
		}
	}

}
