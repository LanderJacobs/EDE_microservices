package fact.it.contentservice.controller;

import fact.it.contentservice.dto.ContentRequest;
import fact.it.contentservice.dto.ContentResponse;
import fact.it.contentservice.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ContentResponse getContentOfRoom(@RequestParam String roomname){
        return contentService.getContentOfRoom(roomname);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public String createContent(@RequestBody ContentRequest contentRequest) {
        return contentService.createContent(contentRequest);
    }

    @DeleteMapping("/delete/")
    @ResponseStatus(HttpStatus.OK)
    public String deleteContent(@RequestParam String roomname){
        return contentService.deleteContent(roomname);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateContent(@RequestBody ContentRequest contentRequest){
        return contentService.updateContent(contentRequest);
    }
}
