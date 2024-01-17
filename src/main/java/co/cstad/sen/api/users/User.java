package co.cstad.sen.api.users;

import co.cstad.sen.api.auth.web.SocialEnum;
import co.cstad.sen.api.role.Role;
import co.cstad.sen.base.GenderEnum;
import co.cstad.sen.utils.EncryptionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id ;

    @Transient
    private String uuid;


    @Column(name = "avatar")
    private String avatar;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender", columnDefinition = "varchar default 'MALE'", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender=GenderEnum.MALE;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username",unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true,nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "verified")
    private LocalDateTime verified;

    @Column(name = "is_disabled" , columnDefinition = "boolean default false",nullable = false)
    private Boolean isDisabled;

    @Column(name = "deleted_at")
    @JsonIgnore
    private Timestamp deletedAt;

    @CreatedDate
    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "verified_code", unique = true)
    private UUID verifiedCode;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialEnum provider= SocialEnum.CREDENTIALS;



    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @Transient
    private List<String> currentRoles;

    public List<String> getCurrentRoles() {
        if (this.roles.isEmpty()) return Collections.emptyList();
        return Collections.singletonList(String.join(",", roles.stream().map(Role::getName).toList()));
    }

    public User(Integer gitId, String firstName, String lastName, String username , String email, String password, LocalDateTime verified){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isDisabled = false;
        this.verified = verified;
    }

    public User(String username, String email, String password, UUID verifiedCode){
        this.username = username;
        this.email = email;
        this.password = password;
        this.verifiedCode = verifiedCode;
        this.isDisabled = false;
    }
    public User(String firstName, String lastName, String username , String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isDisabled = false;
    }

    public String getUsernameFromEmail(){
        return email.split("@")[0];
    }

    public String getUuid() {
        return EncryptionUtil.encrypt(id.toString(), this.getClass().getSimpleName().toLowerCase());
    }

    public static String getClassName(){
        return User.class.getSimpleName().toLowerCase();
    }

    public boolean isVerified(){
        return verified != null;
    }
}
