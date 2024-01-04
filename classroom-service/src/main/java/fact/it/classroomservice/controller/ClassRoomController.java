package fact.it.classroomservice.controller;

import fact.it.classroomservice.dto.ClassRoomRequest;
import fact.it.classroomservice.dto.ClassRoomResponse;
import fact.it.classroomservice.dto.ClassRoomsResponse;
import fact.it.classroomservice.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ClassRoomResponse getRoom(@RequestParam String roomname){
        return classRoomService.getClassRoom(roomname);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassRoomsResponse> getAllRoomNames(){
        return classRoomService.getAllClassRooms();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public String createClassRoom(@RequestBody ClassRoomRequest classRoomRequest){
        return classRoomService.createClassRoom(classRoomRequest);
    }

    @DeleteMapping("/delete/")
    @ResponseStatus(HttpStatus.OK)
    public String deleteClassRoom(@RequestParam String roomname){
        return classRoomService.deleteClassRoom(roomname);
    }
}
