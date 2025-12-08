package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.UtilisateurDAO;
import org.example.codingtickets.exception.DaoException;
import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Organisateur;
import org.example.codingtickets.model.Role;
import org.example.codingtickets.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUtilisateurDAO implements UtilisateurDAO {

    @Override
    public Utilisateur findById(long id) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de l'utilisateur ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de la liste des utilisateurs", e);
        }
        return list;
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        try {
            return Optional.ofNullable(findById((long) id));
        } catch (DaoException e) {
            throw e;
        }
    }

    @Override
    public void save(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur(nom, email, motdepasse, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getMotDePasse());

            if (utilisateur.getRole() == null) {
                throw new DaoException("Impossible de sauvegarder : Le rôle de l'utilisateur est manquant.");
            }
            ps.setString(4, utilisateur.getRole().name());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DaoException("Échec de la création de l'utilisateur, aucune ligne ajoutée.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utilisateur.setId(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("Échec de la création de l'utilisateur, aucun ID obtenu.");
                }
            }

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw new DaoException("Cet email est déjà utilisé : " + utilisateur.getEmail(), e);
            }
            throw new DaoException("Erreur lors de la sauvegarde de l'utilisateur " + utilisateur.getNom(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
               throw new DaoException("Aucun utilisateur trouvé avec l'ID: " + id);
            }

        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression de l'utilisateur ID: " + id, e);
        }
    }

    private Utilisateur map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_utilisateur");
        String nom = rs.getString("nom");
        String email = rs.getString("email");
        String mdp = rs.getString("motdepasse");
        String roleStr = rs.getString("role");

        Role role;
        try {
            if (roleStr == null) {
                throw new DaoException("Incohérence base de données : Rôle null pour l'utilisateur ID " + id);
            }
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new DaoException("Rôle inconnu en base de données : " + roleStr, e);
        }

        if (role == Role.ORGANISATEUR) {
            return new Organisateur(id, nom, email, mdp);
        } else {
            return new Client(id, nom, email, mdp);
        }
    }

    @Override
    public Utilisateur findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND motdepasse = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur auth", e);
        }
        return null;
    }
}