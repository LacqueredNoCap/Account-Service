package account.dto.response.info;

import account.entity.Event;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EventResponse {

    private final long id;

    private final LocalDate date;

    private final String action;

    private final String subject;

    private final String object;

    private final String path;

    public EventResponse(
            long id,
            LocalDate date,
            String action,
            String subject,
            String object,
            String path) {
        this.id = id;
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public static EventResponse fromEvent(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTimestamp().toLocalDate(),
                event.getEvent().name(),
                event.getUser(),
                event.getDescription(),
                event.getPath()
        );
    }
}
