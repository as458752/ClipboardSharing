package clipboardSharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketCommunication {
	public static void sendString(InetAddress address, String str) {
		Socket socket;
		int portNumber = 1777;

		try {
			socket = new Socket(address, portNumber);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

			pw.println(str);

			while ((str = br.readLine()) != null) {
				System.out.println(str);
				pw.println("ACK");

				if (str.equals("SYN-ACK"))
					break;
			}

			br.close();
			pw.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String receiveString() {
		int cTosPortNumber = 1777;
		String message = null, str;

		ServerSocket servSocket;
		try {
			servSocket = new ServerSocket(cTosPortNumber);
			System.out.println("Waiting for a connection on " + cTosPortNumber);

			Socket fromClientSocket = servSocket.accept();
			PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(), true);

			BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));

			while ((str = br.readLine()) != null) {
				System.out.println("The message: " + message);
				pw.println("SYN-ACK");
				if (str.equals("ACK"))
					break;
				else
					message = str;
			}
			pw.close();
			br.close();

			fromClientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
