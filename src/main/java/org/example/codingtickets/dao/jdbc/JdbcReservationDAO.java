package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.EvenementDAO;
import org.example.codingtickets.dao.ReservationDAO;
import org.example.codingtickets.dao.UtilisateurDAO;
import org.example.codingtickets.exception.DaoException;
import org.example.codingtickets.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReservationDAO implements ReservationDAO {
    private final UtilisateurDAO utilisateurDAO = new JdbcUtilisateurDAO();
    private final EvenementDAO evenementDAO = new JdbcEvenementDAO();

    @Override
    public Reservation findById(long id) {
        String sql = "SELECT * FROM reservation WHERE id_reservation = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de la réservation ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de la liste des réservations", e);
        }
        return list;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(findById((long) id));
    }

    @Override
    public void save(Reservation r) {
        String sql = """
            INSERT INTO reservation(datereservation, nbredeplace, montanttotal,\s
                                    statutenum, id_client, id_evenement)
            VALUES (?, ?, ?, ?, ?, ?)
           \s""";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(r.getDateReservation()));
            ps.setInt(2, r.getNbPlaces());
            ps.setBigDecimal(3, r.getMontantTotal());
            ps.setString(4, r.getStatut().name());

            if (r.getClient() == null || r.getEvenement() == null) {
                throw new DaoException("Impossible de sauvegarder : Client ou Evénement manquant.");
            }

            ps.setLong(5, r.getClient().getId());
            ps.setLong(6, r.getEvenement().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("Échec de la création de la réservation, aucune ligne ajoutée.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    r.setId(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("Échec de la création de la réservation, aucun ID obtenu.");
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la sauvegarde de la réservation", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id_reservation = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new DaoException("Aucune réservation trouvée avec l'ID " + id);
            }

        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression de la réservation ID: " + id, e);
        }
    }

    private Reservation map(ResultSet rs) throws SQLException {
        try {
            long idClient = rs.getLong("id_client");
            long idEvent = rs.getLong("id_evenement");

            Utilisateur u = utilisateurDAO.findById(idClient);

            Client client = null;
            if (u instanceof Client) {
                client = (Client) u;
            } else {
                throw new DaoException("L'utilisateur lié à la réservation n'est pas un client valide.");
            }

            Evenement evenement = evenementDAO.findById(idEvent);

            return new Reservation(
                    rs.getLong("id_reservation"),
                    rs.getTimestamp("datereservation").toLocalDateTime(),
                    rs.getInt("nbredeplace"),
                    rs.getBigDecimal("montanttotal"),
                    StatutReservation.valueOf(rs.getString("statutenum")),
                    client,
                    evenement
            );
        } catch (DaoException e) {
            throw new DaoException("Erreur lors du mapping de la réservation (Dépendances introuvables)", e);
        }
    }

    @Override
    public List<Reservation> findByClient(Long clientId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE id_client = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur historique client", e);
        }
        return list;
    }

    @Override
    public void update(Reservation r) {
        String sql = "UPDATE reservation SET statutenum = ? WHERE id_reservation = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getStatut().name());
            ps.setLong(2, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur update reservation", e);
        }
    }
}