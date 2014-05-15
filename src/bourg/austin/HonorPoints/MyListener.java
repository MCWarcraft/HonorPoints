package bourg.austin.HonorPoints;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MyListener implements Listener
{
	private HonorPoints plugin;
	
	public MyListener(HonorPoints plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerLoginEvent(PlayerLoginEvent event)
	{
		DatabaseOperations.addPlayer(event.getPlayer());
	}
}
