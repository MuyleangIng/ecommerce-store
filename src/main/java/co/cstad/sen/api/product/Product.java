package co.cstad.sen.api.product;

import co.cstad.sen.base.AuditEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonToken;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "start_date", columnDefinition = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JoinColumn(name = "created_by", referencedColumnName = "id", table = "users", nullable = true)
    private Long createdBy;

    private String cover;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean status = true;
    public static JsonToken builder() {
        return null;
    }
}