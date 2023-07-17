package account.entity;

import javax.persistence.*;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

import account.service.event.EventEnum;

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
    @Column
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    @Column
    @Enumerated(value = EnumType.STRING)
    private EventEnum event;

    @Column
    private String subject;

    @Column
    private String object;

    @Column
    private String path;

    public Event() {
    }

    public Event(
            OffsetDateTime timestamp,
            EventEnum event,
            String subject,
            String object,
            String path) {
        this.timestamp = timestamp;
        this.event = event;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}
