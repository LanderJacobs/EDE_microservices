package fact.it.classroomservice.repository;

import fact.it.classroomservice.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Optional<ClassRoom> findFirstByRoomName(String roomName);
}
