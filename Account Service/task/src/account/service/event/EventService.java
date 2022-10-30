package account.service.event;

import account.entity.Event;
import account.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void makeEvent(EventEnum action,
                          String subject,
                          String object,
                          String path) {
        Event event = new Event
                (OffsetDateTime.now(),
                action,
                subject,
                object,
                path
        );

        eventRepository.save(event);
    }
}
