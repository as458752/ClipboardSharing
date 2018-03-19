package clipboardSharing;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.InetAddress;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class ClipboardMonitor extends Thread implements ClipboardOwner {
	static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	InetAddress address;
	public ClipboardMonitor(InetAddress add) {
		super();
		Transferable content = clipboard.getContents(this);
		clipboard.setContents(content, this);
		this.address = add;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void paste(String str)
	{
		StringSelection selection = new StringSelection(str);
		clipboard.setContents(selection, this);
	}
	
	public void lostOwnership(Clipboard cb, Transferable oldContent) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("content changed");
		Transferable content = cb.getContents(this);
		cb.setContents(content, this);
		String dstData;
		if(cb.isDataFlavorAvailable(DataFlavor.stringFlavor))
		{
			try {
				dstData = (String) content.getTransferData(DataFlavor.stringFlavor);
				System.out.println(dstData);
				//SocketCommunication.sendString(this.address, dstData);
			} catch (UnsupportedFlavorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("not string");
		}
	}

}
