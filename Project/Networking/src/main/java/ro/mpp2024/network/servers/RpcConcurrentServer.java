package ro.mpp2024.network.servers;

import ro.mpp2024.network.objectprotocol.ClientRpcWorker;
import ro.mpp2024.services.IServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbstractConcurrentServer{

    private IServices server;

    public RpcConcurrentServer(int port, IServices s) {
        super(port);
        this.server = s;
        System.out.println("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker = new ClientRpcWorker(server, client);
        Thread t = new Thread(worker);
        return t;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
