package ro.mpp2024.server;

import ro.mpp2024.network.servers.AbstractServer;
import ro.mpp2024.network.servers.RpcConcurrentServer;
import ro.mpp2024.repo.database.InscriereDbRepo;
import ro.mpp2024.repo.database.ParticipantDbRepo;
import ro.mpp2024.repo.database.PersoanaOficiuDbRepo;
import ro.mpp2024.repo.database.ProbaDbRepo;
import ro.mpp2024.services.IServices;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        PersoanaOficiuDbRepo persoanaOficiuDbRepo = new PersoanaOficiuDbRepo(serverProps);
        ProbaDbRepo probaDbRepo = new ProbaDbRepo(serverProps);
        ParticipantDbRepo participantDbRepo = new ParticipantDbRepo(serverProps);
        InscriereDbRepo inscriereDbRepo = new InscriereDbRepo(serverProps, participantDbRepo, probaDbRepo);


        IServices service = new Service(persoanaOficiuDbRepo, participantDbRepo, probaDbRepo, inscriereDbRepo);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
