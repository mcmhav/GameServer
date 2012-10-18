package com.game.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author firith
 * This class manages a session game.
 */
public class SessionManagement {
	private Map<String, Player> players;
	private ArrayList<String> turns;
	private Player currentPlayer;
	private int turn = 0;
	private boolean gameOver;
	private Player winner;
	private String lastShot;
	
	/**
	 * Constructor - Initializes this session.
	 */
	public SessionManagement(){
		players = new HashMap<String, Player>();
		turns = new ArrayList<String>();
		currentPlayer = new Player();
		winner = null;
		gameOver = false;
		lastShot = "";
	}
	
	/**
	 * Method - Adds a player to this session.
	 * @param player - The player to be added.
	 * @return - The added player id.
	 */
	public String addPlayer(Player player){
		players.put(player.getId(), player);
		turns.add(player.getId());
		currentPlayer.setId(turns.get(0));
		return player.getId();
	}
	
	/**
	 * Method - Gets the players in this session.
	 * @return The players for this session.
	 */
	public Map<String, Player> getPlayers() {
		return players;
	}
	
	/**
	 * Method - The current player for this session.
	 * @return - The current player.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Method - Sets the next turn in the game.
	 */
	public void nextTurn(){
		turn++;
		if(turn >= turns.size())
			turn = 0;
		currentPlayer.setId(turns.get(turn));
	}
	
	/**
	 * Checks whether this game is over or not.
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
	/**
	 * Method - Sets the game over for this session.
	 * @param gameOver
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	/**
	 * Method - Gets the winner of this game's session.
	 * @return The winner of this session.
	 */
	public Player getWinner() {
		return winner;
	}
	
	/**
	 * Method - Sets the winner for this session.
	 * @param winner - The winner of this session.
	 */
	public void setWinner(String winner) {
		this.winner = players.get(winner);
	}
	
	/**
	 * Method - Gets the last shot made in this session.
	 * @return The result of the last shot made in this session.
	 */
	public String getLastShot() {
		return lastShot;
	}
	
	/** 
	 * Method - Sets the last shot made in this session.
	 * @param lastShot - The last shot made in this session.
	 */
	public void setLastShot(String lastShot) {
		this.lastShot = lastShot;
	}
	
}
