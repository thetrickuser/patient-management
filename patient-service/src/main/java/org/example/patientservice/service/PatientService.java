package org.example.patientservice.service;

import org.example.patientservice.entity.Patient;
import org.example.patientservice.exception.EmailAlreadyExistsException;
import org.example.patientservice.exception.PatientNotFoundException;
import org.example.patientservice.mapper.PatientMapper;
import org.example.patientservice.model.PatientRequestDTO;
import org.example.patientservice.model.PatientResponseDTO;
import org.example.patientservice.repository.PatientRepository;
import org.example.patientservice.service.grpc.BillingServiceGrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        if (patientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + requestDTO.getEmail() + " already exists");
        }

        Patient newPatient = patientRepository.save(PatientMapper.toPatientEntity(requestDTO));
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
        return PatientMapper.toPatientResponseDTO(newPatient);
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
