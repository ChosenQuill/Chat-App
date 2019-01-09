package multiplePingConnection;

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
	private static Scanner sc;
	static private int userCount;
    
    public void run(int port) {
    	userCount = 0;
    	try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			System.out.println("Port is already in use or blocked.\nPlease choose a different port.\nExiting Application.");
		}
    	while(true) {
    		try {
    			System.out.println("Waiting for next client connection.");
    			clientSocket = serverSocket.accept();
    			userCount++;
    			System.out.println("New Client Connected!\n" + userCount + " user(s) connected.");
    			new ServerThread(clientSocket,userCount).start();
    		} catch (IOException e) {
    			e.printStackTrace();
    			System.out.println("Server Setup Failed\nExiting Application.");
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
		try {
			System.out.println("Server Ip Address:\n" + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Couldn't find the server ip address, check manually.");
		}
		System.out.println("Server port:\n" + port);
		System.out.println("To stop the server, type 'quit'.");
		server.run(port);
	}
}

class ServerThread extends Thread {
	private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String id;

    public ServerThread(Socket clientSocket, int userCount) {
    	this.socket = clientSocket;
    	id = "ServerThread " + userCount + ": ";
    }

    public void run() {
    	try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println(id+"Server Setup Failed.");
			e.printStackTrace();
		}
    	String message;
    	while(true) {
    		try {
    			message = in.readLine();
    			if(message.equalsIgnoreCase("exit")) {
    				System.out.println(id+"Connection closed.");
    				socket.close();
                    return;
    			}
    			else if(message != null) {
					System.out.println(id + "Log: " + message);
					out.println("Server Response: " + message);
					out.flush();
				}
			} catch (IOException e) {
				System.out.println(id + "Message could not be read or sent. (Client may have Disconnected) Exiting server...");
				e.printStackTrace();
				break;
			}
    	}
        
    }
}
/*
if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                    */
