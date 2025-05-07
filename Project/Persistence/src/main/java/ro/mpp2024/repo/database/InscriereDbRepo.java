package ro.mpp2024.repo.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.IInscriereRepo;
import ro.mpp2024.repo.IParticipantRepo;
import ro.mpp2024.repo.IProbaRepo;
import ro.mpp2024.repo.utils.JdbcUtils;

import java.sql.*;
import java.util.*;

public class InscriereDbRepo implements IInscriereRepo {
    private static final Logger logger = LogManager.getLogger(InscriereDbRepo.class);
    private final JdbcUtils dbUtils;
    private final IParticipantRepo participantRepo;
    private final IProbaRepo probaRepo;

    public InscriereDbRepo(Properties properties, IParticipantRepo participantRepo, IProbaRepo probaRepo) {
        dbUtils = new JdbcUtils(properties);
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
        logger.info("Initialized InscriereDbRepo with properties {}", properties);
    }

    @Override
    public Optional<Inscriere> add(Inscriere inscriere) {
        logger.traceEntry("Adding Inscriere {}", inscriere);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"Inscriere\" (\"participant\", \"proba\") VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inscriere.getParticipant().getId());
            ps.setInt(2, inscriere.getProba().getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inscriere.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Inserted Inscriere successfully");
                logger.traceExit(inscriere);
                return Optional.of(inscriere);
            }
        } catch (SQLException e) {
            logger.error("Error inserting Inscriere", e);
        }
        logger.traceExit("Insert failed");
        return Optional.empty();
    }

    @Override
    public Optional<Inscriere> update(Integer id, Inscriere inscriere) {
        logger.traceEntry("Updating Inscriere with id {} and new data {}", id, inscriere);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE \"Inscriere\" SET \"participant\" = ?, \"proba\" = ? WHERE \"id\" = ?")) {
            ps.setInt(1, inscriere.getParticipant().getId());
            ps.setInt(2, inscriere.getProba().getId());
            ps.setInt(3, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Updated Inscriere successfully with id {}", id);
                logger.traceExit(inscriere);
                return Optional.of(inscriere);
            }
        } catch (SQLException e) {
            logger.error("Error updating Inscriere with id {}", id, e);
        }
        logger.traceExit("Update failed for id {}", id);
        return Optional.empty();
    }

    @Override
    public Optional<Inscriere> delete(Inscriere inscriere) {
        logger.traceEntry("Deleting Inscriere {}", inscriere);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM \"Inscriere\" WHERE \"id\" = ?")) {
            ps.setInt(1, inscriere.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Removed Inscriere id={}", inscriere.getId());
                logger.traceExit(inscriere);
                return Optional.of(inscriere);
            } else {
                logger.warn("No Inscriere found with id={}", inscriere.getId());
            }
        } catch (SQLException e) {
            logger.error("Error deleting Inscriere with id={}", inscriere.getId(), e);
        }
        logger.traceExit("Delete failed");
        return Optional.empty();
    }

    @Override
    public Optional<Inscriere> findById(Integer id) {
        logger.traceEntry("Finding Inscriere by id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Inscriere\" WHERE \"id\" = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inscriere inscriere = decodeInscriere(rs);
                    logger.traceExit(inscriere);
                    return Optional.of(inscriere);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding Inscriere by id", e);
        }
        logger.traceExit("No match found");
        return Optional.empty();
    }

    @Override
    public Iterable<Inscriere> findAll() {
        logger.traceEntry("Finding all Inscriere");
        List<Inscriere> inscriereList = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Inscriere\"")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscriereList.add(decodeInscriere(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all Inscriere", e);
        }
        logger.traceExit(inscriereList.toString());
        return inscriereList;
    }

    private Inscriere decodeInscriere(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int participantId = rs.getInt("participant");
        int probaId = rs.getInt("proba");

        // Assuming you have methods to get Participant and Proba by their IDs
        Participant participant = participantRepo.findById(participantId).orElseThrow(() -> new SQLException("Participant not found"));
        Proba proba = probaRepo.findById(probaId).orElseThrow(() -> new SQLException("Proba not found"));

        // Create the Inscriere object and set the values
        Inscriere inscriere = new Inscriere(participant, proba);
        inscriere.setId(id);

        return inscriere;
    }

    @Override
    public List<Inscriere> getInscrierePentruParticipant(Participant participant) {
        logger.traceEntry("Finding Inscriere for Participant {}", participant);
        List<Inscriere> inscriereList = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Inscriere\" WHERE \"participant\" = ?")) {
            ps.setInt(1, participant.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscriereList.add(decodeInscriere(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving Inscriere for Participant", e);
        }
        logger.traceExit(inscriereList);
        return inscriereList;
    }

    @Override
    public List<Inscriere> getInscrierePentruProba(Proba proba) {
        logger.traceEntry("Finding Inscriere for Proba {}", proba);
        List<Inscriere> inscriereList = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Inscriere\" WHERE \"proba\" = ?")) {
            ps.setInt(1, proba.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscriereList.add(decodeInscriere(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving Inscriere for Proba", e);
        }
        logger.traceExit(inscriereList);
        return inscriereList;
    }
    @Override
    public Collection<Inscriere> getAll() {
        logger.traceEntry("Finding all Inscriere");
        List<Inscriere> inscriereList = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"Inscriere\"")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscriereList.add(decodeInscriere(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all Inscriere", e);
        }
        logger.traceExit(inscriereList.toString());
        return inscriereList;
    }

}
