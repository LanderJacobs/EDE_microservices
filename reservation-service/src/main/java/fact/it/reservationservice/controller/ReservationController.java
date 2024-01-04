package fact.it.reservationservice.controller;

import fact.it.reservationservice.dto.ReservationRequest;
import fact.it.reservationservice.dto.ReservationResponse;
import fact.it.reservationservice.service.ReservationService;
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

    @GetMapping("/allbyroomname/")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getAllByRoomName(@RequestParam String roomname){
        return reservationService.getAllReservationsByRoomName(roomname);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public String createReservation(@RequestBody ReservationRequest reservationRequest){
        return reservationService.createReservation(reservationRequest);
    }

    @DeleteMapping("/delete/")
    @ResponseStatus(HttpStatus.OK)
    public String deleteReservation(@RequestParam String id){
        Long reservationId = Long.parseLong(id);
        return reservationService.deleteReservation(reservationId);
    }

    @DeleteMapping("/deleteall/")
    @ResponseStatus(HttpStatus.OK)
    public String deleteReservations(@RequestParam String roomname){
        return reservationService.deleteReservations(roomname);
    }
}
