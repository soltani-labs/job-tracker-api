package job_tracker_api.dto;



import job_tracker_api.domain.ApplicationStatus;
import job_tracker_api.domain.StatusHistory;

import java.time.LocalDateTime;

public record StatusHistoryResponse(
        ApplicationStatus status,
        LocalDateTime changedAt
) {
    public static StatusHistoryResponse from(StatusHistory history) {
        return new StatusHistoryResponse(history.getStatus(), history.getChangedAt());
    }
}