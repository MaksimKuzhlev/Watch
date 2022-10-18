package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.PatientEntity
import com.vaadin.flow.data.binder.BeanValidationBinder

interface PatientService {
    fun save(patientEntity: PatientEntity)
}