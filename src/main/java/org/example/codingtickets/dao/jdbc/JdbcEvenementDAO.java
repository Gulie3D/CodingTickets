package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.EvenementDAO;
import org.example.codingtickets.model.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEvenementDAO implements EvenementDAO {

    @Override
    public Evenement findById(long id) {
        String sql = "SELECT * FROM evenement WHERE id_evenement = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
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

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return list;
    }

    @Override
    public Optional<Evenement> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Evenement save(Evenement e) {
        String sql = """
            INSERT INTO evenement(titre, description, dateevenement, lieu,
                                  nbreplacetotale, nbreplacesrestantes, prix_base, id_organisateur)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
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
            ps.setLong(8, e.getOrganisateur().getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return e;
    }


    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    private Evenement map(ResultSet rs) throws SQLException {
        return new Evenement(
                rs.getLong("id_evenement"),
                rs.getString("titre"),
                rs.getString("description"),
                rs.getTimestamp("dateevenement").toLocalDateTime(),
                rs.getString("lieu"),
                rs.getInt("nbreplacetotale"),
                rs.getInt("nbreplacesrestantes"),
                rs.getBigDecimal("prix_base"),
                null // ultra simple : pas d’organisateur chargé
        );
    }
}
