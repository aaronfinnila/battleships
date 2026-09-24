package com.battleships.server;

import java.net.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.io.*; 

public class Server {
	private Socket socket;
	private ServerSocket server;
	private DataInputStream in;
	private DataOutputStream out;
	private HashMap<Integer, Socket> codes = new HashMap<>();

	public Server(int port) { 
		try {
			server = new ServerSocket(port);
			System.out.println("Server initialized");
			System.out.println("Waiting for a client...");

			while (true) {
				socket = server.accept();
				socket.setSoTimeout(15000);
				System.out.println("Incoming connection");
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				String hostOrConnect = "";
				try {
					hostOrConnect = in.readUTF();
				} catch (IOException i) {
					System.out.println("Connection timed out");
				}
				if (hostOrConnect.equals("host")) {
					SecureRandom random = new SecureRandom();
					int otp = 100000 + random.nextInt(900000);
					codes.put(otp, socket);
					out.writeUTF("Host created with code " + otp);
					socket.setSoTimeout(0);
					ClientHandler handler = new ClientHandler("host", null, socket);
					System.out.println("Connection accepted");
					new Thread(handler).start();
				} else if (hostOrConnect.startsWith("connect")) {
					if (hostOrConnect.length() != 14) {
						out.writeUTF("invalidCodeLength");
						System.out.println("Invalid code length. Closing connection");
						socket.close();
					}
					int code = Integer.parseInt(hostOrConnect.substring(8, 14));
					System.out.println(code);
					if (codes.get(code) != null) {
						out.writeUTF("Connection found");
						socket.setSoTimeout(0);
						ClientHandler handler = new ClientHandler("connect", codes.get(code), socket);
						System.out.println("Connection accepted");
						new Thread(handler).start();
						codes.remove(code);
					} else {
						out.writeUTF("notFound");
						System.out.println("Invalid code. Closing connection");
						socket.close();
					}
				} else {
					socket.close();
				}
			}

		} catch(IOException i) {
			System.out.println("Server socket error");
			System.out.println(i);
		} 
	} 

	public static void main(String args[]) { 
		Server server = new Server(6666); 
	} 
} 
