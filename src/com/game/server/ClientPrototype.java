package com.game.server;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author firith
 * This classwas created for testing purposes.
 * It is used to serve as an early prototype of the game
 */
public class ClientPrototype extends JFrame implements ActionListener {

	/**
	 * Default Serial Version
	 */
	private static final long serialVersionUID = 1L;
	JLabel text, clicked;
	JButton buttonJoin;
	JButton buttonTurn;
	JButton buttonAttack;
	JButton buttonPlace;
	JPanel panel;
	JTextField textField;
	JTextField result;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	String player = "";
	String session = "";
	int numberPlayers = 2; 
	ArrayList<String> enemies = new ArrayList<String>();

	/**
	 * Constructor - Initializes this screen.
	 */
	ClientPrototype(){ //Begin Constructor
		text = new JLabel("Text to send over socket:");
		textField = new JTextField(20);
		result = new JTextField(20);
		result.setEditable(false);
		buttonJoin = new JButton("Join");
		buttonJoin.addActionListener(this);
		buttonTurn = new JButton("Turn");
		buttonTurn.addActionListener(this);
		buttonAttack = new JButton("Attack");
		buttonAttack.addActionListener(this);
		buttonPlace = new JButton("Place ships");
		buttonPlace.addActionListener(this);

		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 3));
		panel.setBackground(Color.white);
		getContentPane().add(panel);
		panel.add(text);
		panel.add(textField);
		panel.add(result);
		panel.add(buttonJoin);
		panel.add(buttonPlace);
		panel.add(buttonTurn);
		panel.add(buttonAttack);
	
		result.setVisible(false);
		buttonAttack.setEnabled(false);
		buttonPlace.setEnabled(false);
		buttonTurn.setEnabled(false);
		
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event){
		Object source = event.getSource();
		String option = "";
		
		if(source == buttonJoin){
			//Send data over socket
			option = "0";
			out.println(option);
			textField.setText(new String(""));
			//Receive text from server
			try{
				//Receive session id
				String line = in.readLine();
				result.setText(line);
				session = line;
				
				//Receive player id
				line = in.readLine();
				result.setText(line);
				player = line;
				
				//Receive enemies id
				for(int i = 0; i < numberPlayers - 1; i++)
					enemies.add(in.readLine());
				
				//Game started?
				line = in.readLine();
//				result.setText(result.getText() + " " + line + " enemies: " + Arrays.toString(enemies.toArray()));
				System.out.println(result.getText() + " " + line + " enemies: " + Arrays.toString(enemies.toArray()));
				buttonJoin.setEnabled(false);
				buttonPlace.setEnabled(true);
			} catch (IOException e){
				System.out.println("Read failed");
				System.exit(1);
			}
		}
		if(source == buttonTurn){
			//Send data over socket
			checkTurn();

		}
		if(source == buttonAttack){
			//Send data
			option = "2|" + session + "|" + player + "|" + enemies.get(0) + "|" + textField.getText();
			out.println(option);
			textField.setText(new String(""));
			//Receive text from server
			try{
				String line = in.readLine();
				result.setText(line);
				System.out.println("Attack result:" + line);
				
				out.println("4|" + session + "|" + player + "|" + enemies.get(0));
				line = in.readLine();
				if(line.contains("Game Over")){
					result.setText("Game Over");
					System.out.println("Game Over");
					buttonAttack.setEnabled(false);
				}else{
					result.setText(line);
					System.out.println(line);
				}
			} catch (IOException e){
				System.out.println("Read failed");
				System.exit(1);
			}
		}
		if(source == buttonPlace){
			//Send data
			option = "3|" + session + "|" + player + "|" + enemies.get(0) + "|" + textField.getText();
			out.println(option);
			textField.setText(new String(""));
			//Receive text from server
			try{
				String line = in.readLine();
				result.setText(line);
				System.out.println("Ships result:" + line);
				buttonTurn.setEnabled(true);
				buttonAttack.setEnabled(true);
				buttonPlace.setEnabled(false);
				String turn = checkTurn();
				System.out.println(turn);
				if(!turn.contains("Your Turn")){
					out.println("4|" + session + "|" + player + "|" + enemies.get(0));
					line = in.readLine();
					if(line.contains("Game Over")){
						result.setText("Game Over");
						System.out.println("Game Over");
						buttonAttack.setEnabled(false);
					}else{
						result.setText(line);
						System.out.println(line);
					}
				}
			} catch (IOException e){
				System.out.println("Read failed");
				System.exit(1);
			}
		}
	}

	/**
	 * Method- Sends message to server to check turn.
	 * @return Whose turn it is.
	 */
	private String checkTurn() {
		String option = "1|" + session + "|" + player;
		out.println(option);
		textField.setText(new String(""));
		//Receive text from server
		try{
			String line = in.readLine();
			String line2 = "";//in.readLine();
			result.setText(line + line2);
			System.out.println("Turn result:" + line);
			return line;
		} catch (IOException e){
			System.out.println("Read failed");
			System.exit(1);
			return null;
		}	
	}

	/**
	 * Method - Establish communication with the server.
	 */
	public void listenSocket(){
		//Create socket connection
		try{
			socket = new Socket("192.168.1.10", 8888);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: kq6py.eng");
			System.exit(1);
		} catch  (IOException e) {
			System.out.println("No I/O");
//			System.exit(1);
		}
	}

	/**
	 * Method - Main method of this class.
	 * @param args - can be empty
	 */
	public static void main(String[] args){
		ClientPrototype frame = new ClientPrototype();
		frame.setTitle("Client Program");
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};

		frame.addWindowListener(l);
		frame.pack();
		frame.setVisible(true);
		frame.listenSocket();
	}
}
