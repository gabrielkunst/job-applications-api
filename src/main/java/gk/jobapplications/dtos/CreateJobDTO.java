package gk.jobapplications.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 50, message = "O título deve ter no máximo 50 caracteres")
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String description;

    @NotNull(message = "A quantidade é obrigatória")
    private Integer quantity;

    @NotNull(message = "O ID da empresa é obrigatório")
    private UUID companyId;
}

