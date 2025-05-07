package ro.mpp2024.repo;

import ro.mpp2024.domain.Participant;

public interface IParticipantRepo extends IRepo<Integer, Participant> {
        Iterable<Participant> filterByName(String name) ;

        Iterable<Participant> filterByAge(int age);
}
