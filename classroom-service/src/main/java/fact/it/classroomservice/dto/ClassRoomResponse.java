package fact.it.classroomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoomResponse {
    private String roomName;
    private String building;
    private boolean isAvailable;
    private boolean itAccessible;
}
