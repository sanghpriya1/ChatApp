import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	Socket socket;
	BufferedReader br;
	PrintWriter out;

	public Client() {
		try {
			System.out.println("sending request to server");
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("Connection Done");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			startReading();
			startWriting();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startWriting() {
		Runnable r2 = () -> {
			System.out.println("Writer started");

			try {
				while ( !socket.isClosed()) {
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
			
				try {while (true) {
					String msg;
					msg = br.readLine();
					if (msg.equals("exit")) {
						System.out.println("Client terminated the chat");
						socket.close();
						break;
					}
					System.out.println("Server : " + msg);
				} }catch (IOException e) {
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
