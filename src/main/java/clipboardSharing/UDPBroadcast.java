package clipboardSharing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.DefaultListModel;

public class UDPBroadcast{
	private boolean connected = false;
	public UDPBroadcast()
	{
		
	}
	
	public static void broadcastAddress() {
		DatagramSocket c = null;
		try {
			c = new DatagramSocket();
			c.setBroadcast(true);
			InetAddress local = InetAddress.getLocalHost();
			String info = "CLIPBOARDSHARING_ADDRESS:" + local.getHostAddress();
			byte[] sendData = info.getBytes();
			while(true) {
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
	
					if (networkInterface.isLoopback() || !networkInterface.isUp()) {
						continue;
					}
					for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
						InetAddress broadcast = interfaceAddress.getBroadcast();
						if (broadcast == null) {
							continue;
						}
						// broadcast the package
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
						c.send(sendPacket);
						System.out.println("Localhost IP Address sent to: " + broadcast.getHostAddress() + " with interface: " + networkInterface.getDisplayName());
					}
				}
				Thread.sleep(2000);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(c!=null && !c.isClosed())	c.close();
		}
	}
	
	public void removeTimeout(DefaultListModel<TimedAddress> model, long timeOut){
		long current = new Date().getTime();
		for(int i = model.size()-1;i>=0;i--){
			if(model.get(i).checkExpired(current, timeOut))	model.remove(i);
		}
	}
	
	public void receiving(DefaultListModel<TimedAddress> model) {
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (!connected) {
				removeTimeout(model,10000);
				System.out.println("Ready to receive broadcast packets");

				// Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				// Packet received
				System.out.println("packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println("received data: " + new String(packet.getData()));

				// check message
				String message = new String(packet.getData()).trim();
				if (message.substring(0, 25).equals("CLIPBOARDSHARING_ADDRESS:")) {
					//String address = message.substring(25);
					System.out.println("Receive packet from: " );
					TimedAddress address = new TimedAddress(packet.getAddress());
					if(!model.contains(address)) {
						System.out.println("adding!!!!!");
						model.addElement(address);
					}
					else{
						System.out.println("update!!!!!");
						int index = model.indexOf(address);
						model.get(index).update();
					}
					
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
