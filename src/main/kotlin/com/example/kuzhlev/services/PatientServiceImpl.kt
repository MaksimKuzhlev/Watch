package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.repositories.PatientRepository
import com.vaadin.flow.data.binder.BeanValidationBinder
import org.springframework.stereotype.Service

@Service
class PatientServiceImpl(private val patientRepository: PatientRepository):PatientService {


    override fun save(patientEntity: PatientEntity) {
        patientRepository.save(patientEntity)
    }
}