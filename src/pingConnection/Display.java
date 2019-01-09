package pingConnection;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Display { // Alternative Engine (Doesn't Work)

	private JFrame Game;
	private JTextField txtResponse;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Display window = new Display();
					window.Game.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Display() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Game = new JFrame();
		//Game.setIconImage(Toolkit.getDefaultToolkit().getImage(Display.class.getResource("images/LargeIcon.png")));
		Game.setTitle("Chat - Rithvik S.");
		Game.getContentPane().setBackground(Color.WHITE);
		Game.setBounds(100, 100, 788, 409);
		Game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game.getContentPane().setLayout(null);

		Panel panel = new Panel();
		panel.setBounds(10, 10, 748, 288);
		Game.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 748, 288);
		panel.add(scrollPane);

		txtResponse = new JTextField();
		txtResponse.setFont(new Font("Century", Font.PLAIN, 24));
		txtResponse.setBounds(190, 304, 568, 59);
		Game.getContentPane().add(txtResponse);
		txtResponse.setColumns(10);

		txtResponse.setEditable(false);

		JLabel lblScript = new JLabel("<html></font></html>");
		lblScript.setFont(new Font("Candara", Font.PLAIN, 22));
		lblScript.setVerticalAlignment(SwingConstants.TOP);
		scrollPane.setViewportView(lblScript);

		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnSend.setBounds(10, 304, 177, 59);
		btnSend.setBackground(Color.BLUE.darker());
		btnSend.setForeground(Color.LIGHT_GRAY.brighter());
		btnSend.setFocusPainted(false);
		Game.getContentPane().add(btnSend);
		Game.getRootPane().setDefaultButton(btnSend);

		btnSend.addActionListener(new ActionListener() {
			final String space = "<br/>&nbsp;";
			

			String generatedResponse = "";
			int n = 1; // Short For Number Script

			public void actionPerformed(ActionEvent arg0) {
				onButtonPress();
			}

			private void onButtonPress() {
				
			}

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

			private void run(String Main) {
					generatedResponse = generatedResponse + space + "<font color='red'>" + txtResponse.getText()
							+ "</font>" + space + Main;
				
				/*
				 * else { generatedResponse = generatedResponse + space +
				 * "You have to type your response in the response box." +space+
				 * "You can choose your response from the single quotes from the diolouge."; }
				 */
				System.out.println(generatedResponse + "</html>");
				System.out.println(txtResponse.getText());
				lblScript.setText(generatedResponse + "</html>");
				txtResponse.setText("");
				scrollToBottom();
			}

			private void gen(String main, String after) {
				main = main + space + after;
				run(main);
			}

		});

	}
}
