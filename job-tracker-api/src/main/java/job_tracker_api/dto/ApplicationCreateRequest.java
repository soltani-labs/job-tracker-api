package job_tracker_api.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import job_tracker_api.domain.ApplicationStatus;

import java.time.LocalDate;

public record ApplicationCreateRequest(

        @NotBlank(message = "Le nom de l'entreprise est obligatoire")
        @Size(max = 100, message = "Le nom de l'entreprise ne doit pas dépasser 100 caractères")
        String company,

        @NotBlank(message = "Le nom du poste est obligatoire")
        @Size(max = 100, message = "Le nom du poste ne doit pas dépasser 100 caractères")
        String role,

        @Size(max = 2048, message = "Le lien de l'annonce ne doit pas dépasser 2048 caractères")
        String jobPostingUrl,

        ApplicationStatus status,

        @NotNull(message = "La date de candidature est obligatoire")
        @PastOrPresent(message = "La date de candidature doit être passée ou présente")
        LocalDate appliedDate,

        @Size(max = 2000, message = "Les notes ne doivent pas dépasser 2000 caractères")
        String notes
) {}