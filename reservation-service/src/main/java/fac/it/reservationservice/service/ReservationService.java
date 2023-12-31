package fac.it.reservationservice.service;

import fac.it.reservationservice.dto.ReservationRequest;
import fac.it.reservationservice.dto.ReservationResponse;
import fac.it.reservationservice.model.Reservation;
import fac.it.reservationservice.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @PostConstruct
    public void loadData(){
        if (reservationRepository.count() <= 0){
            Reservation reservation = new Reservation();
            reservation.setRoomName("F001");
            reservation.setBooker("Geert Janssens");
            reservation.setDateReservation(LocalDate.of(2023,11,24));
            reservation.setStartTime(LocalTime.of(12,30));
            reservation.setEndTime(LocalTime.of(15,45));

            Reservation reservation1 = new Reservation();
            reservation1.setRoomName("F101");
            reservation1.setBooker("Jan Geerts");
            reservation1.setDateReservation(LocalDate.of(2023,11,27));
            reservation1.setStartTime(LocalTime.of(8,30));
            reservation1.setEndTime(LocalTime.of(11,45));

            Reservation reservation2 = new Reservation();
            reservation2.setRoomName("F101");
            reservation2.setBooker("Jan Geerts");
            reservation2.setDateReservation(LocalDate.of(2023,12,30));
            reservation2.setStartTime(LocalTime.of(19,00));
            reservation2.setEndTime(LocalTime.of(23,00));

            reservationRepository.save(reservation);
            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
        }
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream().map(this::mapToReservationResponse).toList();
    }

    public List<ReservationResponse> getAllReservationsByRoomName(String roomName) {
        List<Reservation> reservations = reservationRepository.findAllByRoomName(roomName);
        return reservations.stream().map(this::mapToReservationResponse).toList();
    }

    public void createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();
        reservation.setRoomName(reservationRequest.getRoomName());
        reservation.setBooker(reservationRequest.getBooker());
        reservation.setDateReservation(reservationRequest.getDateReservation());
        reservation.setStartTime(reservationRequest.getStartTime());
        reservation.setEndTime(reservationRequest.getEndTime());

        reservationRepository.save(reservation);
    }

    public void deleteReservation(long id){
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()){
            reservationRepository.deleteById(id);
        }
    }

    public void deleteReservations(String roomName){
        List<Reservation> reservations = reservationRepository.findAllByRoomName(roomName);
        if (reservations != null){
            reservationRepository.deleteAllByRoomName(roomName);
        }
    }

    private ReservationResponse mapToReservationResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .roomName(reservation.getRoomName())
                .booker(reservation.getBooker())
                .dateReservation(reservation.getDateReservation())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }
}
