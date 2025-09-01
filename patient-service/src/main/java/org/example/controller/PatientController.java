package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.example.model.PatientRequestDTO;
import org.example.model.PatientResponseDTO;
import org.example.model.validators.CreatePatientValidationGroup;
import org.example.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(summary = "Create new patient record")
    public ResponseEntity<PatientResponseDTO> createNewPatientRecord(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO requestDTO) {
        PatientResponseDTO response = patientService.createPatient(requestDTO);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient record")
    public ResponseEntity<PatientResponseDTO> updatePatientRecord(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO requestDTO) {
        PatientResponseDTO response = patientService.updatePatient(id, requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient record")
    public ResponseEntity<Void> deletePatientRecord(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
