package pingConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	Boolean connected = false;
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
	private static Scanner sc;
    
    public void startConnection(String ip, int port) {
        try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        connected = true;
		} catch (IOException e) {
			System.out.println("Connection to server failed. Most Likely Incorrect IP or Port.");
			e.printStackTrace();
		}
        
    }
    
    public void run() {
    	String inMsg,outMsg;
    	while(true) {
    		 outMsg = sc.nextLine();
			 while (!(outMsg.trim().length() > 0)) {
				 System.out.println("This message is blank. Please enter a message to send.");
				 outMsg = sc.nextLine();
			 }
    		 try {
    			 out.println(outMsg);
    			 out.flush();
    			 if((inMsg = in.readLine()) != null) {
    				 System.out.println(inMsg);
				 }
			} catch (IOException e) {
				System.out.println("Message could not be read or sent. (Server may have disconnected) Exiting client...");
				e.printStackTrace();
				break;
			}
    	}
    	
    }
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		Client client = new Client();
		System.out.println("Client\nCreated By Rithvik S.");
		while(client.connected == false) {
		System.out.println("Please enter the ip of the server:");
		String ip = sc.nextLine();
		System.out.println("Please enter the port of the server:");
		int port = 9001; 
		while (!sc.hasNextInt()) {
		      System.out.println("That's not a number!");
		      sc.next(); // this is important!
		}
		port = sc.nextInt();
		sc.nextLine();
		client.startConnection(ip,port);
		}
		System.out.println("To stop the client, press 'Ctrl C'.");
		client.run();
	}
	
}
