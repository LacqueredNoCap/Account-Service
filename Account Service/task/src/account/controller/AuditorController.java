package account.controller;

import account.dto.response.info.EventResponse;
import account.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuditorController {

    private final EventService eventService;

    public AuditorController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("api/security/events")
    public List<EventResponse> getSecurityEvents() {
        return eventService.getAllEvents().stream()
                .map(EventResponse::fromEvent)
                .collect(Collectors.toList());
    }
}
