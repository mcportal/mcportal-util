package me.jpp.MCPortalUtil.commandhandle;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.jpp.MCPortalUtil.Common;
import me.jpp.MCPortalUtil.MCPortalUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class verifyforum extends commandhandle{
	
	
	public verifyforum(JavaPlugin plugin, Common common) {
		super(plugin, common);
		// TODO Auto-generated constructor stub
	}

	public boolean cmd_verifyforum(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player)sender;
		if(!(sender instanceof Player)) {
			sender.sendMessage("You must be logged in!");
			return true;
			}
		if(args.length!=1){
			player.sendMessage(ChatColor.RED+"Wrong syntax!");
			return false;
		}else{
			player.sendMessage(ChatColor.GRAY+"Verifying...");
			//check to see if the player has already been verified! only allow account to tie to one community acc.
			String query = "Select * FROM mcp_forumkeys WHERE `mc_user`=\""+player.getDisplayName()+"\";";
			ResultSet mcnamecheck;
			int numrows=0;
			try {
				mcnamecheck = MCPortalUtil.mysql.sqlQuery(query);
				numrows=0;
				while(mcnamecheck.next()){
					numrows++;
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(numrows!=0){
				//account already tied!
				player.sendMessage(ChatColor.RED+"You may not tie your Minecraft account to more than one user.");
				return true;
			}
			query = "Select * FROM mcp_forumkeys WHERE `key`=\""+args[0]+"\" LIMIT 1;";
			try {
				ResultSet checkverify = MCPortalUtil.mysql.sqlQuery(query);
				String tied = null;
				String forumAcc = null;
				numrows=0;
				while(checkverify.next()){
					numrows++;
					tied = checkverify.getString("tied");
					forumAcc = checkverify.getString("forum_user");
				}if(numrows==0){
					player.sendMessage(ChatColor.RED+"The code you entered ("+args[0]+") was incorrect.");
					return true;
				}
				if(tied=="1"){
					player.sendMessage(ChatColor.RED+"That code has already been verified.");
					return true;
				}else{
					query = "UPDATE mcp_forumkeys SET `tied`=1, `mc_user`=\""+player.getDisplayName()+"\" WHERE `key`=\""+args[0]+"\";";
					MCPortalUtil.mysql.updateQuery(query);
					player.sendMessage(ChatColor.GREEN+"Verification complete!");
					player.sendMessage(ChatColor.GREEN+"The minecraft account "+ChatColor.YELLOW+player.getDisplayName()+ChatColor.GREEN+" is now tied to forum account "+ChatColor.YELLOW+forumAcc+ChatColor.GREEN+".");
					mcplog( player.getDisplayName()+" has tied to forum account "+forumAcc+".");
					return true;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
