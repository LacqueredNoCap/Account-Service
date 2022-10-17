package account.entity;

import account.service.event.EventEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity(name = "events")
@Getter @Setter
public class Event {

    @Id
    @SequenceGenerator(
            name = "events_sequence",
            sequenceName = "events_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "events_sequence"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    @Column(name = "event")
    @Enumerated(value = EnumType.STRING)
    private EventEnum event;

    @Column(name = "user_name")
    private String user;

    @Column(name = "description")
    private String description;

    @Column(name = "path")
    private String path;

    public Event() {
    }

    public Event(
            OffsetDateTime timestamp,
            EventEnum event,
            String user,
            String description,
            String path) {
        this.timestamp = timestamp;
        this.event = event;
        this.user = user;
        this.description = description;
        this.path = path;
    }
}
