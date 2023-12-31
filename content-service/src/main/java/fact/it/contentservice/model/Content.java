package fact.it.contentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "content")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Content {
    private String id;
    private String roomName;
    private int outletAmount;
    private boolean beamer;
    private String functionality;
}
