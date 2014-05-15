package bourg.austin.HonorPoints;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class DatabaseOperations
{
	private static HonorPoints plugin;
	
	public static void initialize(HonorPoints plugin)
	{
		DatabaseOperations.plugin = plugin;
	}
	
	public static synchronized void addPlayer(OfflinePlayer p)
	{
		try
		{
			if (!isPlayerInTable(p))
			{
				PreparedStatement addPlayerStatement = plugin.getConnection().prepareStatement("INSERT INTO player_currency_data SET player = ?");
				addPlayerStatement.setString(1, p.getName());
				addPlayerStatement.execute();
				addPlayerStatement.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized static boolean isPlayerInTable(OfflinePlayer player)
	{
		try
		{
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM player_currency_data WHERE player = '" + player.getName() +"'");
			ResultSet result = statement.executeQuery();
			boolean isInRow = result.next();
			
			statement.close();
			result.close();
			
			return isInRow;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static synchronized int getCurrency(OfflinePlayer player)
	{
		try
		{
			PreparedStatement getPlayerDataStatement = plugin.getConnection().prepareStatement("SELECT * FROM player_currency_data WHERE player = ?");
			getPlayerDataStatement.setString(1, player.getName());
			
			ResultSet result = getPlayerDataStatement.executeQuery();
			
			if (result.next())
				return result.getInt("currency");
			return -1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static synchronized void setCurrency(OfflinePlayer player, int currency)
	{
		try
		{
			PreparedStatement setPlayerDataStatement = plugin.getConnection().prepareStatement("UPDATE player_currency_data SET currency = ? WHERE player = ?");
			setPlayerDataStatement.setInt(1, currency);
			setPlayerDataStatement.setString(2, player.getName());

			setPlayerDataStatement.executeUpdate();
			setPlayerDataStatement.close();
			
			if (plugin.getServer().getPlayer(player.getName()) != null)
				Bukkit.getServer().getPluginManager().callEvent(new OnlinePlayerCurrencyUpdateEvent(plugin.getServer().getPlayer(player.getName())));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
