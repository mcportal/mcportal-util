package me.jpp.MCPortalUtil.commandhandle;

import me.jpp.MCPortalUtil.Common;
import me.jpp.MCPortalUtil.MCPortalUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class kickall extends commandhandle{
	public kickall(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}
	
	public boolean cmd_kickall(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player[] players = plugin.getServer().getOnlinePlayers();
		String kickMsg = "";
		int kickCount = 0;
		if(args.length==0){
			//no kick message. use default.
			kickMsg = MCPortalUtil.defaultKickAllMsg;
		}else{
			//build the kick message
			for(int i=0;i<args.length;i++){
				kickMsg += args[i]+" ";
			}
		}
		
		for(int i=0;i<players.length;i++){
			Player target = (Player)players[i];
			if(!(sender instanceof Player)){
				//console called the command so dont check the player name. Kick.
				//target.kickPlayer(kickMsg);
				sender.sendMessage(target.getName()+" would have been kicked with "+kickMsg);
				kickCount++;
			}else{
				if(target!=(Player)sender){ //dont kick the sender!
					//target.kickPlayer(kickMsg);
					sender.sendMessage(target.getName()+" would have been kicked with "+kickMsg);
					kickCount++;
				}
			}
		}
		sender.sendMessage(ChatColor.GOLD+""+kickCount+" Players were kicked from the server.");
		return true;
	}
}
