package job_tracker_api.exception;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException(Long id) {
        super("Candidature introuvable avec l'identifiant : " + id);
    }
}