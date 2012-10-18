package com.game.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author firith
 * This is the main class of this package that handles the multiple players.
 */
public class MultiplayerHandler extends JFrame{

	/**
	 * Default Serial version
	 */
	private static final long serialVersionUID = 1L;
	JLabel label = new JLabel("Text received over socket:");
	JPanel panel;
	JTextArea textArea = new JTextArea();
	ServerSocket server = null;
	private JScrollPane sbrText;

	/**
	 * Constructor - Initializes this class.
	 */
	MultiplayerHandler(){ //Begin Constructor
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.white);
		getContentPane().add(panel);
		textArea.setLineWrap(true);
		textArea.setText(getIP());
		sbrText = new JScrollPane(textArea);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add("North", label);
		panel.add("Center", sbrText);
		panel.setPreferredSize(new Dimension(400,400));
	} //End Constructor

	private String getIP(){
		try {
		    InetAddress addr = InetAddress.getLocalHost();

		    // Get IP Address
		    byte[] ipAddr = addr.getAddress();

		    
		    // Get hostname
		    String hostname = addr.getHostName();
		    
		    return addr.getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}
	/**
	 * Method - Establish the communication with the client.
	 */
	public void listenSocket(){
		try{
			server = new ServerSocket(8888); 
		} catch (IOException e) {
			System.out.println("Could not listen on port 8888");
			System.exit(-1);
		}
		while(true){
			ClientWorker w;
			try{
				w = new ClientWorker(server.accept(), textArea);
				Thread t = new Thread(w);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize(){
		//Objects created in run method are finalized when 
		//program terminates and thread exits
		try{
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}

	/**
	 * Method - Main method for this class.
	 * @param args
	 */
	public static void main(String[] args){
		MultiplayerHandler frame = new MultiplayerHandler();
		frame.setTitle("Server Program");
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
