package job_tracker_api.dto;


import jakarta.validation.constraints.NotNull;
import job_tracker_api.domain.ApplicationStatus;

public record StatusUpdateRequest(

        @NotNull(message = "Le statut est obligatoire")
        ApplicationStatus status
) {}
