package me.jpp.MCPortalUtil.commandhandle;

import java.net.MalformedURLException;

import me.jpp.MCPortalUtil.Common;
import me.jpp.MCPortalUtil.MCPortalUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class report extends commandhandle{
	
	public report(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}
	public boolean cmd_report(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		if(!(sender instanceof Player)) {
			sender.sendMessage("You must be logged in!");
			return true;
			}
		if(args.length<1){
			player.sendMessage(ChatColor.RED+"Wrong syntax!");
			return false;
		}
		else if(args[0].equalsIgnoreCase("confirm")){
			//confirming the report
			try {
				if(!(MCPortalUtil.reports.containsKey(player))){
					//no key = no report
					player.sendMessage(ChatColor.RED+"You must submit a report to do that!");
					return false;
				}
				String[] reportdata = MCPortalUtil.reports.get(player);
				String query = "INSERT INTO mcp_reports (`reporter`, `reported`, `reason`) VALUES('"+player.getDisplayName()+"', '"+reportdata[0]+"', '"+reportdata[1]+"');";
				MCPortalUtil.mysql.insertQuery(query);
				player.sendMessage(ChatColor.YELLOW+"Your abuse report has been submitted.");
				mcplog("Abuse report recieved from "+player.getDisplayName()+".");
				MCPortalUtil.reports.remove(player); //remove the report from the map.
				return true;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(args[0].equalsIgnoreCase("cancel")){
			if(!(MCPortalUtil.reports.containsKey(player))){
				//no key = no report
				player.sendMessage(ChatColor.RED+"You must submit a report to do that!");
				return false;
			}
			MCPortalUtil.reports.remove(player); //remove the report from the map.
			player.sendMessage(ChatColor.YELLOW+"Abuse report cancelled.");
			return true;
		}
		else if(!(args[0].equalsIgnoreCase(""))){
			//reporting a user
			if(args.length<2){
			//no reason specified
			player.sendMessage(ChatColor.RED+"Wrong syntax!");
			return false;
			}
			String message = "";
			//string together the message
			for(int i=1;i<args.length;i++){
				message += args[i]+" ";
			}
			String[] reportarray = {args[0], message};
			MCPortalUtil.reports.put(player, reportarray);
			player.sendMessage(ChatColor.RED+"Please confirm the following is correct:");
			player.sendMessage(ChatColor.GRAY+"Name of offender: "+ChatColor.WHITE+reportarray[0]);
			player.sendMessage(ChatColor.GRAY+"Report Reason: "+ChatColor.WHITE+reportarray[1]);
			player.sendMessage(ChatColor.YELLOW+"Type "+ChatColor.DARK_GREEN+"/report confirm"+ChatColor.YELLOW+" if correct. If incorrect: "+ChatColor.RED+"/report cancel");
			return true;
		}
		return false;
		
	}

}
