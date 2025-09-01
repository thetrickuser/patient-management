package org.example.mapper;

import org.example.entity.Patient;
import org.example.model.PatientRequestDTO;
import org.example.model.PatientResponseDTO;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toPatientDTO(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId().toString());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setDateOfBirth(patient.getDateOfBirth().toString());
        patientResponseDTO.setAddress(patient.getAddress());
        return patientResponseDTO;
    }

    public static Patient toPatientEntity(PatientRequestDTO requestDTO) {
        Patient patient = new Patient();
        patient.setAddress(requestDTO.getAddress());
        patient.setEmail(requestDTO.getEmail());
        patient.setName(requestDTO.getName());
        patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(requestDTO.getRegisteredDate()));
        return patient;
    }
}
