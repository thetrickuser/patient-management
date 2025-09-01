package org.example.service;

import org.example.entity.Patient;
import org.example.exception.EmailAlreadyExistsException;
import org.example.exception.PatientNotFoundException;
import org.example.mapper.PatientMapper;
import org.example.model.PatientRequestDTO;
import org.example.model.PatientResponseDTO;
import org.example.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        if (patientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + requestDTO.getEmail() + " already exists");
        }

        Patient patient = PatientMapper.toPatientEntity(requestDTO);
        return PatientMapper.toPatientResponseDTO(patientRepository.save(patient));
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        if (patientRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email " + requestDTO.getEmail() + " already exists");
        }

        patient.setName(requestDTO.getName());
        patient.setAddress(requestDTO.getAddress());
        patient.setEmail(requestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));

        return PatientMapper.toPatientResponseDTO(patientRepository.save(patient));
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
