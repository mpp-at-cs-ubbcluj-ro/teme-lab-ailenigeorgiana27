package ro.mpp2024.repo.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.domain.enums.Stil;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.repo.IProbaRepo;
import ro.mpp2024.repo.utils.JdbcUtils;

import java.sql.*;
import java.util.*;

public class ProbaDbRepo implements IProbaRepo {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public ProbaDbRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
        logger.info("Initialized ProbaDbRepo with properties {}", properties);
    }

    @Override
    public Optional<Proba> add(Proba proba) {
        logger.traceEntry("Adding Proba {}", proba);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"Proba\" (\"stil\", \"distance\") VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proba.getStil().toString());
            ps.setFloat(2, proba.getDistanta());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        proba.setId(generatedKeys.getInt(1));
                    }
                }
                logger.traceExit(proba);
                return Optional.of(proba);
            }
        } catch (SQLException e) {
            logger.error("Error inserting Proba", e);
        }
        logger.traceExit("Insert failed");
        return Optional.empty();
    }

    @Override
    public Optional<Proba> update(Integer id, Proba proba) {
        logger.traceEntry("Updating Proba {}", proba);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE \"Proba\" SET \"stil\" = ?, \"distance\" = ? WHERE \"id\" = ?")) {
            ps.setString(1, proba.getStil().toString());
            ps.setFloat(2, proba.getDistanta());
            ps.setInt(3, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                proba.setId(id);
                logger.traceExit(proba);
                return Optional.of(proba);
            }
        } catch (SQLException e) {
            logger.error("Error updating Proba", e);
        }
        logger.traceExit("Update failed");
        return Optional.empty();
    }

    @Override
    public Optional<Proba> delete(Proba proba) {
        logger.traceEntry("Deleting Proba {}", proba);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM \"Proba\" WHERE \"id\" = ?")) {
            ps.setInt(1, proba.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                logger.traceExit(proba);
                return Optional.of(proba);
            }else {
                logger.warn("No Proba found with id={}", proba.getId());
            }
        } catch (SQLException e) {
            logger.error("Error deleting Proba", e);
        }
        logger.traceExit("Delete failed");
        return Optional.empty();
    }

    @Override
    public Optional<Proba> findById(Integer id) {
        logger.traceEntry("Finding Proba by id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"Proba\" WHERE \"id\" = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String stil = rs.getString("stil");
                    Float distanta = rs.getFloat("distance");
                    Proba proba = new Proba(distanta, Stil.valueOf(stil));
                    proba.setId(id);
                    logger.traceExit(proba);
                    return Optional.of(proba);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding Proba by id", e);
        }
        logger.traceExit("No match found");
        return Optional.empty();
    }

    @Override
    public Iterable<Proba> findAll() {
        logger.traceEntry("Finding all Proba");
        List<Proba> probe = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"Proba\"")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String stil = rs.getString("stil");
                    Float distanta = rs.getFloat("distance");
                    Proba proba = new Proba(distanta, Stil.valueOf(stil));
                    proba.setId(id);
                    probe.add(proba);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all Proba", e);
        }
        logger.traceExit(probe.toString());
        return probe;
    }

    @Override
    public Collection<Proba> getAll() {
        return (Collection<Proba>) findAll();
    }

    @Override
    public Iterable<Proba> filterByStil(String stil)  {
        logger.traceEntry("Filtering Proba by stil {}", stil);
        List<Proba> result = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"Proba\" WHERE \"stil\" = ?")) {
            ps.setString(1, stil);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    Float distanta = rs.getFloat("distance");
                    Proba proba =new Proba(distanta, Stil.valueOf(stil));
                    proba.setId(id);
                    result.add(proba);
                }
            }
        } catch (SQLException e) {
            logger.error("Error filtering Proba by stil", e);
        }
        logger.traceExit(result);
        return result;
    }
}
