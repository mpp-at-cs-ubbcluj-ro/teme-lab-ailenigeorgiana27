package ro.mpp2024.network.objectprotocol;

import ro.mpp2024.domain.*;
import ro.mpp2024.network.dto.*;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.services.AppException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;


public class ClientRpcWorker implements Runnable, IObserver {

    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientRpcWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            PersoanaOficiu user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (AppException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            UserDTO udto=(UserDTO)request.data();
            PersoanaOficiu user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_PROBE){
            System.out.println("GetProbe Request ...");
            //UserDTO udto=(UserDTO)request.data();
            //User user= DTOUtils.getFromDTO(udto);
            try {
                Proba[] probe=server.getProbe();
                ProbaDTO[] frDTO=DTOUtils.getDTO(probe);
                return new Response.Builder().type(ResponseType.GET_PROBE).data(frDTO).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_PARTICIPANTI_DUPA_PROBE){
            System.out.println("GET_PARTICIPNATI_DUPA_PROBE Request ...");
            ProbaDTO udto=(ProbaDTO)request.data();
            Proba p= DTOUtils.getFromDTO(udto);
            try {
                Participant[] probe=server.getParticipantiDupaProba(p);
                ParticipantDTO[] frDTO=DTOUtils.getDTO(probe);
                return new Response.Builder().type(ResponseType.GET_PARTICIPANTI_DUPA_PROBE).data(frDTO).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_PROBE_DUPA_PARTICIPANT){
            System.out.println("GET_PROBE_DUPA_PARTICIPANT Request ...");
            ParticipantDTO udto=(ParticipantDTO) request.data();
            Participant user= DTOUtils.getFromDTO(udto);
            try {
                Proba[] probe=server.getProbeDupaParticipanti(user);
                ProbaDTO[] frDTO=DTOUtils.getDTO(probe);
                return new Response.Builder().type(ResponseType.GET_PROBE_DUPA_PARTICIPANT).data(frDTO).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.INSCRIE){
            System.out.println("INSCRIE request ..."+request.type());
            InscriereDTO idto=(InscriereDTO)request.data();
            Tuple t = DTOUtils.getFromDTO(idto);
            Participant p = t.getP();
            Proba[] probas = t.getProbas();
            try {
                server.Inscrie(p,probas);
                return okResponse;
            } catch (AppException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_NR_PARTICIPANTI){
            System.out.println("GET_NR_PARTICIPANTI Request ...");
            //ProbaDTO udto=(ProbaDTO)request.data();
            //Proba p= DTOUtils.getFromDTO(udto);
            try {
                List<Integer> nr=server.getNrParticipanti();
                return new Response.Builder().type(ResponseType.GET_NR_PARTICIPANTI).data(nr).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        if (output != null && connected) {
            System.out.println("Sending response: " + response);
            output.writeObject(response);
            output.flush();
        } else {
            System.out.println("Error: output stream is null or disconnected.");
        }
    }

    @Override
    public void newInscriere() throws AppException {
        Response resp=new Response.Builder().type(ResponseType.NEW_INSCRIERE).data().build();
        System.out.println("New participant added");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
