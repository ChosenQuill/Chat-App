package pingConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Server {
	
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
	private static Scanner sc;
    
    public void setup(int port) {
    	try {
			System.out.println("Server Ip Address:\n" + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Couldn't find the server ip address, check manually.");
		}
		System.out.println("Server port:\n" + port);
    	try {
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting for connection from client.");
			clientSocket = serverSocket.accept();
			System.out.println("Client Connected.");
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server Setup Failed. (Port may be in use or blocked)");
		}
    }
    
    public void run() {
    	String message;
		System.out.println("To stop the server, use 'Ctrl C'.");
    	while(true) {
    		try {
				if((message = in.readLine()) != null) {
					System.out.println("Log: " + message);
					out.println("Server Response: " + message);
					out.flush();
				}
			} catch (IOException e) {
				System.out.println("Message could not be read or sent. (Client may have Disconnected) Exiting server...");
				e.printStackTrace();
				break;
			}
    	}
    	
    }
	
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		Server server = new Server();
		System.out.println("Server\nCreated By Rithvik S.");
		int port = 9001;
		do {
		    System.out.println("Please enter a port above 9000.");
		    while (!sc.hasNextInt()) {
		        System.out.println("That's not a number!");
		        sc.next(); // this is important!
		    }
		    port = sc.nextInt();
		} while (!(port > 9000));
		server.setup(port);
		server.run();
	}
}
