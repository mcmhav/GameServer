package com.game.server;

import java.util.ArrayList;
import java.util.Map;

public class GameManagement {
	
	private static GameManagement instance = null;
	

	private static ArrayList <SessionManagement> sessions;
	
	/**
	 * Constructor - Initializes the Sessions for the different games.
	 */
	private GameManagement(){
		sessions = new ArrayList<SessionManagement>();
	}
	
	/**
	 * Method - Returns the instance of this Singleton class.
	 * @return
	 */
	public static synchronized GameManagement getInstance(){
		if(instance == null)
			instance = new GameManagement();
		return instance;
	}
	
	/**
	 * Method - Adds a player to an open session or creates a new one.
	 * @return The player id of the added player.
	 */
	public static synchronized String addPlayer(){
		SessionManagement session;
		if(sessions.size() < 1){
			session = new SessionManagement();
			sessions.add(session);
		}
		else{
			if(sessions.get(sessions.size() - 1).getPlayers().size() >= 2){
				session = new SessionManagement();
				sessions.add(session);
			}
			else{
				session = sessions.get(sessions.size() - 1);
			}
		}
		Player player = new Player();
		player.setId("Player" + (session.getPlayers().size() + 1));
		session.addPlayer(player);
		return (sessions.size() + "|" + player.getId());
	}
	
	/**
	 * Method - Gets the players in a given session.
	 * @param session - The session id.
	 * @return A map of playerId, Player
	 */
	public static synchronized Map<String, Player> getPlayers(String session){
		return sessions.get(Integer.parseInt(session) - 1).getPlayers();
	}
	
	/**
	 * Method - Gets the player in the current turn in the given session.
	 * @param session - The session id.
	 * @return The player in the current turn.
	 */
	public static synchronized Player getCurrentPlayer(String session){
		return sessions.get(Integer.parseInt(session) - 1).getCurrentPlayer();
	}
	
	/**
	 * Method - Changes turn in the given session.
	 * @param session - The session id.
	 */
	public static synchronized void nextTurn(String session){
		sessions.get(Integer.parseInt(session) - 1).nextTurn();
	}
	
	/**
	 * Method - Checks if the game is over in the given session.
	 * @param session - The session id
	 * @return Whether the game is over or not.
	 */
	public static synchronized boolean isGameOver(String session){
		return sessions.get(Integer.parseInt(session) - 1).isGameOver();
	}
	
	/**
	 * Method - Sets the game over in the given session.
	 * @param session - The session id.
	 */
	public static synchronized void setGameOver(String session){
		sessions.get(Integer.parseInt(session) - 1).setGameOver(true);
	}
	
	/**
	 * Method - Gets the winner of the game in a given session.
	 * @param session - The session id.
	 * @return The winner of the game.
	 */
	public static synchronized Player getWinner(String session){
		return sessions.get(Integer.parseInt(session) - 1).getWinner();
	}
	
	/**
	 * Method - Sets the winner of the game for the given session.
	 * @param session - The session id.
	 * @param player - The winner of the game.
	 */
	public static synchronized void setWinner(String session, String player){
		if(sessions.get(Integer.parseInt(session) - 1).getWinner() == null)
			sessions.get(Integer.parseInt(session) - 1).setWinner(player);
	}

	/**
	 * Method - Gets the result of the last shot made in the given session.
	 * @param session - The session id.
	 * @return - The result of the last shot made in the game.
	 */
	public static synchronized String getLastShot(String session) {
		return sessions.get(Integer.parseInt(session) - 1).getLastShot();
	}

	/**
	 * Method - Sets the last shot made in the given session.
	 * @param session - The session id.
	 * @param lastShot - The last shot made.
	 */
	public static synchronized void setLastShot(String session, String lastShot) {
		sessions.get(Integer.parseInt(session) - 1).setLastShot(lastShot);
	}
	
}
