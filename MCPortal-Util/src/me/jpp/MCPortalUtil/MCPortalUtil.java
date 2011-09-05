package me.jpp.MCPortalUtil;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.net.*;
import java.io.*;

import me.jpp.MCPortalUtil.commandhandle.commandhandle;
import me.jpp.MCPortalUtil.Common;
//bukkit
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
//mysql
import com.alta189.sqlLibrary.MySQL.mysqlCore;

public class MCPortalUtil extends JavaPlugin{
	//VERSION
	public static String version = "0.9-DEV";
	//MySQL
	public static mysqlCore mysql;
	//CONFIG
	public Configuration config;
	public static String[] dbconf = {"","","",""};
	public static boolean useGeoIP = false;
    public static boolean useNewsGrab = false;
    public static String newsGrabURL = "";
    public static String[] newsData = {"", "", ""};
    public static boolean stripChatColour = false;
    public static String defaultKickAllMsg = "";
	//LOGGING
    public static String prefix = "[MCPortalUtil] ";
	public static Logger log = Logger.getLogger("Minecraft");
    //OTHER
    public static Map <Player, String[]> reports = new HashMap<Player, String[]>(); //store reports in map before confirm
    //public static Map <Player, Boolean> tpallow = new HashMap<Player, Boolean>(); //tpallow for players (/tptoggle)
    private final MCPplayerListener playerListener = new MCPplayerListener(this, this);
    private final MCPentityListener entityListener = new MCPentityListener(this, this);
    public static Map <Player, List<World>> god = new HashMap<Player, List<World>>();
    
    
    
    public void getdata_config(){
    	config = getConfiguration();
    	//MCPortalUtil.var to stop NullPointers in reloads
		dbconf[0] = config.getString("dbServer", "localhost");
		MCPortalUtil.dbconf[1] = config.getString("dbUser", "minecraft");
		MCPortalUtil.dbconf[2] = config.getString("dbPassword", "hello");
		MCPortalUtil.dbconf[3] = config.getString("dbDatabase", "minecraft");
		MCPortalUtil.useGeoIP = config.getBoolean("useGeoIP", false);
		MCPortalUtil.useNewsGrab = config.getBoolean("useNewsGrab", false);
		MCPortalUtil.newsGrabURL = config.getString("newsGrabURL", "");
		MCPortalUtil.stripChatColour = config.getBoolean("stripChatColour", false);
		MCPortalUtil.defaultKickAllMsg = config.getString("defaultKickAllMsg", "All players are being kicked from the server!");
		config.save();
    }
    
    public void dbCheck(){
		try {
			if(MCPortalUtil.mysql.checkConnection()){
				log.info(prefix+"Connected to Database at "+dbconf[0]);
				if(!MCPortalUtil.mysql.checkTable("mcp_users")){
					log.info(prefix+"A table required does not exist, creating it (mcp_users)...");
					String query = "CREATE TABLE `mcp_users` (`id` INT AUTO_INCREMENT PRIMARY KEY, `username` VARCHAR(150), `tpallow` INT NOT NULL DEFAULT '1', `lastipnum` BIGINT, `lastseen` INT, `god_worlds` VARCHAR(50));";
					MCPortalUtil.mysql.createTable(query);
				}
				if(!MCPortalUtil.mysql.checkTable("mcp_reports")){
					log.info(prefix+"A table required does not exist, creating it (mcp_reports)...");
					String query = "CREATE TABLE `mcp_reports` (`id` INT AUTO_INCREMENT PRIMARY KEY, `reporter` VARCHAR(25), `reported` VARCHAR(25), `reason` VARCHAR(120));";
					MCPortalUtil.mysql.createTable(query);
				}
				if(!MCPortalUtil.mysql.checkTable("mcp_forumkeys")){
					log.info(prefix+"A table required does not exist, creating it (mcp_forumkeys)...");
					String query = "CREATE TABLE `mcp_forumkeys` (`id` INT AUTO_INCREMENT PRIMARY KEY, `forum_user` VARCHAR(25), `key` VARCHAR(8), `tied` INT NOT NULL DEFAULT '0', `mc_user` VARCHAR(150));";
					MCPortalUtil.mysql.createTable(query);
				}
				if(!MCPortalUtil.mysql.checkTable("mcp_geoipdata")){
					log.info(prefix+"A table required does not exist, creating it (mcp_geoipdata)...");
					String query = "CREATE TABLE `mcp_geoipdata` (`ipstart` VARCHAR(25), `ipend` VARCHAR(25), `ipnumstart` INT, `ipnumend` INT, `country_code` VARCHAR(2), `country_name` VARCHAR(50));";
					MCPortalUtil.mysql.createTable(query);
				}
				if(!MCPortalUtil.mysql.checkTable("mcp_config")){
					log.info(prefix+"A table required does not exist, creating it (mcp_config)...");
					String query = "CREATE TABLE `mcp_config` (`id` INT AUTO_INCREMENT PRIMARY KEY, `config_key` VARCHAR(60), `config_value` VARCHAR(25));";
					MCPortalUtil.mysql.createTable(query);
				}
				ResultSet rs = null;
				String query = "Select * FROM mcp_reports;";
				rs = MCPortalUtil.mysql.sqlQuery(query);
				int reports=0;
				while(rs.next()){
					reports++;
				}
				log.info(prefix+"There are "+reports+" reports in the database.");
				rs = null;
				query = "Select * FROM mcp_forumkeys;";
				rs = MCPortalUtil.mysql.sqlQuery(query);
				int keys=0;
				int tied=0;
				while(rs.next()){
					keys++;
					if(rs.getInt("tied")==1){
						tied++;
					}
				}
				log.info(prefix+"There are "+keys+" verification keys in the database. "+tied+" of these keys are tied to accounts.");
				query = "Select * FROM mcp_users;";
				rs = MCPortalUtil.mysql.sqlQuery(query);
				int playnum=0;
				while(rs.next()){
					playnum++;
				}
				log.info(prefix+"There are "+playnum+" users in the database.");
				query = "Select * FROM mcp_config;";
				rs = MCPortalUtil.mysql.sqlQuery(query);
				int cfgkeys=0;
				int cfgvalues=0;
				while(rs.next()){
					if(rs.getString("config_value")!=""){
						cfgvalues++;
					}
					cfgkeys++;
				}
				log.info(prefix+"There are "+cfgkeys+" configuration keys in the database. "+cfgvalues+" of these keys have Values assigned.");
			}else{
				log.severe(prefix+"MySQL DB Connection FAILED");
				
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
			//e.printStackTrace();
			log.severe(prefix+"SQLException!");
			
		}
    }

    
    public void get_newsdata(){
    	if(useNewsGrab){
	    	try {
				URL newsurl = new URL(newsGrabURL);
				BufferedReader httpdata = new BufferedReader(new InputStreamReader(newsurl.openStream()));
				String newsdata = httpdata.readLine();
				//MCPortalUtil.var to stop NullPointers in reloads
				MCPortalUtil.newsData = newsdata.split(":::");
				log.info(prefix+"News data loaded.");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
    
    
    
    //enable and disable
    
	public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
		getdata_config();
		log.info(prefix+"Plugin version "+version+" has been enabled.");
		MCPortalUtil.mysql = new mysqlCore(log, prefix, dbconf[0], dbconf[3], dbconf[1], dbconf[2]);
		MCPortalUtil.mysql.initialize();
		dbCheck();
		get_newsdata();
		
		//plugin events!
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		//god damage
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Event.Priority.Monitor, this);
		//colour strip
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Normal, this);
		
	}
	
	public void onDisable(){
		log.info(prefix+"Plugin version "+version+" has been disabled.");
	}
	
	
	//commands
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Common persist = new Common(this);
		commandhandle mcpcmd = new commandhandle(this, persist);
		if(mcpcmd.cmdhandle(sender, cmd, commandLabel, args)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean cmd_mcpreload(CommandSender sender, Command cmd, String commandLabel, String[] args){
		getdata_config();
		dbCheck();
		get_newsdata();
		sender.sendMessage(ChatColor.GREEN+"MCPortalUtil configuration has been reloaded");
		return true;
	}

}
