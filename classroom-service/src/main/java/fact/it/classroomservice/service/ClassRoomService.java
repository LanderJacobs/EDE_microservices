package fact.it.classroomservice.service;

import fact.it.classroomservice.dto.*;
import fact.it.classroomservice.model.ClassRoom;
import fact.it.classroomservice.repository.ClassRoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final WebClient webClient;

    @Value("${contentservice.baseurl}")
    private String contentServiceBaseUrl;

    @Value("${reservationservice.baseurl}")
    private String reservationServiceBaseUrl;

    @PostConstruct
    public void LoadData(){
        if (classRoomRepository.count() <= 0){
            ClassRoom classRoom = new ClassRoom();
            classRoom.setRoomName("F001");
            classRoom.setBuilding("F-gebouw");

            ClassRoom classRoom1 = new ClassRoom();
            classRoom1.setRoomName("F101");
            classRoom1.setBuilding("F-gebouw");

            classRoomRepository.save(classRoom);
            classRoomRepository.save(classRoom1);
        }
    }

    public ClassRoomResponse getClassRoom(String roomName){
        try {
            Optional<ClassRoom> classRoomAnswer = classRoomRepository.findFirstByRoomName(roomName);
            ClassRoom classRoom;

            if (classRoomAnswer.isPresent()){
                classRoom = classRoomAnswer.get();
            } else {
                return null;
            }
            ClassRoomResponse classRoomResponse = new ClassRoomResponse();
            classRoomResponse.setRoomName(classRoom.getRoomName());
            classRoomResponse.setBuilding(classRoom.getBuilding());

            ContentResponse content = webClient.get()
                    .uri("http://" + contentServiceBaseUrl + "/api/content",
                            uriBuilder -> uriBuilder.queryParam("roomname", roomName).build())
                    .retrieve()
                    .bodyToMono(ContentResponse.class)
                    .block();

            classRoomResponse.setItAccessible(content.isBeamer() && content.getOutletAmount() >= 20);

            ReservationResponse[] reservationArray = webClient.get()
                    .uri("http://" + reservationServiceBaseUrl + "/api/reservation/allbyroomname",
                            uriBuilder -> uriBuilder.queryParam("roomname", roomName).build())
                    .retrieve()
                    .bodyToMono(ReservationResponse[].class)
                    .block();

            classRoomResponse.setAvailable(true);

            for (ReservationResponse x : reservationArray){
                if (x.getDateReservation().equals(LocalDate.now()) && x.getStartTime().isBefore(LocalTime.now()) && x.getEndTime().isAfter(LocalTime.now())){
                    classRoomResponse.setAvailable(false);
                }
            }

            return classRoomResponse;
        } catch (Exception e) {
            ClassRoomResponse test = new ClassRoomResponse();
            test.setRoomName(e.toString());
            return test;
        }
    }

    public List<ClassRoomsResponse> getAllClassRooms(){
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        return classRooms.stream()
                .map(this::mapToClassRoomsResponse).toList();
    }

    public void createClassRoom(ClassRoomRequest classRoomRequest){
        ClassRoom classRoom = new ClassRoom();
        classRoom.setRoomName(classRoomRequest.getRoomName());
        classRoom.setBuilding(classRoomRequest.getBuilding());
        classRoomRepository.save(classRoom);
    }

    public void deleteClassRoom(String roomName) {
        Optional<ClassRoom> classRoom = classRoomRepository.findFirstByRoomName(roomName);
        if (classRoom.isPresent()){
            classRoomRepository.deleteById(classRoom.get().getId());
        }
    }

    private ClassRoomsResponse mapToClassRoomsResponse(ClassRoom classRoom) {
        return ClassRoomsResponse.builder()
                .roomName(classRoom.getRoomName())
                .building(classRoom.getBuilding())
                .build();
    }
}
