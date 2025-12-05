package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.dao.ReservationDAO;
import org.example.codingtickets.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReservationDAO implements ReservationDAO {

    @Override
    public Reservation findById(long id) {
        String sql = "SELECT * FROM reservation WHERE id_reservation = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            e.fillInStackTrace();
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

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return list;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Reservation save(Reservation r) {
        String sql = """
            INSERT INTO reservation(datereservation, nbredeplace, montanttotal,
                                    statutenum, id_client, id_evenement)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(r.getDateReservation()));
            ps.setInt(2, r.getNbPlaces());
            ps.setBigDecimal(3, r.getMontantTotal());
            ps.setString(4, r.getStatut().name());
            ps.setLong(5, r.getClient().getId());
            ps.setLong(6, r.getEvenement().getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return r;
    }



    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id_reservation = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Reservation map(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getLong("id_reservation"),
                rs.getTimestamp("datereservation").toLocalDateTime(),
                rs.getInt("nbredeplace"),
                rs.getBigDecimal("montanttotal"),
                Reservation.get,//rajouter pour le statut

                null, // ultra simple : pas de client chargé
                null  // ultra simple : pas d'évènement chargé
        );
    }
}
