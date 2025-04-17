package ro.mpp2024.repo.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.repo.IPersoanaOficiuRepo;
import ro.mpp2024.repo.utils.JdbcUtils ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PersoanaOficiuDbRepo implements IPersoanaOficiuRepo {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public PersoanaOficiuDbRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
        logger.info("Initialized PersoanaOficiuDbRepo with properties {}", properties);
    }

    @Override
    public PersoanaOficiu findbyCredentials(String username, String password) {
        logger.traceEntry("Finding PersoanaOficiu by credentials {}, {}", username, password);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"PersoanaOficiu\" WHERE \"username\" = ? AND \"password\" = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    PersoanaOficiu persoana = new PersoanaOficiu(username, password);
                    persoana.setId(id);
                    logger.traceExit(persoana);
                    return persoana;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding PersoanaOficiu by credentials", e);
        }
        logger.traceExit("No match found");
        return null;
    }

    @Override
    public Optional<PersoanaOficiu> add(PersoanaOficiu persoanaOficiu) {
        logger.traceEntry("Adding PersoanaOficiu {}", persoanaOficiu);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"PersoanaOficiu\" (\"username\", \"password\") VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, persoanaOficiu.getUsername());
            ps.setString(2, persoanaOficiu.getPassword());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        persoanaOficiu.setId(generatedKeys.getInt(1));
                    }
                }
                logger.traceExit(persoanaOficiu);
                return Optional.of(persoanaOficiu);
            }
        } catch (SQLException e) {
            logger.error("Error inserting PersoanaOficiu", e);
        }
        logger.traceExit("Insert failed");
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> update(Integer id, PersoanaOficiu persoanaOficiu) {
        logger.traceEntry("Updating PersoanaOficiu {}", persoanaOficiu);
        // Update is not allowed
        logger.warn("Update operation not allowed for PersoanaOficiu");
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> delete(PersoanaOficiu persoanaOficiu) {
        logger.traceEntry("Deleting PersoanaOficiu {}", persoanaOficiu);
        // Delete is not allowed
        logger.warn("Delete operation not allowed for PersoanaOficiu");
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> findById(Integer id) {
        logger.traceEntry("Finding PersoanaOficiu by id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"PersoanaOficiu\" WHERE \"id\" = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    PersoanaOficiu persoana = new PersoanaOficiu(username, password);
                    persoana.setId(id);
                    logger.traceExit(persoana);
                    return Optional.of(persoana);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding PersoanaOficiu by id", e);
        }
        logger.traceExit("No match found");
        return Optional.empty();
    }

    @Override
    public Iterable<PersoanaOficiu> findAll() {
        logger.traceEntry("Finding all PersoanaOficiu");
        List<PersoanaOficiu> persoane = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"PersoanaOficiu\"")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    PersoanaOficiu persoana = new PersoanaOficiu(username, password);
                    persoana.setId(id);
                    persoane.add(persoana);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all PersoanaOficiu", e);
        }
        logger.traceExit(persoane.toString());
        return persoane;
    }

    @Override
    public Collection<PersoanaOficiu> getAll() {
        return (Collection<PersoanaOficiu>) findAll();
    }
}
