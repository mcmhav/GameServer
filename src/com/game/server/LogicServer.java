package com.game.server;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author firith
 * This class calculates all the actions done in the game.
 */
class ClientWorker implements Runnable {
//	private Socket client;
	private JTextArea textArea;

	private String clientMessage;
	private String player = "";
	private String session = "";
	private String enemy = "";
	private Ship attackedShip = null;

	private BufferedReader in = null;
	private PrintWriter out = null;

	/**
	 * Constructor - Sets up the interface and variables for the server.
	 * @param client - The communication socket.
	 * @param textArea - The area where the server displays the messages.
	 */
	ClientWorker(Socket client, JTextArea textArea) {
//		this.client = client;
		this.textArea = textArea;

		try{
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
		GameManagement.getInstance();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run(){		
		while(true){
			try{
				clientMessage = in.readLine();
				StringTokenizer st = new StringTokenizer(clientMessage, "|");
				int option = Integer.parseInt(st.nextToken());

				switch(option){
					case 0:
						joinGame();
						break;
					case 1:
						checkTurn(st);
						break;
					case 2:
						attack(st);
						break;
					case 3:
						placeShips(st);
						break;
					case 4:
						gameOver(st);
						break;
					default:
						out.println("Error");
				}
//				textArea.append(clientMessage + "\n");
//				System.out.println(GameManagement.getPlayers());

			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method - Sends whose turn it is to the client.
	 * @param st - The rest of the client's message.
	 */
	private void checkTurn(StringTokenizer st) {
		//Turn
		session = st.nextToken();
		if(st.nextToken().equals(GameManagement.getCurrentPlayer(session).getId())){
			out.println("Your Turn");
		}
		else
			out.println("Opponent's Turn");
	}

	/**
	 * Method - Joins the player in a session and sends the client
	 * the player id and his enemies id.
	 */
	private void joinGame() {
		//Get session and player
		StringTokenizer st = new StringTokenizer(GameManagement.addPlayer(), "|");
		session = st.nextToken();
		player = st.nextToken();
		out.println(session);
		out.println(player);
		textArea.append(player + " joins in Session: " + session + "\n");
		
		//Wait until all players have joined
		textArea.append("Waiting for Players\n");
		while(GameManagement.getPlayers(session).size() < 2);

		//Send the enemies id to player
		for(Map.Entry<String, Player> p : GameManagement.getPlayers(session).entrySet()){
			if(!p.getValue().getId().equals(player))
				out.println(p.getValue().getId()); 
		}
		
		//Game set
		out.println("Game Set!");
		textArea.append("Game Set!\n");
	}

	/**
	 * Method - Check if the game is over.
	 * @param st - The rest of the client's message.
	 * @return - Whether this game is over or not.
	 * @throws InterruptedException
	 */
	private boolean gameOver(StringTokenizer st) throws InterruptedException {
		session = st.nextToken();
		player = st.nextToken();
		enemy = st.nextToken();
		boolean end = false;
		try{
			ArrayList<Ship> ships = GameManagement.getPlayers(session).get(enemy).getShips();
			for(Ship ship : ships){
				if(ship.destroyedShip()){
					end = true;
				}else{
					end = false;
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Player Management not finished\nNot important on first turn.");
		}
	
		
		if(end){
			GameManagement.setGameOver(session); 
			GameManagement.setWinner(session, player);
		}
		
		while(!player.equals(GameManagement.getCurrentPlayer(session).getId()) && !GameManagement.isGameOver(session));
		Thread.currentThread();
		Thread.sleep(1500);
		if(GameManagement.isGameOver(session)){
			out.println("Game Over|" + GameManagement.getWinner(session).getId());
			textArea.append("Game Over: " + GameManagement.getWinner(session).getId() + " wins");
		}
		else{
			out.println(GameManagement.getLastShot(session));
		}
		enemy = "";
		return end;
	}

	/**
	 * Method - Receives the attack made by the player and send the outcome.
	 * @param st - The rest of the client's message.
	 */
	private void attack(StringTokenizer st) {
		session = st.nextToken();
		player = st.nextToken();
		enemy = st.nextToken();
		String coordinate = st.nextToken();
		if(player.equals(GameManagement.getCurrentPlayer(session).getId()) && !enemy.equals(GameManagement.getCurrentPlayer(session).getId())){
			ArrayList<Ship> ships = GameManagement.getPlayers(session).get(enemy).getShips();
			for(Ship ship : ships){
				if(ship.checkShot(coordinate)){
					attackedShip = ship;
				}
			}
			if(attackedShip != null){
				if(attackedShip.destroyedShip()){
					out.println("Hit|Destroyed|" + coordinate + "|" + ships.indexOf(attackedShip));
					GameManagement.setLastShot(session, "Hit|Destroyed|" + coordinate + "|" + ships.indexOf(attackedShip));
					textArea.append("Attack by: " + player + " Against: " + enemy + " at: " + coordinate + " Ship Hit & Destroyed\n");
				}
				else{
					out.println("Hit|NotDestroyed|" + coordinate + "|" + ships.indexOf(attackedShip));
					GameManagement.setLastShot(session, "Hit|NotDestroyed|" + coordinate + "|" + ships.indexOf(attackedShip));
					textArea.append("Attack by: " + player + " Against: " + enemy + " at: " + coordinate + " Ship Hit but Not Destroyed\n");
				}
				attackedShip = null;
			}
			else{
				out.println("Fail|" + coordinate);
				GameManagement.setLastShot(session, "Fail|" + coordinate);
				textArea.append("Attack by: " + player + " Against: " + enemy + " at: " + coordinate + " Shoot fails\n");
			}
			GameManagement.nextTurn(session);
		}else
			out.println("Error no attack performed!");
	}

	/**
	 * Method - Sets the position of the players ship in the game.
	 * @param st - The rest of the client's message.
	 * @return Whether this positioning was successful.
	 */
	private boolean placeShips(StringTokenizer st) {
		session = st.nextToken();
		player = st.nextToken();
		enemy = st.nextToken();
		
		if(GameManagement.getPlayers(session).get(player).getShips().size() > 1){
			out.println("Ships already placed, cannot change positions");
			return false;
		}
		
		//Add 4 slots ship
		Ship ship = new Ship();
		ship.setSlots(4);
		for(int i = 0; i < 4; i++)
			ship.setCoordinate(st.nextToken());
		GameManagement.getPlayers(session).get(player).addShip(ship);

//		//Add 3 slots ship 1
		ship = new Ship();
		ship.setSlots(3);
		for(int i = 0; i < 3; i++)
			ship.setCoordinate(st.nextToken());
		GameManagement.getPlayers(session).get(player).addShip(ship);
//		
//		//Add 3 slots ship 2
		ship = new Ship();
		ship.setSlots(3);
		for(int i = 0; i < 3; i++)
			ship.setCoordinate(st.nextToken());
		GameManagement.getPlayers(session).get(player).addShip(ship);
//		
		//Add 2 slots ship 1
		ship = new Ship();
		ship.setSlots(2);
		ship.setCoordinate(st.nextToken());
		ship.setCoordinate(st.nextToken());
		GameManagement.getPlayers(session).get(player).addShip(ship);
		
		//Add 2 slots ship 2
		ship = new Ship();
		ship.setSlots(2);
		ship.setCoordinate(st.nextToken());
		ship.setCoordinate(st.nextToken());
		GameManagement.getPlayers(session).get(player).addShip(ship);
		
		textArea.append("Player " + player + "ships: " + Arrays.toString(GameManagement.getPlayers(session).get(player).getShips().toArray()) + "\n");
		while(GameManagement.getPlayers(session).get(enemy).getShips().size() < 2);
		out.println("Ships placed");
		return false;
	}
}

