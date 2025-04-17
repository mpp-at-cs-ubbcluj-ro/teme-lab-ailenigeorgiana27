package ro.mpp2024.repo;

import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.stream.StreamSupport;
public class ParticipantDbRepo extends DatabaseRepoUtils<Integer, Participant> implements IParticipantRepo{
    private static final Logger logger = LogManager.getLogger(ParticipantDbRepo.class);

    public ParticipantDbRepo(Properties properties) {
        super(properties);
        logger.info("Initializing ParticipantDbRepo with properties {} ", properties);
    }

    public Participant decodeResult(ResultSet result) throws SQLException {
        var name = result.getString("name");
        var age = result.getInt("age");
        var nrProbe = result.getInt("nrProbe");
        var id = result.getInt("id");
        var participant = new Participant(name, age, nrProbe);
        participant.setId(id);
        return participant;
    }

    @Override
    public void add(Participant participant) throws EntityRepoException{
        logger.trace("Inserting {}", participant);

        executeNonQuery("insert into \"Participant\" (\"name\", \"age\", \"neProbe\") values (?,?,?)", participant.getName(), participant.getAge(), participant.getNrProbe());
        logger.info("Inserted succesfully");
        logger.traceExit();
    }

    @Override
    public void update(Participant participant) throws EntityRepoException{
        throw new EntityRepoException("Participant update is not allowed");
    }

    @Override
    public void remove(Integer integer) throws EntityRepoException{
        throw new EntityRepoException("Participant removal is not allowed");
    }

    @Override
    public Participant getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"Participant\" where \"id\" = ?", id);
    }

    @Override
    public Iterable<Participant> getAll() throws EntityRepoException{
        return select("select * from \"Participant\" ");
    }

    @Override
    public Iterable<Participant> getbyNameAndAge(String name, int age) throws EntityRepoException{
        return select("select * from \"Participant\" where \"name\" = ? and \"age\" = ?",
                name,
                age);
    }
}
