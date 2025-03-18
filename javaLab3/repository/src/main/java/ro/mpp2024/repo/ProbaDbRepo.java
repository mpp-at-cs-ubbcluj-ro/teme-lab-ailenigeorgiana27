package ro.mpp2024.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Proba;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class ProbaDbRepo extends DatabaseRepoUtils<Integer, Proba> implements IProbaRepo{
    private static Logger logger = LogManager.getLogger(ProbaDbRepo.class);

    public ProbaDbRepo(Properties properties) {
        super(properties);
        logger.info("Initializing ProbaDbRepo with properties {} ", properties);
    }

    @Override
    public Proba decodeResult(ResultSet result) throws SQLException {
        var distance = result.getString("distance");
        var stil = result.getString("stil");
        var nrParticipants  = result.getInt("nrParticipants");
        var id = result.getInt("id");
        var proba = new Proba(distance, stil, nrParticipants);
        proba.setId(id);
        return proba;
    }

    @Override
    public void add(Proba proba) throws EntityRepoException{
        logger.trace("Inserting {} ", proba);
        executeNonQuery("insert into \"Proba\" " + "(\"distance\", \"stil\", \"nrParticipants\") " +
                "values (?,?,?)",
                proba.getDistance(),
                proba.getStil(),
                proba.getNrParticipants());
        logger.info("Inserted successfully");
        logger.traceExit();
    }

    @Override
    public void update(Proba proba) throws EntityRepoException{
        var sql = "update \"Proba\" set" +
                "distance = ?, " +
                "stil = ?, " +
                "nrParticipants = ? " +
                "where id = ?";
        logger.trace("Updating proba {} ", proba);
        executeNonQuery(sql,
                proba.getDistance(),
                proba.getStil(),
                proba.getNrParticipants(),
                proba.getId());
        logger.info("Updated proba {} ", proba);
        logger.traceExit();
    }

    @Override
    public void remove(Integer integer) throws EntityRepoException{
        throw new EntityRepoException("Proba removal is not allowed");
    }

    @Override
    public Proba getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"Proba\" where id = ?", id);
    }

    @Override
    public Iterable<Proba> getAll() throws EntityRepoException{
        return select("select * from \"Proba\" ");
    }

    @Override
    public Iterable<Proba> getbyDistanceAndStil(String distance, String stil) throws EntityRepoException{
        return select("select * from \"Proba\" where distance = ? and stil = ?", distance, stil);
    }
}
