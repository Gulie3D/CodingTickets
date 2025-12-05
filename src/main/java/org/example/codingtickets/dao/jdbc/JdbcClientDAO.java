package org.example.codingtickets.dao.jdbc;

import org.example.codingtickets.dao.ClientDAO;
import org.example.codingtickets.dao.ConnectionManager;
import org.example.codingtickets.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcClientDAO implements ClientDAO {

    @Override
    public Client findById(long id) {
        String sql = "SELECT * FROM client WHERE id_client = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Client(id);
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return null;
    }

    @Override
    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Client(rs.getLong("id_client")));
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return list;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO client(id_client) VALUES (?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, client.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return client;
    }


    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM client WHERE id_client = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
