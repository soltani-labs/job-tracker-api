package job_tracker_api.dto;


import job_tracker_api.domain.Application;
import job_tracker_api.domain.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ApplicationResponse(
        Long id,
        String company,
        String role,
        String jobPostingUrl,
        ApplicationStatus status,
        LocalDate appliedDate,
        LocalDateTime lastUpdated,
        String notes,
        List<StatusHistoryResponse> statusHistory
) {
    public static ApplicationResponse from(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getCompany(),
                application.getRole(),
                application.getJobPostingUrl(),
                application.getStatus(),
                application.getAppliedDate(),
                application.getLastUpdated(),
                application.getNotes(),
                application.getStatusHistory().stream()
                        .map(StatusHistoryResponse::from)
                        .toList()
        );
    }
}
