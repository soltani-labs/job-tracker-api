package job_tracker_api.controller;

import jakarta.validation.Valid;
import job_tracker_api.domain.ApplicationStatus;
import job_tracker_api.dto.ApplicationCreateRequest;
import job_tracker_api.dto.ApplicationResponse;
import job_tracker_api.dto.ApplicationUpdateRequest;
import job_tracker_api.dto.StatusUpdateRequest;
import job_tracker_api.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@Valid @RequestBody ApplicationCreateRequest request) {
        ApplicationResponse response = applicationService.create(request);
        return ResponseEntity
                .created(URI.create("/api/applications/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String company) {
        return ResponseEntity.ok(applicationService.getAll(status, company));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateRequest request) {
        return ResponseEntity.ok(applicationService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.changeStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}