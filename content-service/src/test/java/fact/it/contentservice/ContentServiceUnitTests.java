package fact.it.contentservice;

import fact.it.contentservice.dto.ContentRequest;
import fact.it.contentservice.dto.ContentResponse;
import fact.it.contentservice.model.Content;
import fact.it.contentservice.repository.ContentRepository;
import fact.it.contentservice.service.ContentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContentServiceUnitTests {

    @InjectMocks
    private ContentService contentService;

    @Mock
    private ContentRepository contentRepository;

    @Test
    public void testGetContentOfRoom() {
        // Arrange
        String roomName = "F001";
        Content content = createContent(roomName, 15, true, "Lokaal op de begane grond, met beamer en meerdere stopcontacten");

        when(contentRepository.findFirstByRoomName(roomName)).thenReturn(content);

        // Act
        ContentResponse result = contentService.getContentOfRoom(roomName);

        // Assert
        assertNotNull(result);
        assertEquals(roomName, result.getRoomName());
    }

    @Test
    public void testCreateContent_Success() {
        // Arrange
        ContentRequest contentRequest = new ContentRequest("F002", 20, false, "Another room");

        when(contentRepository.findFirstByRoomName(contentRequest.getRoomName())).thenReturn(null);

        // Act
        String result = contentService.createContent(contentRequest);

        // Assert
        assertEquals("Your content was added.", result);
        verify(contentRepository, times(1)).save(any(Content.class));
    }

    @Test
    public void testCreateContent_AlreadyExists() {
        // Arrange
        ContentRequest contentRequest = new ContentRequest("F001", 20, false, "Another room");

        when(contentRepository.findFirstByRoomName(contentRequest.getRoomName())).thenReturn(createContent(contentRequest.getRoomName(), 15, true, "Existing room"));

        // Act
        String result = contentService.createContent(contentRequest);

        // Assert
        assertEquals("The content for this room already exists", result);
        verify(contentRepository, times(0)).save(any(Content.class));
    }

    @Test
    public void testDeleteContent_Success() {
        // Arrange
        String roomName = "F001";
        Content content = createContent(roomName, 15, true, "Existing room");

        when(contentRepository.findFirstByRoomName(roomName)).thenReturn(content);

        // Act
        String result = contentService.deleteContent(roomName);

        // Assert
        assertEquals("The content was deleted.", result);
        verify(contentRepository, times(1)).deleteById(content.getId());
    }

    @Test
    public void testDeleteContent_NotFound() {
        // Arrange
        String roomName = "NonExistingRoom";

        when(contentRepository.findFirstByRoomName(roomName)).thenReturn(null);

        // Act
        String result = contentService.deleteContent(roomName);

        // Assert
        assertEquals("Couldn't find this content.", result);
        verify(contentRepository, times(0)).deleteById(any());
    }

    @Test
    public void testUpdateContent_Success() {
        // Arrange
        ContentRequest contentRequest = new ContentRequest("F001", 20, false, "Updated room");
        Content existingContent = createContent(contentRequest.getRoomName(), 15, true, "Existing room");

        when(contentRepository.findFirstByRoomName(contentRequest.getRoomName())).thenReturn(existingContent);

        // Act
        String result = contentService.updateContent(contentRequest);

        // Assert
        assertEquals("Updated your content", result);
        verify(contentRepository, times(1)).save(existingContent);
        assertEquals(contentRequest.getFunctionality(), existingContent.getFunctionality());
        assertEquals(contentRequest.getOutletAmount(), existingContent.getOutletAmount());
        assertEquals(contentRequest.isBeamer(), existingContent.isBeamer());
    }

    @Test
    public void testUpdateContent_NotFound() {
        // Arrange
        ContentRequest contentRequest = new ContentRequest("NonExistingRoom", 20, false, "Updated room");

        when(contentRepository.findFirstByRoomName(contentRequest.getRoomName())).thenReturn(null);

        // Act
        String result = contentService.updateContent(contentRequest);

        // Assert
        assertEquals("Couldn't find the original content to update.", result);
        verify(contentRepository, times(0)).save(any(Content.class));
    }

    // Helper method to create content
    private Content createContent(String roomName, int outletAmount, boolean beamer, String functionality) {
        return Content.builder()
                .roomName(roomName)
                .outletAmount(outletAmount)
                .beamer(beamer)
                .functionality(functionality)
                .build();
    }
}
