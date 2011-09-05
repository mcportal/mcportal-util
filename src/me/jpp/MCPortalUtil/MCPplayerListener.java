package me.jpp.MCPortalUtil;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.jpp.MCPortalUtil.Common;

public class MCPplayerListener extends PlayerListener{
	
	public static MCPortalUtil plugin;
	public static Common common;
	
	public MCPplayerListener(MCPortalUtil instance, JavaPlugin plugin) {
		// TODO Auto-generated constructor stub
		plugin = instance;
		common = new Common(plugin);
	}
	
	public void mcplog(String msg){
		MCPortalUtil.log.info(MCPortalUtil.prefix+msg);
	}
	
	public static Long ipToInt(String addr) {
        String[] addrArray = addr.split("\\.");

        long num = 0;
        for (int i=0;i<addrArray.length;i++) {
            int power = 3-i;

            num += ((Integer.parseInt(addrArray[i])%256 * Math.pow(256,power)));
        }
        return num;
    }
	
	public void onPlayerJoin(PlayerJoinEvent event){
		
		//DB Check
		Player player = event.getPlayer();
		String q="SELECT * from mcp_users WHERE `username`=\""+player.getName()+"\" LIMIT 1";
		try {
			ResultSet rs = MCPortalUtil.mysql.sqlQuery(q);
			boolean inDb=false;
			while(rs.next()){
				inDb=true;
				/*if(rs.getInt("tpallow")==1){
				MCPortalUtil.tpallow.put(player, true);
				}else{
				MCPortalUtil.tpallow.put(player, false);
				}*/
				mcplog("Loading database data for player "+player.getName());
			}
			if(!inDb){
				//not in DB!
				mcplog(player.getName()+" is a new user! Creating database entry.");
				q = "INSERT INTO mcp_users (`username`) VALUES(\""+player.getName()+"\");";
				MCPortalUtil.mysql.insertQuery(q);
				//MCPortalUtil.tpallow.put(player, true);
				plugin.getServer().broadcastMessage(ChatColor.GOLD+"Please welcome "+player.getName()+" to MCPortal!");
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
		String ip = player.getAddress().toString().substring(1).split(":")[0];
		Long ipnum = ipToInt(ip);
		if(!common.setUserProperty("lastipnum", ipnum.toString(), player)){
			mcplog("Failed to set LASTIPNUM!");
		}
		if(MCPortalUtil.useGeoIP){
			String query = "SELECT country_name FROM mcp_geoipdata WHERE "+ipnum+" >= ipnumstart AND "+ipnum+" <= ipnumend LIMIT 1;";
			ResultSet country;
			try {
				country = MCPortalUtil.mysql.sqlQuery(query);
				while(country.next()){
					event.setJoinMessage(ChatColor.YELLOW+player.getDisplayName()+" has joined the game. "+ChatColor.GRAY+"("+ChatColor.GREEN+country.getString("country_name")+ChatColor.GRAY+")");
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
	}
	
	public void onPlayerTeleport(PlayerTeleportEvent event){
		Player player = (Player)event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		if(from.getWorld()!=to.getWorld()){
			//Changed world. check if player is god on this world!
			if(common.isGodOfWorld(player, to.getWorld())){
				player.sendMessage(ChatColor.GOLD+"You are a god of world "+to.getWorld().getName()+".");
			}
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event){
		long ts = System.currentTimeMillis()/1000;
		String timestamp = String.valueOf(ts);
		Player player = (Player)event.getPlayer();
		if(!common.setUserProperty("lastseen", timestamp, player)){
			mcplog("Error setting lastseen property!");
		}
		common.clearDataMaps(player);
	}
	
	public void onPlayerChat(PlayerChatEvent event){
		if(MCPortalUtil.stripChatColour==true){
			Player player = (Player)event.getPlayer();
			if(!common.playerCan("chat.color", player)){
				String newmsg = event.getMessage().replaceAll("&[0-9a-fA-F]", "");
				if(newmsg!=event.getMessage()){
					player.sendMessage(ChatColor.RED+"You may not use colour in your chat messages at this time.");
					player.sendMessage(ChatColor.RED+"The colouring has been removed from your message.");
					event.setMessage(newmsg);
				}
			}
		}
	}

}
