package fact.it.classroomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponse {
    private String  id;
    private String roomName;
    private int outletAmount;
    private boolean beamer;
    private String functionality;
}
