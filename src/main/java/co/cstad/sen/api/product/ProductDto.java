package co.cstad.sen.api.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate; // Added this import
import java.util.UUID;

@Builder
public record ProductDto(

        @NotBlank(message = "Title cannot be blank") String title,
        @NotBlank(message = "Description cannot be blank") String description,
        Long createdBy,
        @NotBlank(message = "Cover cannot be blank") String cover,
        Boolean status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate, // Added this line
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate, // Added this line
        UUID uuid // Added this line

) {
}
