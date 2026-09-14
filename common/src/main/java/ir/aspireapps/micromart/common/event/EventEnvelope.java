package ir.aspireapps.micromart.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope<T> {
    private String eventId;
    private String eventType;
    private String aggregateId;
    private Instant occurredAt;
    private T payload;

    public static <T> EventEnvelope<T> of(String eventType, String aggregateId, T payload) {
        return EventEnvelope.<T>builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .aggregateId(aggregateId)
                .occurredAt(Instant.now())
                .payload(payload)
                .build();
    }
}
