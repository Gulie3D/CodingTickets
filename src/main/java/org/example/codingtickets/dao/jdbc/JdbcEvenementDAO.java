package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.EvenementDAO;
import org.example.codingtickets.dao.UtilisateurDAO;
import org.example.codingtickets.exception.DaoException;
import org.example.codingtickets.model.Evenement;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEvenementDAO implements EvenementDAO {
    private final UtilisateurDAO utilisateurDAO = new JdbcUtilisateurDAO();

    @Override
    public Evenement findById(long id) {
        String sql = "SELECT * FROM evenement WHERE id_evenement = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de l'événement ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Evenement> findAll() {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM evenement";

        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors du chargement de la liste des événements", e);
        }
        return list;
    }

    @Override
    public Optional<Evenement> findById(Long id) {
        return Optional.ofNullable(findById((long) id));
    }

    @Override
    public void save(Evenement e) {
        String sql = """
            INSERT INTO evenement(titre, description, dateevenement, lieu,
                                  nbreplacetotale, nbreplacesrestantes, prix_base, id_organisateur)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDateEvenement()));
            ps.setString(4, e.getLieu());
            ps.setInt(5, e.getNbPlacesTotales());
            ps.setInt(6, e.getNbPlacesRestantes());
            ps.setBigDecimal(7, e.getPrixBase());

            if (e.getOrganisateur() == null) {
                throw new DaoException("Impossible de créer l'événement : Organisateur manquant.");
            }
            ps.setLong(8, e.getOrganisateur().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("Échec de la création de l'événement, aucune ligne ajoutée.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    e.setId(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("Échec de la création de l'événement, aucun ID obtenu.");
                }
            }
        } catch (SQLException ex) {
            throw new DaoException("Erreur SQL lors de la sauvegarde de l'événement : " + e.getTitre(), ex);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
               throw new DaoException("Aucun événement trouvé avec l'ID: " + id);
            }

        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression de l'événement ID: " + id, e);
        }
    }

    private Evenement map(ResultSet rs) throws SQLException {
        try {
            long idOrga = rs.getLong("id_organisateur");

            Utilisateur user = utilisateurDAO.findById(idOrga);
            Organisateur organisateur;

            if (user instanceof Organisateur) {
                organisateur = (Organisateur) user;
            } else {
                throw new DaoException("L'utilisateur lié à l'événement n'est pas un organisateur valide.");
            }

            return new Evenement(
                    rs.getLong("id_evenement"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getTimestamp("dateevenement").toLocalDateTime(),
                    rs.getString("lieu"),
                    rs.getInt("nbreplacetotale"),
                    rs.getInt("nbreplacesrestantes"),
                    rs.getBigDecimal("prix_base"),
                    organisateur
            );
        } catch (DaoException e) {
            throw new DaoException("Erreur lors du mapping de l'événement (Problème UtilisateurDAO)", e);
        }
    }

    @Override
    public void update(Evenement e) {
        String sql = """
            UPDATE evenement SET 
                titre = ?,
                description = ?,
                dateevenement = ?,
                lieu = ?,
                nbreplacetotale = ?,
                nbreplacesrestantes = ?,
                prix_base = ?
            WHERE id_evenement = ?
            """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDateEvenement()));
            ps.setString(4, e.getLieu());
            ps.setInt(5, e.getNbPlacesTotales());
            ps.setInt(6, e.getNbPlacesRestantes());
            ps.setBigDecimal(7, e.getPrixBase());
            ps.setLong(8, e.getId());
            
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DaoException("Aucun événement trouvé avec l'ID: " + e.getId());
            }
        } catch (SQLException ex) {
            throw new DaoException("Erreur lors de la mise à jour de l'événement ID: " + e.getId(), ex);
        }
    }
}
