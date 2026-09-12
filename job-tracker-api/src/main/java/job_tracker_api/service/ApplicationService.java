package job_tracker_api.service;


import job_tracker_api.domain.Application;
import job_tracker_api.domain.ApplicationStatus;
import job_tracker_api.domain.StatusHistory;
import job_tracker_api.dto.ApplicationCreateRequest;
import job_tracker_api.dto.ApplicationResponse;
import job_tracker_api.dto.ApplicationUpdateRequest;
import job_tracker_api.dto.StatusUpdateRequest;
import job_tracker_api.exception.ApplicationNotFoundException;
import job_tracker_api.exception.InvalidStatusTransitionException;
import job_tracker_api.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public ApplicationResponse create(ApplicationCreateRequest request) {
        Application application = new Application();
        application.setCompany(request.company());
        application.setRole(request.role());
        application.setJobPostingUrl(request.jobPostingUrl());
        application.setAppliedDate(request.appliedDate());
        application.setNotes(request.notes());
        application.setStatus(request.status() != null ? request.status() : ApplicationStatus.APPLIED);


        application.addStatusHistory(new StatusHistory(application.getStatus()));

        return ApplicationResponse.from(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAll(ApplicationStatus status, String company) {
        List<Application> applications;
        if (status != null && company != null) {
            applications = applicationRepository.findByStatusAndCompanyContainingIgnoreCase(status, company);
        } else if (status != null) {
            applications = applicationRepository.findByStatus(status);
        } else if (company != null) {
            applications = applicationRepository.findByCompanyContainingIgnoreCase(company);
        } else {
            applications = applicationRepository.findAll();
        }
        return applications.stream().map(ApplicationResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getById(Long id) {
        return ApplicationResponse.from(findById(id));
    }

    @Transactional
    public ApplicationResponse update(Long id, ApplicationUpdateRequest request) {
        Application application = findById(id);
        application.setCompany(request.company());
        application.setRole(request.role());
        application.setJobPostingUrl(request.jobPostingUrl());
        application.setAppliedDate(request.appliedDate());
        application.setNotes(request.notes());

        if (request.status() != application.getStatus()) {
            changeStatusInternal(application, request.status());
        }
        return ApplicationResponse.from(application);
    }

    @Transactional
    public ApplicationResponse changeStatus(Long id, StatusUpdateRequest request) {
        Application application = findById(id);
        changeStatusInternal(application, request.status());
        return ApplicationResponse.from(application);
    }

    @Transactional
    public void delete(Long id) {
        applicationRepository.delete(findById(id));
    }

    private Application findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    private void changeStatusInternal(Application application, ApplicationStatus target) {
        if (!application.getStatus().canTransitionTo(target)) {
            throw new InvalidStatusTransitionException(application.getStatus(), target);
        }
        application.setStatus(target);
        application.addStatusHistory(new StatusHistory(target));
    }
}