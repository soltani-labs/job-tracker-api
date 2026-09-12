package job_tracker_api.domain;


import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public enum ApplicationStatus {

    APPLIED,
    PHONE_SCREEN,
    TECHNICAL,
    OFFER,
    REJECTED;

    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_TRANSITIONS = Map.ofEntries(
            entry(APPLIED, Set.of(PHONE_SCREEN, TECHNICAL, REJECTED)),
            entry(PHONE_SCREEN, Set.of(TECHNICAL, REJECTED)),
            entry(TECHNICAL, Set.of(OFFER, REJECTED)),
            entry(OFFER, Set.of()),
            entry(REJECTED, Set.of())
    );

    public boolean canTransitionTo(ApplicationStatus target) {
        return ALLOWED_TRANSITIONS.get(this).contains(target);
    }
}