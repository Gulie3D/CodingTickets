package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.OrganisateurDAO;
import org.example.codingtickets.model.Organisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcOrganisateurDAO implements OrganisateurDAO {

    @Override
    public Organisateur findById(Long id) {
        String sql = "SELECT * FROM organisateur WHERE id_organisateur = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Organisateur(id);
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return null;
    }

    @Override
    public List<Organisateur> findAll() {
        List<Organisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM organisateur";

        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Organisateur(rs.getLong("organisateur_id")));
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return list;
    }

    @Override
    public Organisateur save(Organisateur organisateur) {
        String sql = "INSERT INTO organisateur(id_organisateur) VALUES (?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, organisateur.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return organisateur;
    }


    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM organisateur WHERE id_organisateur = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
