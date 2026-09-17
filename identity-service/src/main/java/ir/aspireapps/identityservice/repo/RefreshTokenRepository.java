package ir.aspireapps.identityservice.repo;

import ir.aspireapps.identityservice.model.RefreshToken;
import ir.aspireapps.identityservice.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByHashTokenAndNotRevoked(@Valid String hashToken);

    List<RefreshToken> findByUserAndNotRevoked(User user);

}
