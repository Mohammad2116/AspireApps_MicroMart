package ir.aspireapps.identifyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String hashedToken;

    @Column(nullable = false, length = 256)
    private String deviceName;
    @Column(nullable = false)
    private UUID deviceId;

    @Column(nullable = false)
    private Instant expirationAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;
    @Column(nullable = true)
    private Instant revokedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private User user;

    public void revoke(){
        this.revoked = true;
        this.revokedAt = Instant.now();
    }
}
