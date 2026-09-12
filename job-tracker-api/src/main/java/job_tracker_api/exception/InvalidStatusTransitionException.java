package job_tracker_api.exception;


import job_tracker_api.domain.ApplicationStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(ApplicationStatus from, ApplicationStatus to) {
        super("Transition de statut invalide : " + from + " vers " + to);
    }
}