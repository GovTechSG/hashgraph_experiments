package ipos.hashgraph.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class IPOSServer extends Thread {

    private Socket socket;

    IPOSServer(Socket socket) {
        System.out.println("Client connected");
        this.socket = socket;
    }


    public static void main ( String[] args ) {
        final int port = 8888;

        try ( ServerSocket ss = new ServerSocket(port) ) {
            System.out.println("Listening ...");
            while ( true ) {
                Socket socket = ss.accept();
                new IPOSServer(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        socket();
    }

    private void socket() {
        try {
            // Reader and writer
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Read message from client
            System.out.println(reader.readLine());

            // Write a message back to client
            writer.println("Hello from server");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
