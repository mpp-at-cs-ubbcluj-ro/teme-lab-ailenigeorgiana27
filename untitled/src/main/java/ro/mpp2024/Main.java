package ro.mpp2024;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(55556)) {
            System.out.println("Server started on port 55556...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                // Handle client connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}