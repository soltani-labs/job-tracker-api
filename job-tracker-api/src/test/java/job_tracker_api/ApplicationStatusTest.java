package job_tracker_api.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class ApplicationStatusTest {

    @Test
    void appliedCanTransitionToPhoneScreenTechnicalOrRejected() {
        assertThat(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.PHONE_SCREEN)).isTrue();
        assertThat(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.TECHNICAL)).isTrue();
        assertThat(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.REJECTED)).isTrue();
    }

    @Test
    void appliedCannotTransitionDirectlyToOffer() {
        assertThat(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.OFFER)).isFalse();
    }

    @Test
    void technicalCanTransitionToOfferOrRejected() {
        assertThat(ApplicationStatus.TECHNICAL.canTransitionTo(ApplicationStatus.OFFER)).isTrue();
        assertThat(ApplicationStatus.TECHNICAL.canTransitionTo(ApplicationStatus.REJECTED)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = ApplicationStatus.class, names = {"OFFER", "REJECTED"})
    void terminalStatusesCannotTransitionAnywhere(ApplicationStatus terminalStatus) {
        for (ApplicationStatus target : ApplicationStatus.values()) {
            assertThat(terminalStatus.canTransitionTo(target)).isFalse();
        }
    }
}
