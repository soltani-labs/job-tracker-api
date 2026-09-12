package job_tracker_api.repository;

import job_tracker_api.domain.Application;
import job_tracker_api.domain.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStatus(ApplicationStatus status);

    List<Application> findByCompanyContainingIgnoreCase(String company);

    List<Application> findByStatusAndCompanyContainingIgnoreCase(ApplicationStatus status, String company);
}