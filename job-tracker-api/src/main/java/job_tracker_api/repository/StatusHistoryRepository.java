package job_tracker_api.repository;


import job_tracker_api.domain.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

    List<StatusHistory> findByApplicationIdOrderByChangedAtAsc(Long applicationId);
}
