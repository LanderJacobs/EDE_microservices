package fac.it.reservationservice.controller;

import fac.it.reservationservice.dto.ReservationRequest;
import fac.it.reservationservice.dto.ReservationResponse;
import fac.it.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getAll(){
        return reservationService.getAllReservations();
    }

    @GetMapping("/allbyroomname")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getAllByRoomName(@RequestParam String roomname){
        return reservationService.getAllReservationsByRoomName(roomname);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createReservation(@RequestBody ReservationRequest reservationRequest){
        reservationService.createReservation(reservationRequest);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservation(@RequestParam String id){
        Long reservationId = Long.parseLong(id);
        reservationService.deleteReservation(reservationId);
    }

    @DeleteMapping("/deleteall")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservations(@RequestParam String roomname){
        reservationService.deleteReservations(roomname);
    }
}
