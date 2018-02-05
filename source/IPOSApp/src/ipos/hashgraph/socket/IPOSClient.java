package ipos.hashgraph.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class IPOSClient {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostName(), 8889);

            // Reader and writer
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Write a message to server
            writer.println("Hello from minisha");

            // Read message from server
            System.out.println(reader.readLine());
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
