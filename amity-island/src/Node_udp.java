import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import processing.core.PApplet;
import hypermedia.net.*;

public class Node_udp {
	
	UDP udp;
	String beaglebone_ip;
	String host_ip;
	
	ByteBuffer serialBuffer = ByteBuffer.allocate(8);
	
	public Node_udp(String ip_destination, String ip_host) {
		beaglebone_ip = ip_destination;
		host_ip = ip_host;
		udp = new UDP(this);
	}
	
	public void sendPacket(String message, int port) {
		udp.send(message, beaglebone_ip, port);
	}
	
	
	public void recievePacket( int port) {
		//do stuff in here to receive packets
	}
	
	String sabretoothPacket(int address, int command, float power) throws UnsupportedEncodingException {

		if (power < 0) {
			power *= -1;
			command += 1;
		}
		int speed = (int) (power * 127.0f);
		int checksum = (speed + address + command) & 127;
		byte[] packet = {(byte) address, (byte) command, (byte) speed, (byte) checksum};
		String out = new String(packet, "UTF-8");

		return out;
		
	}
}
