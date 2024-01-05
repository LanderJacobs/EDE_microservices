package fact.it.classroomservice;

import fact.it.classroomservice.dto.*;
import fact.it.classroomservice.model.ClassRoom;
import fact.it.classroomservice.repository.ClassRoomRepository;
import fact.it.classroomservice.service.ClassRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceUnitTests {

    @InjectMocks
    private ClassRoomService classRoomService;

    @Mock
    private ClassRoomRepository classRoomRepository;

    @Mock
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(classRoomService, "reservationServiceBaseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(classRoomService, "contentServiceBaseUrl", "http://localhost:8082");
    }

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    public void testGetClassRoom_Success() {
        // Arrange
        String roomName = "F001";
        ClassRoom classRoom = createClassRoom(roomName, "Building");

        ContentResponse contentResponse = ContentResponse.builder()
                .id("1")
                .roomName(roomName)
                .outletAmount(25)
                .beamer(true)
                .functionality("Functionality")
                .build();

        ReservationResponse[] reservationArray = {
                ReservationResponse.builder()
                        .id(25L)
                        .roomName(roomName)
                        .booker("Geert Janssens")
                        .dateReservation(LocalDate.now())
                        .startTime(LocalTime.now().minusHours(1))
                        .endTime(LocalTime.now().plusHours(1))
                        .build()
        };

        when(classRoomRepository.findFirstByRoomName(roomName)).thenReturn(Optional.of(classRoom));
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ContentResponse.class)).thenReturn(Mono.just(contentResponse));
        when(responseSpec.bodyToMono(ReservationResponse[].class)).thenReturn(Mono.just(reservationArray));

        // Act
        ClassRoomResponse result = classRoomService.getClassRoom(roomName);

        // Assert
        assertNotNull(result);
        assertEquals(roomName, result.getRoomName());
        assertEquals("Building", result.getBuilding());
        assertTrue(result.isItAccessible());
        assertFalse(result.isAvailable());

        verify(classRoomRepository, times(1)).findFirstByRoomName(roomName);
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri(anyString(), any(Function.class));
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(ContentResponse.class);
        verify(responseSpec, times(1)).bodyToMono(ReservationResponse[].class);
    }

    @Test
    public void testGetClassRoom_NotFound() {
        // Arrange
        String roomName = "NonExistingRoom";

        when(classRoomRepository.findFirstByRoomName(roomName)).thenReturn(Optional.empty());

        // Act
        ClassRoomResponse result = classRoomService.getClassRoom(roomName);

        // Assert
        assertNull(result);

        verify(classRoomRepository, times(1)).findFirstByRoomName(roomName);
        verify(webClient, times(0)).get();
        verify(requestHeadersUriSpec, times(0)).uri(anyString(), any(Function.class));
        verify(requestHeadersSpec, times(0)).retrieve();
        verify(responseSpec, times(0)).bodyToMono(ContentResponse[].class);
    }

    @Test
    public void testCreateClassRoom_Success() {
        // Arrange
        ClassRoomRequest classRoomRequest = new ClassRoomRequest("NewRoom", "NewBuilding");

        when(classRoomRepository.findFirstByRoomName(classRoomRequest.getRoomName())).thenReturn(Optional.empty());

        // Act
        String result = classRoomService.createClassRoom(classRoomRequest);

        // Assert
        assertEquals("Your classroom was added.", result);
        verify(classRoomRepository, times(1)).save(any(ClassRoom.class));
    }

    @Test
    public void testCreateClassRoom_AlreadyExists() {
        // Arrange
        ClassRoomRequest classRoomRequest = new ClassRoomRequest("F001", "Building");

        when(classRoomRepository.findFirstByRoomName(classRoomRequest.getRoomName())).thenReturn(Optional.of(createClassRoom(classRoomRequest.getRoomName(), "Building")));

        // Act
        String result = classRoomService.createClassRoom(classRoomRequest);

        // Assert
        assertEquals("This classroom already exists", result);
        verify(classRoomRepository, times(0)).save(any(ClassRoom.class));
    }

    @Test
    public void testDeleteClassRoom_Success() {
        // Arrange
        String roomName = "F001";
        ClassRoom classRoom = createClassRoom(roomName, "Building");

        when(classRoomRepository.findFirstByRoomName(roomName)).thenReturn(Optional.of(classRoom));

        // Act
        String result = classRoomService.deleteClassRoom(roomName);

        // Assert
        assertEquals("This classroom was deleted.", result);
        verify(classRoomRepository, times(1)).deleteById(classRoom.getId());
    }

    @Test
    public void testDeleteClassRoom_NotFound() {
        // Arrange
        String roomName = "NonExistingRoom";

        when(classRoomRepository.findFirstByRoomName(roomName)).thenReturn(Optional.empty());

        // Act
        String result = classRoomService.deleteClassRoom(roomName);

        // Assert
        assertEquals("Couldn't find this classroom.", result);
        verify(classRoomRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testGetAllClassRooms() {
        // Arrange
        ClassRoom classRoom1 = createClassRoom("F001", "Building1");
        ClassRoom classRoom2 = createClassRoom("F101", "Building2");

        when(classRoomRepository.findAll()).thenReturn(Arrays.asList(classRoom1, classRoom2));

        // Act
        List<ClassRoomsResponse> result = classRoomService.getAllClassRooms();

        // Assert
        assertEquals(2, result.size());

        verify(classRoomRepository, times(1)).findAll();
    }

    // Helper method to create class room
    private ClassRoom createClassRoom(String roomName, String building) {
        return new ClassRoom(1L, building, roomName);
    }
}
