package job_tracker_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import job_tracker_api.dto.ApplicationCreateRequest;
import job_tracker_api.dto.StatusUpdateRequest;
import job_tracker_api.domain.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createThenGetById_returnsThePersistedApplication() throws Exception {
        ApplicationCreateRequest createRequest = new ApplicationCreateRequest(
                "Acme Corp", "Backend Engineer", "https://acme.example/jobs/42",
                null, LocalDate.now(), "Found via referral");

        String createResponseJson = mockMvc.perform(post("/api/applications")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.company").value("Acme Corp"))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.statusHistory", hasSize(1)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponseJson).get("id").asLong();

        mockMvc.perform(get("/api/applications/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Acme Corp"))
                .andExpect(jsonPath("$.role").value("Backend Engineer"));
    }

    @Test
    void create_withBlankCompany_returns400WithValidationError() throws Exception {
        ApplicationCreateRequest invalidRequest = new ApplicationCreateRequest(
                "", "Backend Engineer", null, null, LocalDate.now(), null);

        mockMvc.perform(post("/api/applications")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.company").exists());
    }

    @Test
    void changeStatus_withInvalidTransition_returns409Conflict() throws Exception {
        ApplicationCreateRequest createRequest = new ApplicationCreateRequest(
                "Globex", "QA Engineer", null, ApplicationStatus.OFFER, LocalDate.now(), null);

        String createResponseJson = mockMvc.perform(post("/api/applications")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponseJson).get("id").asLong();

        // OFFER is a terminal status — no transition should be allowed from it
        mockMvc.perform(patch("/api/applications/{id}/status", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new StatusUpdateRequest(ApplicationStatus.REJECTED))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getById_whenApplicationDoesNotExist_returns404() throws Exception {
        mockMvc.perform(get("/api/applications/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }
}
