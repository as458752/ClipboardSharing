package clipboardSharing;

import java.net.InetAddress;
import java.util.Date;

public class TimedAddress {
	private InetAddress address;
	private long createdTime;
	public TimedAddress(InetAddress addr){
		createdTime = new Date().getTime();
		address = addr;
	}
	
	public boolean checkExpired(long current, long timeOut){
		if(current - this.createdTime >= timeOut)	return true;
		else	return false;
	}
	
	public InetAddress getAddress(){
		return this.address;
	}
	
	@Override
	public String toString(){
		return "IP Address: " + address.getHostAddress() + " Name: " + address.getHostName();
	}
	
	public void update(){
		createdTime = new Date().getTime();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)	return false;
		if(!TimedAddress.class.isAssignableFrom(obj.getClass()))	return false;
		TimedAddress addr = (TimedAddress) obj;
		return this.address.equals(addr.getAddress());
	}
}
