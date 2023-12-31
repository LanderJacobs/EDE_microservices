package fact.it.contentservice.service;

import fact.it.contentservice.dto.ContentRequest;
import fact.it.contentservice.dto.ContentResponse;
import fact.it.contentservice.model.Content;
import fact.it.contentservice.repository.ContentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @PostConstruct
    public void LoadData(){
        if (contentRepository.count() <= 0){
            Content content = new Content();
            content.setRoomName("F001");
            content.setOutletAmount(15);
            content.setBeamer(true);
            content.setFunctionality("Lokaal op de begane grond, met beamer en meerdere stopcontacten");


            Content content1 = new Content();
            content1.setRoomName("F101");
            content1.setOutletAmount(50);
            content1.setBeamer(true);
            content1.setFunctionality("Practicalokaal op de eerste verdieping, zonder beamer en meerdere stopcontacten");

            contentRepository.save(content);
            contentRepository.save(content1);
        }
    }

    public ContentResponse getContentOfRoom(String roomName) {
        Content content = contentRepository.findFirstByRoomName(roomName);

        return mapToContentResponse(content);
    }

    public void createContent(ContentRequest contentRequest){
        Content content = Content.builder()
                .roomName(contentRequest.getRoomName())
                .outletAmount(contentRequest.getOutletAmount())
                .beamer(contentRequest.isBeamer())
                .functionality(contentRequest.getFunctionality())
                .build();

        contentRepository.save(content);
    }

    public void deleteContent(String roomName){
        Content content = contentRepository.findFirstByRoomName(roomName);
        if (content != null){
            contentRepository.deleteById(content.getId());
        }
    }

    public void updateContent(ContentRequest contentRequest){
        Content content = contentRepository.findFirstByRoomName(contentRequest.getRoomName());

        content.setBeamer(contentRequest.isBeamer());
        content.setFunctionality(contentRequest.getFunctionality());
        content.setOutletAmount(contentRequest.getOutletAmount());

        contentRepository.save(content);
    }

    private ContentResponse mapToContentResponse(Content content){
        return ContentResponse.builder()
                .id(content.getId())
                .roomName(content.getRoomName())
                .outletAmount(content.getOutletAmount())
                .beamer(content.isBeamer())
                .functionality(content.getFunctionality())
                .build();
    }
}
