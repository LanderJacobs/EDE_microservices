package fact.it.reservationservice.service;

import fact.it.reservationservice.dto.ReservationRequest;
import fact.it.reservationservice.dto.ReservationResponse;
import fact.it.reservationservice.model.Reservation;
import fact.it.reservationservice.repository.ReservationRepository;
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

    public String createReservation(ReservationRequest reservationRequest) {
        try {
            Reservation reservation = new Reservation();
            reservation.setRoomName(reservationRequest.getRoomName());
            reservation.setBooker(reservationRequest.getBooker());
            reservation.setDateReservation(reservationRequest.getDateReservation());
            reservation.setStartTime(reservationRequest.getStartTime());
            reservation.setEndTime(reservationRequest.getEndTime());

            List<Reservation> reservations = reservationRepository.findAllByRoomName(reservationRequest.getRoomName());

            for (Reservation x : reservations){
                if (x.getDateReservation().equals(reservation.getDateReservation())){
                    if (x.getStartTime().isBefore(reservation.getStartTime()) && x.getEndTime().isAfter(reservation.getEndTime())){
                        return "The timing of your reservation is during another reservation.";
                    }
                    if (x.getStartTime().isAfter(reservation.getStartTime()) && x.getEndTime().isBefore(reservation.getEndTime())){
                        return "There is another reservation during your reservation.";
                    }
                    if (x.getStartTime().isAfter(reservation.getStartTime()) && x.getStartTime().isBefore(reservation.getEndTime())){
                        return "Your timing is conflicting with another reservation.";
                    }
                    if (x.getEndTime().isAfter(reservation.getStartTime()) && x.getEndTime().isBefore(reservation.getEndTime())){
                        return "Your timing is conflicting with another reservation.";
                    }
                    if (x.getStartTime().equals(reservation.getStartTime()) && x.getEndTime().equals(reservation.getEndTime())){
                        return "There already exists a reservation for this classroom for these exact hours.";
                    }
                }
            }

            reservationRepository.save(reservation);
            return "You have reserved this room";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteReservation(long id){
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (reservation.isPresent()){
                reservationRepository.deleteById(id);
                return "You have deleted this reservation.";
            } else {
                return "Could not find this reservation";
            }
        } catch (Exception e){
            return e.getMessage();
        }
    }

    public String deleteReservations(String roomName){
        try {
            List<Reservation> reservations = reservationRepository.findAllByRoomName(roomName);
            if (reservations != null){
                reservationRepository.deleteAllByRoomName(roomName);
                return "You have deleted these reservations";
            } else {
                return "Could not find these reservations";
            }
        } catch (Exception e) {
            return e.getMessage();
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
