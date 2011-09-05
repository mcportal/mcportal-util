package me.jpp.MCPortalUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MCPentityListener extends EntityListener{
	
	public static MCPortalUtil plugin;
	public static Common common;
	
	public void mcplog(String msg){
		MCPortalUtil.log.info(MCPortalUtil.prefix+msg);
	}
	
	
	public MCPentityListener(MCPortalUtil instance, JavaPlugin plugin) {
		// TODO Auto-generated constructor stub
		plugin = instance;
		common = new Common(plugin);
	}
	
	
	
	public void onEntityDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(common.isGod(player)){
				//Player is god in this world!
				if(player.getFireTicks()!=0){
					//on fire!
					player.setFireTicks(0); //no longer on fire
				}
				event.setCancelled(true); //Cancel damage
			}
		}
	}
}
