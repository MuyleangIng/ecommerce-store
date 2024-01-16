package co.cstad.sen.api.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProductCreateDto(
        @NotBlank(message = "Title cannot be blank") String title,
        @NotBlank(message = "Description cannot be blank") String description,
        @NotBlank(message = "Cover cannot be blank") String cover,
        Boolean status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate
) {
}
