package fact.it.contentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
    private String roomName;
    private int outletAmount;
    private boolean beamer;
    private String functionality;
}
