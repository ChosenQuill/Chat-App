package display;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Group { // Alternative Engine (Doesn't Work)

	public JFrame frame;
	private JTextField txtResponse;
	private JLabel lblScript;
	private JScrollPane scrollPane;
	private JButton btnSend;
	private Boolean boolResponse = false;
	public static String text = "";
	public static int count = 0;
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		Direct display = new Direct();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					display.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Thread thread = new Thread(){
		    public void run(){
		    	display.run();
		    }
		};
		thread.start();
		
	}

	/**
	 * Create the application.
	 */
	public Group() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//Game.setIconImage(Toolkit.getDefaultToolkit().getImage(Display.class.getResource("images/LargeIcon.png")));
		frame.setTitle("Chat - Rithvik S.");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 788, 409);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Panel panel = new Panel();
		panel.setBounds(10, 10, 748, 288);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 748, 288);
		panel.add(scrollPane);

		txtResponse = new JTextField();
		txtResponse.setEditable(true);
		txtResponse.setFont(new Font("Century", Font.PLAIN, 24));
		txtResponse.setBounds(190, 304, 568, 59);
		frame.getContentPane().add(txtResponse);
		txtResponse.setColumns(10);

		lblScript = new JLabel("<html>&nbsp;<font color='green'>Welcome to the <b>chatbox</b>!</font></html>");
		lblScript.setFont(new Font("Candara", Font.PLAIN, 22));
		lblScript.setVerticalAlignment(SwingConstants.TOP);
		scrollPane.setViewportView(lblScript);

		btnSend = new JButton("Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnSend.setBounds(10, 304, 177, 59);
		btnSend.setBackground(Color.BLUE.darker());
		btnSend.setForeground(Color.LIGHT_GRAY.brighter());
		btnSend.setFocusPainted(false);
		frame.getContentPane().add(btnSend);
		frame.getRootPane().setDefaultButton(btnSend);

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolResponse = true;
			}
		});
			  
	
	}
	
	// Custom Code Begins
 	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    /**
     * The set of all names of clients in the chat room.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static HashSet<String> names = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
	public void run() {
		if(question("Are you hosting the server? (Y/N)")) {
			host();
		} else {
			client();
		}
		
	}
	
	public void host() {
		int port = 0;
		do {
			out("Please enter a port above 9000.");
			String response = get();
			if (isNumeric(response)) {
				port = Integer.parseInt(response);
			} else {
				out("That's not a number!");
			}
			
		}  while (!(port > 9000));
		try {
			out("Server Ip Address:\n" + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			out("Couldn't find the server ip address, check manually.");
		}
		out("Server port:\n" + port);
    	try {
			serverSocket = new ServerSocket(port);
			out("Waiting for connection from client.");
			clientSocket = serverSocket.accept();
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        communication();
		} catch (IOException e) {
			out("Server Setup Failed. (Port may be in use or blocked)");
		}
		 
    }
	
	public void client() {
		out("Please enter the ip of the server:");
		String ip = get();
		int port = 0;
		do {
			out("Please enter the port of the server: (Above 9000)");
			String response = get();
			if (isNumeric(response)) {
				port = Integer.parseInt(response);
			} else {
				out("That's not a number!");
			}
			
		}  while (!(port > 9000));
		try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        communication();
		} catch (IOException e) {
			out("Connection to server failed. Most Likely Incorrect IP or Port.");
		}
	}
	public void communication() {
		out("You are now connected.");
		String outMsg;
		Thread thread = new Thread(){
		public void run(){
			String inMsg;
		    while(true) {	
		    	whileRun();
			try {
		    		if((inMsg = in.readLine()) != null) {
		    			out(inMsg);
		    		}
		    	} catch (IOException e) {
		    		out("Message could not be read. (Other person probally disconnected) Exiting client...");
		    		break;
		    	}
		    }
		}
		};
		thread.start();
		while(true) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				out("Thread refresher is not working... IDK why.");
				break;
			}
    		if(boolResponse == true) {
				if(!txtResponse.getText().replaceAll("\\s+","").equalsIgnoreCase("")) {
					boolResponse = false;
					outMsg = txtResponse.getText();
					out("<font color= 'red'>" + outMsg + "</font>");
					txtResponse.setText("");
					out.println(outMsg);
	    			out.flush();
				} else {
					out("Your message is blank.");
				}
					boolResponse = false;
				}
    	}
	}
	
    // Custom Code Ends
    
	
	public Boolean question(String question) {
		Boolean send;
		String response;
		while(true) {
			out(question);
			response = get();
			if(response.equalsIgnoreCase("y")) {
				send = true;
				break;
			} else if (response.equalsIgnoreCase("n")) {
				send = false;
				break;
			}
			else {
				out("This is not a valid response. Please type the charecter 'y' for yes and 'n' for no.");
			}
		}
		return send;
			
	}
	
	public static boolean isNumeric(String str) {
		try {
			int i = Integer.parseInt(str);
		} catch (NumberFormatException ntr) {
			return false;
		}
		return true;
	}
    
	final static String space = "<br/>&nbsp;";
	
	static String generatedResponse = "<html>&nbsp;<font color='green'>Welcome to the <b>chatbox</b>!</font>";
	//int n = 1; // Short For Number Script
	
	private void scrollToBottom() {
		JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
		AdjustmentListener downScroller = new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Adjustable adjustable = e.getAdjustable();
				adjustable.setValue(adjustable.getMaximum());
				verticalBar.removeAdjustmentListener(this);
			}
		};
		verticalBar.addAdjustmentListener(downScroller);
	}

	private void run(String Main) {
			generatedResponse = generatedResponse + space + Main;
		//System.out.println(generatedResponse + "</html>");
		//System.out.println(txtResponse.getText());
		lblScript.setText(generatedResponse + "</html>");
		txtResponse.setText("");
		scrollToBottom();
	}

	public void out(String main, String after) {
		main = main + space + after;
		run(main);
	}
	public void out(String main) {
		run(main);
	}
	
	public String get() {
		while (true){
			whileRun();
			String response;
			if(boolResponse == true) {
				if(!txtResponse.getText().replaceAll("\\s+","").equalsIgnoreCase("")) {
					boolResponse = false;
					response = txtResponse.getText();
					out("<font color= 'blue'>" + response + "</font>");
					txtResponse.setText("");
					return response;
				} else {
					out("Your message is blank.");
				}
				boolResponse = false;
			}
		}
		//out("Error in getting String. Returning nothing");
		//return "";
		
	}
	
	public void whileRun() {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				out("Thread refresher is not working... IDK why.");
			}
	}
		
	public Boolean isNotBlank() {
		if(!txtResponse.getText().replaceAll("\\s+","").equalsIgnoreCase("")) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	private void timer(double main) {
		btnSend.setEnabled(false);
		main = main * 1000;
		try {
			Thread.sleep((int) main);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		btnSend.setEnabled(true);
	}
	
	private void stimer(double main) {
		main = main * 1000;
		try {
			Thread.sleep((int) main);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean c(String compareTo) { // short for compare
		if (txtResponse.getText().equalsIgnoreCase(compareTo)) {
			return true;
		} else {
			return false;
		}
	}

	private void audio(String main) {
		try {
			// Open an audio input stream.
			File soundFile = new File("src/sounds/" + main); // you could also get the sound file with an URL //
																// "src/sounds/crash.wav"
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}
*/
	 private static class Handler extends Thread {
	        private String name;
	        private Socket socket;
	        private BufferedReader in;
	        private PrintWriter out;

	        /**
	         * Constructs a handler thread, squirreling away the socket.
	         * All the interesting work is done in the run method.
	         */
	        public Handler(Socket socket) {
	            this.socket = socket;
	        }

	        /**
	         * Services this thread's client by repeatedly requesting a
	         * screen name until a unique one has been submitted, then
	         * acknowledges the name and registers the output stream for
	         * the client in a global set, then repeatedly gets inputs and
	         * broadcasts them.
	         */
	        public void run() {
	            try {

	                // Create character streams for the socket.
	                in = new BufferedReader(new InputStreamReader(
	                    socket.getInputStream()));
	                out = new PrintWriter(socket.getOutputStream(), true);

	                // Request a name from this client.  Keep requesting until
	                // a name is submitted that is not already used.  Note that
	                // checking for the existence of a name and adding the name
	                // must be done while locking the set of names.
	                while (true) {
	                    out.println("SUBMITNAME");
	                    name = in.readLine();
	                    if (name == null) {
	                        return;
	                    }
	                    synchronized (names) {
	                        if (!names.contains(name)) {
	                            names.add(name);
	                            break;
	                        }
	                    }
	                }

	                // Now that a successful name has been chosen, add the
	                // socket's print writer to the set of all writers so
	                // this client can receive broadcast messages.
	                out.println("NAMEACCEPTED");
	                writers.add(out);

	                // Accept messages from this client and broadcast them.
	                // Ignore other clients that cannot be broadcasted to.
	                while (true) {
	                    String input = in.readLine();
	                    if (input == null) {
	                        return;
	                    }
	                    for (PrintWriter writer : writers) {
	                        writer.println("MESSAGE " + name + ": " + input);
	                    }
	                }
	            } catch (IOException e) {
	                System.out.println(e);
	            } finally {
	                // This client is going down!  Remove its name and its print
	                // writer from the sets, and close its socket.
	                if (name != null) {
	                    names.remove(name);
	                }
	                if (out != null) {
	                    writers.remove(out);
	                }
	                try {
	                    socket.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
}
