package gk.jobapplications.dtos;

import gk.jobapplications.entities.JobEntity;
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
public class JobKPIDTO {
    private int totalJobs;
    private int activeJobs;
    private int inactiveJobs;
    private int totalCandidates;
    private JobEntity mostAppliedJob;
}
