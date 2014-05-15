package bourg.austin.HonorPoints;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

public class HonorPoints extends JavaPlugin
{	
	private Connection connection;
	
	public void onEnable()
	{
		this.saveDefaultConfig();
		
		openConnection();
		checkDatabase();
		
		//Set event listeners
		this.getServer().getPluginManager().registerEvents(new MyListener(this), this);
		
		//Set command executors
		this.getCommand("honor").setExecutor(new MyCommandExecutor(this));
		
		DatabaseOperations.initialize(this);
	}
	
	public void onDisable()
	{
		closeConnection();
	}
	
	public synchronized boolean openConnection()
	{
		String connectionString = "jdbc:mysql://" + this.getConfig().getString("sql.ip") + ":" + this.getConfig().getString("sql.port") + "/" + this.getConfig().getString("sql.database");
		
		try
		{
			connection = DriverManager.getConnection(connectionString, this.getConfig().getString("sql.username"), this.getConfig().getString("sql.password"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private synchronized boolean closeConnection()
	{		
		try
		{
			connection.close();
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	private synchronized void checkDatabase()
	{
		try
		{
			//Configure tables for arenas
			PreparedStatement openSinglesArenaDataStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_currency_data" +
					"( player varchar(17) not null," +
						"currency int(10) DEFAULT 0," +
						"PRIMARY KEY (player) " +
					")");
			openSinglesArenaDataStatement.execute();
			openSinglesArenaDataStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}