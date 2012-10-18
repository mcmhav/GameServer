package com.game.server;

import java.util.ArrayList;

/**
 * @author firith
 * This class is the representation of the player in the game.
 */
public class Player {
	private String id;
	private ArrayList<Ship> ships;
	
	/**
	 * Constructor - Initializes this player.
	 */
	public Player(){
		id = "";
		ships = new ArrayList<Ship>();
	}
	
	/**
	 * Method - Gets the player id.
	 * @return The player id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Method - Sets the player id to this player.
	 * @param id - The player id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Method - Gets the ships for this player.
	 * @return - The player's ships.
	 */
	public ArrayList<Ship> getShips() {
		return ships;
	}

	/**
	 * Method - Sets the ships for this player.
	 * @param ships - The ships to be establish for this player.
	 */
	public void setShips(ArrayList<Ship> ships) {
		this.ships = ships;
	}
	
	/**
	 * Method - Adds a ship to this player.
	 * @param ship - The ship to be added.
	 */
	public void addShip(Ship ship) {
		this.ships.add(ship);
	}

}
