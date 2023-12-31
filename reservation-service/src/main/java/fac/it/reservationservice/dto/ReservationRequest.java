package fac.it.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {
    private String roomName;
    private String booker;
    private LocalDate dateReservation;
    private LocalTime startTime;
    private LocalTime endTime;
}
