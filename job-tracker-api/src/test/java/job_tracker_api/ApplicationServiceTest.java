package job_tracker_api;



import job_tracker_api.domain.Application;
import job_tracker_api.domain.ApplicationStatus;
import job_tracker_api.dto.ApplicationCreateRequest;
import job_tracker_api.dto.ApplicationResponse;
import job_tracker_api.dto.StatusUpdateRequest;
import job_tracker_api.exception.ApplicationNotFoundException;
import job_tracker_api.exception.InvalidStatusTransitionException;
import job_tracker_api.repository.ApplicationRepository;
import job_tracker_api.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void create_withNoStatusProvided_defaultsToAppliedAndRecordsHistory() {
        ApplicationCreateRequest request = new ApplicationCreateRequest(
                "Acme Corp", "Backend Engineer", null, null, LocalDate.now(), null);

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response = applicationService.create(request);

        assertThat(response.status()).isEqualTo(ApplicationStatus.APPLIED);
        assertThat(response.statusHistory()).hasSize(1);
        assertThat(response.statusHistory().get(0).status()).isEqualTo(ApplicationStatus.APPLIED);

        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        assertThat(captor.getValue().getCompany()).isEqualTo("Acme Corp");
    }

    @Test
    void getById_whenApplicationDoesNotExist_throwsApplicationNotFoundException() {
        when(applicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getById(99L))
                .isInstanceOf(ApplicationNotFoundException.class);
    }

    @Test
    void changeStatus_withInvalidTransition_throwsAndDoesNotChangeState() {
        Application existing = new Application();
        existing.setId(1L);
        existing.setStatus(ApplicationStatus.OFFER); // terminal status, no transitions allowed
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existing));

        StatusUpdateRequest request = new StatusUpdateRequest(ApplicationStatus.REJECTED);

        assertThatThrownBy(() -> applicationService.changeStatus(1L, request))
                .isInstanceOf(InvalidStatusTransitionException.class);

        assertThat(existing.getStatus()).isEqualTo(ApplicationStatus.OFFER);
    }

    @Test
    void changeStatus_withValidTransition_updatesStatusAndAppendsHistory() {
        Application existing = new Application();
        existing.setId(1L);
        existing.setStatus(ApplicationStatus.APPLIED);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existing));

        StatusUpdateRequest request = new StatusUpdateRequest(ApplicationStatus.TECHNICAL);

        ApplicationResponse response = applicationService.changeStatus(1L, request);

        assertThat(response.status()).isEqualTo(ApplicationStatus.TECHNICAL);
        assertThat(existing.getStatusHistory()).hasSize(1);
        assertThat(existing.getStatusHistory().get(0).getStatus()).isEqualTo(ApplicationStatus.TECHNICAL);
    }
}
