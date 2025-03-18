package ro.mpp2024.repo;

import ro.mpp2024.domain.PersoanaOficiu;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class PersoanaOficiuDbRepo extends DatabaseRepoUtils<Integer, PersoanaOficiu> implements IPersoanaOficiuRepo{
    private static final Logger logger = LogManager.getLogger(PersoanaOficiuDbRepo.class);

    public PersoanaOficiuDbRepo(Properties properties) {
        super(properties);
        logger.info("Initializing PersoanaOficiuDbRepo with properties: {} " , properties);
    }

    @Override
    public PersoanaOficiu decodeResult(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("id");
        var orasOficiu = resultSet.getString("orasOficiu");
        var username = resultSet.getString("username");
        var password = resultSet.getString("password");
        var persoanaOficiu = new PersoanaOficiu(orasOficiu, username, password);
        persoanaOficiu.setId(id);
        return persoanaOficiu;
    }

    @Override
    public PersoanaOficiu findbyCredentials(String orasOficiu, String username, String password) throws EntityRepoException{
        return selectFirst("select * from \"PersoanaOficiu\" where " + "\"orasOficiu\" =? and \"username\"= ? and \"password\" = ?", orasOficiu, username, password);
    }

    @Override
    public void add(PersoanaOficiu persoanaOficiu) throws EntityRepoException{
        logger.trace("Inserting {}", persoanaOficiu);

        executeNonQuery("insert into \"PersoanaOficiu\" (\"orasOficiu\", \"username\", \"password\") values (?,?,?)", persoanaOficiu.getOrasOficiu(), persoanaOficiu.getUsername(), persoanaOficiu.getPassword());
        logger.info("Inserted succesfully");
        logger.traceExit();
    }

    @Override
    public void update(PersoanaOficiu persoanaOficiu) throws EntityRepoException{
        throw new EntityRepoException("PersoanaOficiu update is not allowed");
    }

    @Override
    public void remove(Integer integer) throws EntityRepoException{
        throw new EntityRepoException("PersoanaOficiu removal is not allowed");
    }

    @Override
    public PersoanaOficiu getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"PersoanaOficiu\" where \"id\" = ?", id);
    }

    @Override
    public Iterable<PersoanaOficiu> getAll() throws EntityRepoException{
        return select("select * from \"PersoanaOficiu\" ");
    }
}
