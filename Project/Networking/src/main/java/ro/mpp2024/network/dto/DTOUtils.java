package ro.mpp2024.network.dto;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.domain.Tuple;
import ro.mpp2024.domain.enums.Stil;

public class DTOUtils {
    public static PersoanaOficiu getFromDTO(UserDTO usdto) {
        String id = usdto.getId();
        String pass = usdto.getPasswd();
        return new PersoanaOficiu(id, pass);

    }

    public static UserDTO getDTO(PersoanaOficiu user) {
        String id = String.valueOf(user.getUsername());
        String pass = user.getPassword();
        return new UserDTO(id, pass);
    }

    public static Tuple getFromDTO(InscriereDTO idto) {
        Participant p = idto.getP();
        Proba[] probas = idto.getProbas();
        return new Tuple(p, probas);

    }

    public static InscriereDTO getDTO(Participant p, Proba[] probas) {
        return new InscriereDTO(p, probas);
    }

    public static Participant getFromDTO(ParticipantDTO usdto) {
        int id = Integer.parseInt(usdto.getId());
        String pass = usdto.getNume();
        int varsta = Integer.parseInt(usdto.getVarsta());
        Participant p = new Participant(pass, varsta);
        p.setId(id);
        return p;

    }

    public static ParticipantDTO getDTO(Participant user) {
        String id = String.valueOf(user.getId());
        String pass = user.getName();
        String va = String.valueOf(user.getAge());
        return new ParticipantDTO(id, pass, va);
    }

    public static Proba getFromDTO(ProbaDTO probadto) {
        int i = Integer.valueOf(probadto.getId());
        float id = Float.valueOf(probadto.getDistanta());
        Stil pass = Stil.valueOf(probadto.getStil());
        Proba p = new Proba(id, pass);
        p.setId(i);
        return p;

    }

    public static ProbaDTO getDTO(Proba proba) {
        String i = String.valueOf(proba.getId());
        String id = String.valueOf(proba.getDistanta());
        String pass = String.valueOf(proba.getStil());
        return new ProbaDTO(id, pass, i);
    }

    public static UserDTO[] getDTO(PersoanaOficiu[] users) {
        UserDTO[] frDTO = new UserDTO[users.length];
        for (int i = 0; i < users.length; i++)
            frDTO[i] = getDTO(users[i]);
        return frDTO;
    }

    public static PersoanaOficiu[] getFromDTO(UserDTO[] users) {
        PersoanaOficiu[] friends = new PersoanaOficiu[users.length];
        for (int i = 0; i < users.length; i++) {
            friends[i] = getFromDTO(users[i]);
        }
        return friends;
    }

    public static ProbaDTO[] getDTO(Proba[] probe) {
        ProbaDTO[] frDTO = new ProbaDTO[probe.length];
        for (int i = 0; i < probe.length; i++)
            frDTO[i] = getDTO(probe[i]);
        return frDTO;
    }

    public static Proba[] getFromDTO(ProbaDTO[] probedto) {
        Proba[] friends = new Proba[probedto.length];
        for (int i = 0; i < probedto.length; i++) {
            friends[i] = getFromDTO(probedto[i]);
        }
        return friends;
    }

    public static ParticipantDTO[] getDTO(Participant[] probe) {
        ParticipantDTO[] frDTO = new ParticipantDTO[probe.length];
        for (int i = 0; i < probe.length; i++)
            frDTO[i] = getDTO(probe[i]);
        return frDTO;
    }

    public static Participant[] getFromDTO(ParticipantDTO[] probedto) {
        Participant[] friends = new Participant[probedto.length];
        for (int i = 0; i < probedto.length; i++) {
            friends[i] = getFromDTO(probedto[i]);
        }
        return friends;
    }
}
