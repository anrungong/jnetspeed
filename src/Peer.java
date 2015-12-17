import java.net.*;
import java.io.*;

public class Peer {
	
	private static ServerSocket server;
	
	private static Socket accept(int port) {
		try {
			Socket client = server.accept();
			return client;
		}
		catch (Exception e) {
			System.out.println("Connection error!");
			e.printStackTrace();
		}
		return null;
	}
	
	private static int receiveData(int nbytes, Socket client) {
		try {
			byte[] buf = new byte[nbytes];
			InputStream socketin = client.getInputStream();
			int len = socketin.read(buf);
			//socketin.close();
			return len;
		}
		catch (IOException e) {
			System.out.println("No data received!");
			e.printStackTrace();
		}
		return -1;
	}
	
	private static long receiver(int port, int nbytes, int count) throws IOException {
		Socket client = accept(port);
		long time = System.currentTimeMillis();
		for (int l = 0; ; ) {
			int t = receiveData(nbytes, client);
			if (t == -1) {
				System.out.printf"%d bytes received!\n", l);
				break;
			}
			l += t;
		}
		client.close();
		return System.currentTimeMillis() - time;
	}
	
	private static void sendData(byte[] buf, Socket client) throws Exception {
		try {
			OutputStream socketout = client.getOutputStream();
			socketout.write(buf);
			socketout.flush();
			//socketout.close();
		}
		catch (IOException e) {
			System.out.println("Data send error!");
			e.printStackTrace();
		}
	}
	
	private static void sender(String host, int port, int nbytes, int count) {
		try {
			Socket client = new Socket(host, port);
			byte[] buf = new byte[nbytes];
			
			for (int i = 0; i < nbytes; i++) {
				buf[i] = 1;
			}
			
			for (int i = 0; i < count; i++) {
				sendData(buf, client);
			}
			//client.close();
		}
		catch (Exception e) {
			System.out.println("Data send error!");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String type = args[0];
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		int nbytes = Integer.parseInt(args[3]);
		int count = Integer.parseInt(args[4]);
		if (type.equals("client")) {
			sender(host, port, nbytes, count);
		}
		else if (type.equals("server")) {
			server = new ServerSocket(port);
			long time = receiver(port, nbytes, count);
			server.close();
			double speed = (double)nbytes / (double)time * (double)count * 1000;
			System.out.printf("Speed is %f bytes/s", speed);
		}
		else {
			System.out.println("Param error!");
		}
	}
}
