package org.example.codingtickets.dao;

import org.example.codingtickets.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {

    Reservation findById(long id);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    void delete(Long id);

    List<Reservation> findByClient(Long clientId);

    void update(Reservation r);

}
