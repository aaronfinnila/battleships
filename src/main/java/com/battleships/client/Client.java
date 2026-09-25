package com.battleships.client;

import java.net.*;
import java.io.*;

public class Client {
	private Socket socket;
	private BufferedReader input;
	private DataOutputStream out;
	private DataInputStream in;
	private String message;

	public Client(String address) {
		try {
			if (address.equals("local")) {
				address = "127.0.0.1";
			}
			socket = new Socket(address, 6666);

			input = new BufferedReader(new InputStreamReader(System.in));
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			message = null;
		} catch(UnknownHostException u) {
			System.out.println(u);
		} catch(IOException i) {
			System.out.println(i);
		}

		new Thread(() -> {
			while (true) {
				try {
					String incoming = in.readUTF();
					if (incoming.length() > 4096) {
						System.out.println("Incoming readUTF size too large, closing socket");
						setMessage("Incoming readUTF size too large, closing socket");
						socket.close();
					}
					setMessage(incoming);
				} catch(IOException i) {
					System.out.println(i);
					setMessage("IOException: something wrong");
					break;
				}
			}
		}).start();

/* 		String line = "";
		try {
			System.out.println("");
			System.out.println("Host connection or connect to existing?");
			System.out.println("");
			System.out.println("1) Host connection");
			System.out.println("2) Connect to existing");
			System.out.println("");
			line = input.readLine();
			if (line.equals("1")) {
				line = "host";
			} else if (line.equals("2")) {
				System.out.println("Give connection code");
				String tempLine = input.readLine();
				line = "connect " + tempLine;
			} else {
				System.out.println("Invalid input. Closing connection");
			}
			out.writeUTF(line);
			out.flush();
			// created host / connection found / connection not found
			String incoming = in.readUTF();
			if (incoming.equals("invalidCodeLength")) {
				System.out.println("Invalid code length. Try again");
				System.exit(0);
			} else if (incoming.equals("notFound")) {
				System.out.println("Invalid code. Try again");
				System.exit(0);
			} else {
				System.out.println(incoming);
			}
			// client name
			incoming = in.readUTF();
			System.out.println(incoming);
			line = input.readLine();
			out.writeUTF(line);
			out.flush();
			// set client name
			incoming = in.readUTF();
			System.out.println(incoming);
		} catch (IOException i) {
			System.out.println(i);
		} */


		try {
			input.close();
			out.close();
			socket.close();
		} catch(IOException i) {
			System.out.println(i);
		}
	}

	public String showMessage() {
		if (message != null && !message.isEmpty()) {
			return message;
		} else {
			return null;
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
