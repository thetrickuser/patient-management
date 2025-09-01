package org.example.controller;

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
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createNewPatientRecord(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO requestDTO) {
        PatientResponseDTO response = patientService.createPatient(requestDTO);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatientRecord(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO requestDTO) {
        PatientResponseDTO response = patientService.updatePatient(id, requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientRecord(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
