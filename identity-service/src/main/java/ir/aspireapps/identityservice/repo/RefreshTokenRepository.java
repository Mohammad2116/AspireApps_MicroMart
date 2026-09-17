package ir.aspireapps.identityservice.repo;

import ir.aspireapps.identityservice.model.RefreshToken;
import ir.aspireapps.identityservice.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByHashTokenAndRevokedFalse(@Valid String hashToken);

    List<RefreshToken> findByUserAndRevokedFalse(User user);

    Optional<RefreshToken> findByUserAndDeviceIdAndRevokedFalse(User user,
                                              @NotNull(message = "Device Id is required as a valued UUID number")
                                              UUID deviceId);
}
