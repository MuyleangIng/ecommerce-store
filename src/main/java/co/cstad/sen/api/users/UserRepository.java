package co.cstad.sen.api.users;

import co.cstad.sen.api.auth.web.SocialEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndVerifiedCode(String email,UUID verifiedCode);

    Optional<User> findByEmailOrUsername (String email, String username);

    @Query("SELECT u FROM User u WHERE (u.email = :email OR u.username = :email) AND u.verified IS NOT NULL AND u.isDisabled = false")
    Optional<User> findByVerifiedNotNullAndIsDisabledFalseAndEmail(String email);

    @Query("SELECT u FROM User u WHERE (u.email = :email OR u.username = :email) AND u.verified IS NULL OR u.isDisabled = true ")
    Optional<User> findVerifiedNullOrIsDisabledTrueByEmail(String email);

    @Query("SELECT u FROM User u WHERE (u.email = :email OR u.username = :email) AND u.verified IS NOT NULL")
    Optional<User> findIsVerifiedNotNullByEmail(String email);

    @Query("SELECT u FROM User u WHERE (u.email = :email OR u.username = :username) AND u.provider = :provider")
    Optional<User> findByEmailUsernameProvider(String email, String username, SocialEnum provider);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByVerifiedCode(UUID verifiedCode);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = CURRENT_TIMESTAMP WHERE u.email = :email")
    void updateLastLoginByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.gitId = :gitId WHERE u.email = :email")
    void updateGitIdByEmail(Integer gitId, String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.verified = :verified, u.verifiedCode = NULL WHERE u.email = :email")
    void updateVerifiedByEmail(LocalDateTime verified, String email);

}
