package ro.mpp2024.server;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.*;
import ro.mpp2024.services.AppException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Service implements IServices {
    private IPersoanaOficiuRepo persoanaOficiuRepo;
    private IParticipantRepo participantRepo;
    private IProbaRepo probaRepo;
    private IInscriereRepo inscriereRepo;
    private Map<String, IObserver> loggedClients;

    public Service(IPersoanaOficiuRepo persoanaOficiuRepo, IParticipantRepo participantRepo, IProbaRepo probaRepo, IInscriereRepo inscriereRepo) {
        this.persoanaOficiuRepo = persoanaOficiuRepo;
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
        this.inscriereRepo = inscriereRepo;
        loggedClients = new ConcurrentHashMap<>();
    }


    public Iterable<PersoanaOficiu> getUtilizatori() {
        return persoanaOficiuRepo.findAll();
    }

    @Override
    public synchronized Proba[] getProbe() throws AppException {
        List<Proba> list = (List<Proba>) probaRepo.findAll();
        Proba[] probe = new Proba[list.size()];
        for (int i = 0; i < list.size(); i++) {
            probe[i] = list.get(i);
        }
        return probe;
    }

    @Override
    public synchronized Participant[] getParticipantiDupaProba(Proba p) throws AppException {
        List<Inscriere> list = new ArrayList<>();
        List<Participant> list2 = new ArrayList<>();
        inscriereRepo.findAll().forEach(x -> {
            list.add((Inscriere) x);
        });
        list.forEach(x -> {
            if (x.getProba().getId().equals(p.getId())) {
                list2.add(x.getParticipant());
            }
        });
        Participant[] participants = new Participant[list2.size()];
        for (int i = 0; i < list2.size(); i++) {
            participants[i] = list2.get(i);
        }
        System.out.println("Id" + participants[0].getId());
        return participants;
    }

    public synchronized Proba[] getProbeDupaParticipanti(Participant p) throws AppException {
        List<Inscriere> list = new ArrayList<>();
        List<Proba> list2 = new ArrayList<>();
        inscriereRepo.findAll().forEach(x -> {
            list.add((Inscriere) x);
        });
        list.forEach(x -> {
            if (x.getParticipant().getId().equals(p.getId())) {
                list2.add(x.getProba());
            }
        });
        Proba[] probe = new Proba[list2.size()];
        for (int i = 0; i < list2.size(); i++) {
            probe[i] = list2.get(i);
        }
        return probe;
    }

    public boolean existaInscriere(Participant participant, Proba proba) {
        List<Inscriere> list = new ArrayList<>();
        boolean ok = false;
        inscriereRepo.findAll().forEach(x -> {
            list.add((Inscriere) x);
        });
        for (var i : list) {
            if (i.getParticipant().getId() == participant.getId() && i.getProba().getId() == proba.getId())
                return true;
        }
        return false;

    }

    @Override
    public void Inscrie(Participant participant, Proba[] probas) throws AppException {
        List<Proba> probe = new ArrayList<>();
        for (int i = 0; i < probas.length; i++) {
            probe.add(probas[i]);
        }
        List<Participant> list = new ArrayList<>();
        boolean ok = false;
        participantRepo.findAll().forEach(x -> {
            list.add(x);
        });

        for (var i : list) {
            if (i.getName().equals(participant.getName()) && i.getAge() == participant.getAge())
                ok = true;
        }

        if (!ok) {
            participantRepo.add(participant);
        }
        for (var i : participantRepo.findAll()) {
            if (i.getName().equals(participant.getName()) && i.getAge() == participant.getAge()) {
                participant.setId(i.getId());
            }
        }

        probe.forEach(x -> {
            if (!existaInscriere(participant, x)) {
                Inscriere inscriere = new Inscriere(participant, x);
                inscriereRepo.add(inscriere);
            }
        });
        notifyNewInscriere();

    }

    @Override
    public synchronized List<Integer> getNrParticipanti() throws AppException {
        List<Integer> list = new ArrayList<>();
        probaRepo.findAll().forEach(p -> {
            AtomicInteger nr = new AtomicInteger();
            nr.set(0);
            inscriereRepo.findAll().forEach(x -> {
                if (x.getProba().getId() == p.getId()) {
                    nr.getAndIncrement();
                }
            });
            list.add(nr.get());
        });
        return list;
    }

    @Override
    public synchronized void login(PersoanaOficiu user, IObserver client) throws AppException {
        PersoanaOficiu userR = persoanaOficiuRepo.findbyCredentials(user.getUsername(), user.getPassword());
        if (userR != null) {
            if (loggedClients.get(user.getUsername()) != null)
                throw new AppException("User already logged in.");
            loggedClients.put(user.getUsername(), client);
        } else
            throw new AppException("Authentication failed.");
    }

    @Override
    public void logout(PersoanaOficiu user, IObserver client) throws AppException {
        IObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null)
            throw new AppException("User " + user.getId() + " is not logged in.");
    }

    private final int defaultThreadsNo = 5;

    private void notifyNewInscriere() throws AppException {
        Iterable<PersoanaOficiu> users = persoanaOficiuRepo.findAll();
        System.out.println("Logged " + users);

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (PersoanaOficiu us : users) {
            IObserver Client = loggedClients.get(us.getUsername());
            if (Client != null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us.getId() + "] logged in.");
                        Client.newInscriere();
                    } catch (AppException e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }


}


