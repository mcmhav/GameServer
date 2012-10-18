package com.game.server;

import java.util.ArrayList;

/**
 * @author firith
 * This class is the representation of a ship for the game.
 */
public class Ship {
	private int slots;
	private ArrayList<String> coordinates;
	private ArrayList<String> shots;
	
	/**
	 * Constructor - Initializes this ship.
	 */
	public Ship(){
		slots = 0;
		coordinates = new ArrayList<String>();
		shots = new ArrayList<String>();
	}

	/**
	 * Method - Gets the number of slots of this ship.
	 * @return The number of slots.
	 */
	public int getSlots() {
		return slots;
	}

	/**
	 * Method - Sets the number of slots for this ship.
	 * @param slots - The number of slots.
	 */
	public void setSlots(int slots) {
		this.slots = slots;
	}
	
	/**
	 * Method - Set a coordinate for this ship.
	 * @param coordinate - A coordinate for this ship.
	 */
	public void setCoordinate(String coordinate){
		coordinates.add(coordinate);
	}
	
	/**
	 * Method - Gets the coordinates for this ship.
	 * @return The coordinates occupied by this ship.
	 */
	public ArrayList<String> getCoordinates(){
		return coordinates;
	}
	
	/**
	 * Method - Checks wheter a shot has hit the ship or not.
	 * @param coordinate - The coordinate where the shot took place.
	 * @return Whether the shot hit the ship or not.
	 */
	public boolean checkShot(String coordinate){
		if(coordinates.contains(coordinate)){
			shots.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * Method - Checks if the ship has been destroyed.
	 * @return Whether this ship has been destroyed or not.
	 */
	public boolean destroyedShip(){
		if(shots.size() == slots)
			return true;
		return false;
	}

}
