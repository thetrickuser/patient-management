package org.example.controller;

import jakarta.validation.Valid;
import org.example.entity.Patient;
import org.example.mapper.PatientMapper;
import org.example.model.PatientRequestDTO;
import org.example.model.PatientResponseDTO;
import org.example.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<PatientResponseDTO> savePatient(@Valid @RequestBody PatientRequestDTO requestDTO) {
        PatientResponseDTO response = patientService.createPatient(requestDTO);
        return ResponseEntity.ok().body(response);
    }
}
