package ir.aspireapps.identityservice.repo;

import ir.aspireapps.identityservice.model.User;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsernameOrEmail(
            @NotEmpty(message = "Username can't be empty")
            @Size(min = 6, max = 255, message = "Username must be 6-255 characters")
            @Pattern(
                regexp = "^[a-zA-Z_]+$",
                message = "Username can only contain English letters and underscores")
            String username,

            @NotBlank(message = "Email is required")
            @Size(max = 254, message = "Email must not exceed 254 characters")
            @Email(message = "Please provide a valid email address")
            String email);

    Optional<User> findByUsername(
            @NotEmpty(message = "Username can't be empty")
            @Size(min = 6, max = 255, message = "Username must be 6-255 characters")
            @Pattern(
                regexp = "^[a-zA-Z_]+$",
                message = "Username can only contain English letters and underscores")
            String username);
}
