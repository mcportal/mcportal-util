package me.jpp.MCPortalUtil.commandhandle;



import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.jpp.MCPortalUtil.Common;
import me.jpp.MCPortalUtil.MCPortalUtil;

public class commandhandle{
	
	protected JavaPlugin plugin;
	protected Common common;


	public commandhandle(JavaPlugin plugin, Common common){
		this.plugin = plugin;
		this.common = common;
	}
	
	public void mcplog(String msg){
		MCPortalUtil.log.info(MCPortalUtil.prefix+msg);
	}
	
	

	public boolean cmdhandle(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		
		//ALL COMMANDS BELOW REQUIRE YOU TO NOT BE CONSOLE
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("You must be logged in!");
			return true;
		}
		Player player = (Player)sender;
		
		
		
		if(cmd.getName().equalsIgnoreCase("kickall")){
			if(!common.playerCan("kickall", (Player) sender)){
				sender.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			kickall kickallcmd = new me.jpp.MCPortalUtil.commandhandle.kickall(plugin, common);
			if(kickallcmd.cmd_kickall(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		
	
		if(cmd.getName().equalsIgnoreCase("mcpreload")){
			if(!common.playerCan("mcpreload", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			if(((MCPortalUtil) plugin).cmd_mcpreload(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		if(cmd.getName().equalsIgnoreCase("report")){
			if(!common.playerCan("report.report", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			report reportcmd = new me.jpp.MCPortalUtil.commandhandle.report(plugin, common);
			if(reportcmd.cmd_report(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}

		if(cmd.getName().equalsIgnoreCase("verifyforum")){
			if(!common.playerCan("verifyforum", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			verifyforum verforumcmd = new me.jpp.MCPortalUtil.commandhandle.verifyforum(plugin, common);
			if(verforumcmd.cmd_verifyforum(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		
		
		
		if(cmd.getName().equalsIgnoreCase("tp")){
			if(!common.playerCan("teleport.tp", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			teleportation tp = new me.jpp.MCPortalUtil.commandhandle.teleportation(plugin, common);
			if(tp.cmd_tp(sender, cmd, commandLabel, args, 0)){
				return true;
			}else{
				return false;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("tpc")){
			if(!common.playerCan("teleport.tpcoord", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			teleportation tp = new me.jpp.MCPortalUtil.commandhandle.teleportation(plugin, common);
			if(tp.cmd_tpcoord(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("tpo")){
			if(!common.playerCan("teleport.tpo", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			teleportation tp = new me.jpp.MCPortalUtil.commandhandle.teleportation(plugin, common);
			if(tp.cmd_tp(sender, cmd, commandLabel, args, 1)){
				return true;
			}else{
				return false;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("tptoggle")){
			if(!common.playerCan("teleport.tptoggle", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			teleportation tp = new me.jpp.MCPortalUtil.commandhandle.teleportation(plugin, common);
			if(tp.cmd_tptoggle(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("mcpgod")){
			if(!common.playerCan("god.god", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			god godlike = new me.jpp.MCPortalUtil.commandhandle.god(plugin, common);
			if(godlike.cmd_god(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("hub")){
			if(!common.playerCan("hub.hub", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			hub hubcmd = new me.jpp.MCPortalUtil.commandhandle.hub(plugin, common);
			if(hubcmd.cmd_hub(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("sethub")){
			if(!common.playerCan("hub.sethub", player)){
				player.sendMessage(ChatColor.RED+"You don't have permission to access that command");
				return true;
			}
			hub hubcmd = new me.jpp.MCPortalUtil.commandhandle.hub(plugin, common);
			if(hubcmd.cmd_sethub(sender, cmd, commandLabel, args)){
				return true;
			}else{
				return false;
			}
		}
		return false; 
	}
}
