package fact.it.reservationservice;

import fact.it.reservationservice.dto.ReservationRequest;
import fact.it.reservationservice.dto.ReservationResponse;
import fact.it.reservationservice.model.Reservation;
import fact.it.reservationservice.repository.ReservationRepository;
import fact.it.reservationservice.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceUnitTests {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    public void testGetAllReservations() {
        // Arrange
        Reservation reservation1 = createReservation("F001", "Geert Janssens", LocalDate.of(2023, 11, 24), LocalTime.of(12, 30), LocalTime.of(15, 45));
        Reservation reservation2 = createReservation("F101", "Jan Geerts", LocalDate.of(2023, 11, 27), LocalTime.of(8, 30), LocalTime.of(11, 45));

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // Act
        List<ReservationResponse> result = reservationService.getAllReservations();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllReservationsByRoomName() {
        // Arrange
        String roomName = "F101";
        Reservation reservation1 = createReservation(roomName, "Jan Geerts", LocalDate.of(2023, 11, 27), LocalTime.of(8, 30), LocalTime.of(11, 45));

        when(reservationRepository.findAllByRoomName(roomName)).thenReturn(Arrays.asList(reservation1));

        // Act
        List<ReservationResponse> result = reservationService.getAllReservationsByRoomName(roomName);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    public void testCreateReservation_Success() {
        // Arrange
        ReservationRequest reservationRequest = new ReservationRequest("F002", "John Doe", LocalDate.of(2023, 12, 01), LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(reservationRepository.findAllByRoomName(reservationRequest.getRoomName())).thenReturn(Arrays.asList());

        // Act
        String result = reservationService.createReservation(reservationRequest);

        // Assert
        assertEquals("You have reserved this room", result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_ConflictingTiming() {
        // Arrange
        ReservationRequest reservationRequest = new ReservationRequest("F101", "Jane Doe", LocalDate.of(2023, 11, 27), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation existingReservation = createReservation("F101", "Jan Geerts", LocalDate.of(2023, 11, 27), LocalTime.of(8, 30), LocalTime.of(11, 45));

        when(reservationRepository.findAllByRoomName(reservationRequest.getRoomName())).thenReturn(Arrays.asList(existingReservation));

        // Act
        String result = reservationService.createReservation(reservationRequest);

        // Assert
        assertEquals("Your timing is conflicting with another reservation.", result);
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    public void testDeleteReservation_Success() {
        // Arrange
        long reservationId = 1L;
        Optional<Reservation> existingReservation = Optional.of(createReservation("F001", "Geert Janssens", LocalDate.of(2023, 11, 24), LocalTime.of(12, 30), LocalTime.of(15, 45)));

        when(reservationRepository.findById(reservationId)).thenReturn(existingReservation);

        // Act
        String result = reservationService.deleteReservation(reservationId);

        // Assert
        assertEquals("You have deleted this reservation.", result);
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    public void testDeleteReservation_NotFound() {
        // Arrange
        long reservationId = 2L;
        Optional<Reservation> existingReservation = Optional.empty();

        when(reservationRepository.findById(reservationId)).thenReturn(existingReservation);

        // Act
        String result = reservationService.deleteReservation(reservationId);

        // Assert
        assertEquals("Could not find this reservation", result);
        verify(reservationRepository, times(0)).deleteById(reservationId);
    }

    @Test
    public void testDeleteReservations_Success() {
        // Arrange
        String roomName = "F101";
        List<Reservation> existingReservations = Arrays.asList(
                createReservation(roomName, "Jan Geerts", LocalDate.of(2023, 11, 27), LocalTime.of(8, 30), LocalTime.of(11, 45)),
                createReservation(roomName, "Jan Geerts", LocalDate.of(2023, 12, 30), LocalTime.of(19, 0), LocalTime.of(23, 0))
        );

        when(reservationRepository.findAllByRoomName(roomName)).thenReturn(existingReservations);

        // Act
        String result = reservationService.deleteReservations(roomName);

        // Assert
        assertEquals("You have deleted these reservations", result);
        verify(reservationRepository, times(1)).deleteAllByRoomName(roomName);
    }

    @Test
    public void testDeleteReservations_NotFound() {
        // Arrange
        String roomName = "F102";

        when(reservationRepository.findAllByRoomName(roomName)).thenReturn(null);

        // Act
        String result = reservationService.deleteReservations(roomName);

        // Assert
        assertEquals("Could not find these reservations", result);
        verify(reservationRepository, times(0)).deleteAllByRoomName(roomName);
    }

    // Helper method to create a reservation
    private Reservation createReservation(String roomName, String booker, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Reservation reservation = new Reservation();
        reservation.setRoomName(roomName);
        reservation.setBooker(booker);
        reservation.setDateReservation(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        return reservation;
    }
}
