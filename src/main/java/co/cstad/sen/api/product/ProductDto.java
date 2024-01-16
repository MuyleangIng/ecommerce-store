package co.cstad.sen.api.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate; // Added this import

@Builder
public record ProductDto(
        @NotNull(message = "Id cannot be null") Long id,
        @NotBlank(message = "Title cannot be blank") String title,
        @NotBlank(message = "Description cannot be blank") String description,
        Long createdBy,
        @NotBlank(message = "Cover cannot be blank") String cover,
        Boolean status,
        String uuid,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate, // Added this line
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate // Added this line
) {
}
