package fact.it.contentservice.repository;

import fact.it.contentservice.model.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepository extends MongoRepository<Content, String> {
    Content findFirstByRoomName(String roomName);
}
