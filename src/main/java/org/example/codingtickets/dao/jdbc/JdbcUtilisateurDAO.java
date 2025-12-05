package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.UtilisateurDAO;
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
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getLong("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("motdepasse"),
                        Role.valueOf(rs.getString("role"))

                );
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
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
                list.add(new Utilisateur(
                        rs.getLong("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("motdepasse"),
                        Role.valueOf(rs.getString("role"))

                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        String sql = """
                INSERT INTO utilisateur(nom, email, motdepasse, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getMotDePasse());
            ps.setString(4, utilisateur.getRole());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return utilisateur;
    }



    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
