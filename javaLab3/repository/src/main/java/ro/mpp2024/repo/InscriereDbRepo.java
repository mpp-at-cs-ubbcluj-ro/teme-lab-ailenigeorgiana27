package ro.mpp2024.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class InscriereDbRepo extends DatabaseRepoUtils<Integer, Inscriere> implements IInscriereRepo {
    private static final Logger logger = LogManager.getLogger(InscriereDbRepo.class);

    IParticipantRepo participantRepo;
    IProbaRepo probaRepo;

    public InscriereDbRepo(Properties properties, IParticipantRepo participantRepo, IProbaRepo probaRepo) {
        super(properties);
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
    }

    @Override
    public Inscriere decodeResult(ResultSet result) throws SQLException, EntityRepoException {
        var id = result.getInt("id");
        var participantId = result.getInt("participant");
        var probaId = result.getInt("proba");
        var participant = participantRepo.getById(participantId);
        var proba = probaRepo.getById(probaId);
        var inscriere = new Inscriere(participant, proba);
        inscriere.setId(id);
        return inscriere;
    }

    @Override
    public Iterable<Inscriere> getbyProbaParticipant(Proba proba, Participant participant) throws EntityRepoException{
        return select("select * from \"Inscriere\" where \"proba\" = ? and \"participant\" = ?", proba, participant);
    }

    @Override
    public void add(Inscriere inscriere) throws EntityRepoException{
        logger.trace("Inserting {} ", inscriere);

        executeNonQuery("insert into \"Inscriere\" (\"participant\", \"proba\") values (?, ?)",
                inscriere.getParticipant().getId(),
                inscriere.getProba().getId());
        logger.info("Inserted successfully");
        logger.traceExit();
    }

    @Override
    public void update(Inscriere inscriere) throws EntityRepoException{
        logger.trace("Updating inscriere {} ", inscriere);
        executeNonQuery("update \"Inscriere\" set " +
                "\"participant\"=?," +
                "\"proba\" = ? where \"id\"= ?",
                inscriere.getParticipant().getId(),
                inscriere.getProba().getId(),
                inscriere.getId());
        logger.info("Updated inscriere {}",inscriere);
        logger.traceExit();
    }

    @Override
    public void remove(Integer id) throws EntityRepoException{
        logger.trace("Removing inscriere id= {} ", id);
        executeNonQuery("delete from \"Inscriere\" where \"id\" = ?", id);
        logger.info("Removed inscriere id={}",id);
        logger.traceExit();
    }

    @Override
    public Inscriere getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"Inscriere\" where \"id\" = ?", id);
    }

    @Override
    public Iterable<Inscriere> getAll() throws EntityRepoException{
        return select("select * from \"Inscriere\"");
    }
}
