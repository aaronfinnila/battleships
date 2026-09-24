package com.battleships.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String clientName;
    private static HashMap<Socket, Socket> allConnections = new HashMap<>();

    public ClientHandler(String hostOrConnect, Socket connectToSocket, Socket socket) {
        this.socket = socket;
        if (connectToSocket != null) {
            allConnections.put(socket, connectToSocket);
            allConnections.put(connectToSocket, socket);
        }
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Give a name for this client");
            System.out.println("Waiting for client name...");
            clientName = in.readUTF();
            System.out.println("Received client name: " + clientName);
            out.writeUTF("Set client name as " + clientName);
            while (true) {
                String message = in.readUTF();
                System.out.println("Received: '" + message + "', from: " + clientName);
                if (allConnections.get(socket) != null) {
                    DataOutputStream otherOut = new DataOutputStream(allConnections.get(socket).getOutputStream());
                    otherOut.writeUTF(clientName + ": " + message);
                    otherOut.flush();
                    System.out.println("Sent: " + clientName + ": " + message);
                } else {
                    out.writeUTF("No neighbors found");
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
                
            }
        }
    }
}
