package fac.it.reservationservice.repository;

import fac.it.reservationservice.model.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByRoomName(String roomName);
    void  deleteAllByRoomName(String roomName);
}
