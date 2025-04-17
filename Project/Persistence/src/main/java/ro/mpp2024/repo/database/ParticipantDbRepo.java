package ro.mpp2024.repo.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.repo.IParticipantRepo;
import ro.mpp2024.repo.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantDbRepo implements IParticipantRepo {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public ParticipantDbRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
        logger.info("Initialized ParticipantDbRepo with properties {}", properties);
    }

    @Override
    public Optional<Participant> add(Participant participant) {
        logger.traceEntry("Adding Participant {}", participant);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"Participant\" (\"name\", \"age\") VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, participant.getName());
            ps.setInt(2, participant.getAge());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        participant.setId(generatedKeys.getInt(1));
                    }
                }
                logger.traceExit(participant);
                return Optional.of(participant);
            }
        } catch (SQLException e) {
            logger.error("Error inserting Participant", e);

        }
        logger.traceExit("Insert failed");
        return Optional.empty();
    }

    @Override
    public Optional<Participant> update(Integer id, Participant entity) {
        logger.traceEntry("Updating Participant with id {} -> {}", id, entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE \"Participant\" SET \"name\" = ?, \"age\" = ? WHERE \"id\" = ?")) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getAge());
            ps.setInt(3, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                entity.setId(id);
                logger.traceExit(entity);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error updating Participant", e);
        }
        logger.traceExit("Update failed");
        return Optional.empty();
    }


    @Override
    public Optional<Participant> delete(Participant entity) {
        logger.traceEntry("Deleting Participant {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM \"Participant\" WHERE \"id\" = ?")) {
            ps.setInt(1, entity.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                logger.traceExit(entity);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error deleting Participant", e);
        }
        logger.traceExit("Delete failed");
        return Optional.empty();
    }


    @Override
    public Optional<Participant> findById(Integer id) {
        logger.traceEntry("Finding Participant by id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Participant\" WHERE \"id\" = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Participant participant = decodeParticipant(rs);
                    logger.traceExit(participant);
                    return Optional.of(participant);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding Participant by id", e);
        }
        logger.traceExit("No match found");
        return Optional.empty();
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry("Finding all Participants");
        List<Participant> participants = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Participant\"")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Participant participant = decodeParticipant(rs);
                    participants.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all Participants", e);
        }
        logger.traceExit(participants.toString());
        return participants;
    }

    @Override
    public Collection<Participant> getAll() {
        return (Collection<Participant>) findAll();
    }

    @Override
    public Iterable<Participant> filterByName(String name) {
        logger.traceEntry("Filtering Participants by name {}", name);
        List<Participant> participants = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Participant\" WHERE \"name\" = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    participants.add(decodeParticipant(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error filtering Participants by name", e);
        }
        logger.traceExit(participants.toString());
        return participants;
    }

    @Override
    public Iterable<Participant> filterByAge(int age) {
        logger.traceEntry("Filtering Participants by age {}", age);
        List<Participant> participants = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Participant\" WHERE \"age\" = ?")) {
            ps.setInt(1, age);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    participants.add(decodeParticipant(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error filtering Participants by age", e);
        }
        logger.traceExit(participants.toString());
        return participants;
    }

    private Participant decodeParticipant(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        Participant participant = new Participant(name, age);
        participant.setId(id);
        return participant;
    }
}
