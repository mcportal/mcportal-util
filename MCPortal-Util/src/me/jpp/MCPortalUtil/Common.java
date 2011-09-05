package me.jpp.MCPortalUtil;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class Common {
	private JavaPlugin plugin;
	public Common(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	//Common Functions
	/*
	 * PEX
	 */
	public boolean playerCan(String node, Player player){
		//Look for PEX
		if(!plugin.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			MCPortalUtil.log.info(MCPortalUtil.prefix+"PermissionsEx was NOT detected. Commands will NOT execute.");
			return false;
		}
		PermissionManager permissions = PermissionsEx.getPermissionManager();
		if(permissions.has(player, "mcputil."+node)){
			return true;
		}else{
			return false;
		}
	}
	/*
	 * New logging
	 */
	//TODO: NEW LOGGING
	/*
	 * Plugin properties (DB)
	 */
	public String getConfigValue(String key){
		String query = "SELECT `config_value` from mcp_config WHERE `config_key`=\""+key+"\" LIMIT 1;";
		try {
			ResultSet config = MCPortalUtil.mysql.sqlQuery(query);
			String returnprop = "";
			while(config.next()){
				returnprop = config.getString("config_value");
				return returnprop;
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
		return "";
		
	}
	public boolean configKeyExists(String key){
		try {
			ResultSet rs = MCPortalUtil.mysql.sqlQuery("SELECT `config_key` from mcp_config WHERE `config_key`=\""+key+"\" LIMIT 1;");
			while(rs.next()){
				//key already exists
				return true;
			}
			//key does not exist
			return false;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean setConfigValue(String key, String value){
		
		String query = "UPDATE mcp_config SET `config_value`=\""+value+"\" WHERE `config_key`=\""+key+"\"";
		String query_set = "INSERT into mcp_config (`config_key`, `config_value`) VALUES (\""+key+"\", \""+value+"\");";
		try {
			ResultSet rs = MCPortalUtil.mysql.sqlQuery("SELECT `config_key` from mcp_config WHERE `config_key`=\""+key+"\" LIMIT 1;");
			while(rs.next()){
				//key already exists. update.
				MCPortalUtil.mysql.updateQuery(query);
				return true;
			}
			//key does not exist. create.
			MCPortalUtil.mysql.insertQuery(query_set);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
	 * USER PROPERTIES
	 */
	public String getUserPropertyStr(String prop, Player player){
		String query = "SELECT `"+prop+"` from mcp_users WHERE `username`=\""+player.getName()+"\";";
		try {
			ResultSet udata = MCPortalUtil.mysql.sqlQuery(query);
			String returnprop = "";
			while(udata.next()){
				returnprop = udata.getString(prop);
				return returnprop;
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
		return "";
		
	}
	public int getUserPropertyInt(String prop, Player player){
		
		String query = "SELECT `"+prop+"` from mcp_users WHERE `username`=\""+player.getName()+"\";";
		try {
			ResultSet udata = MCPortalUtil.mysql.sqlQuery(query);
			int returnprop = 0;
			while(udata.next()){
				returnprop = udata.getInt(prop);
				return returnprop;
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
		return 0;
		
	}
	public boolean setUserProperty(String prop, String value, Player player){
		String query = "UPDATE mcp_users SET `"+prop+"`=\""+value+"\" WHERE `username`=\""+player.getName()+"\"";
		try {
			MCPortalUtil.mysql.updateQuery(query);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
	 * GOD
	 */
	public boolean isGod(Player player){
		if(MCPortalUtil.god.containsKey(player)){
			List <World> godworlds = MCPortalUtil.god.get(player);
			if(godworlds.contains(player.getWorld())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isGodOfWorld(Player player, World world){
		if(MCPortalUtil.god.containsKey(player)){
			List <World> godworlds = MCPortalUtil.god.get(player);
			if(godworlds.contains(world)){
				return true;
			}
		}
		return false;
	}
	public void clearDataMaps(Player player){
		//clear the common maps of the player.
		if(MCPortalUtil.god.containsKey(player)){ MCPortalUtil.god.remove(player); }
	}
}
