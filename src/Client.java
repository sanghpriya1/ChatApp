import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {
	Socket socket;
	BufferedReader br;
	PrintWriter out;

	private JLabel heading = new JLabel("Client Area");
	private JTextArea messaArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font;

	public Client() {
		try {
			System.out.println("sending request to server");
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("Connection Done");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			createGUI();
			handleEvents(); 
			startReading();
//			startWriting();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("Key released" + e.getKeyCode());
				if(e.getKeyCode() == 10) {
					//System.out.println("you have pressed enter button");
					String contetToSend = messageInput.getText(); 
					messaArea.append("Me :" + contetToSend +"\n");
					out.println(contetToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void createGUI() {
		this.setTitle("Client Messanger[END]");
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		heading.setFont(font);
		messaArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("chat.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		
		heading.setVerticalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		messageInput.setHorizontalAlignment(SwingConstants.CENTER );
		this.setLayout(new BorderLayout());
		this.add(heading, BorderLayout.NORTH);
		this.add(messaArea, BorderLayout.CENTER);
		this.add(messageInput, BorderLayout.SOUTH);
  
		this.setVisible(true);
	}

	private void startWriting() {
		Runnable r2 = () -> {
			System.out.println("Writer started");

			try {
				while (!socket.isClosed()) {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();

					out.println(content);
					out.flush();
					if (content.equals("exit")) {

						socket.close();
						break;
					}
				}
			} catch (Exception e) {

//				e.printStackTrace();
				System.out.println("Connection Closed");
			}
			System.out.println("Connection Closed");
		};
		new Thread(r2).start();
	}

	private void startReading() {
		Runnable r1 = () -> {
			System.out.println("reader started");

			try {
				while (true) {
					String msg;
					msg = br.readLine();
					if (msg.equals("exit")) {
						System.out.println("Server terminated the chat");
						JOptionPane.showMessageDialog(this, "Server terminated the chat");
						messageInput.setEnabled(false); 
						socket.close();
						break;
					}
					System.out.println("Server : " + msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//					e.printStackTrace();
				System.out.println("Connection Closed");
			}

		};

		new Thread(r1).start();
	}

	public static void main(String[] args) {
		System.out.println("this is client  ");
		new Client();
	}

}
