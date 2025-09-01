package org.example.service;

import org.example.entity.Patient;
import org.example.mapper.PatientMapper;
import org.example.model.PatientRequestDTO;
import org.example.model.PatientResponseDTO;
import org.example.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toPatientDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        Patient patient = PatientMapper.toPatientEntity(requestDTO);
        return PatientMapper.toPatientDTO(patientRepository.save(patient));
    }
}
