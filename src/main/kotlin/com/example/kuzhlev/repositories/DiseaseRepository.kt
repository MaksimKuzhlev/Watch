package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.PatientDiseaseEntity
import com.example.kuzhlev.entitys.PatientPhysicalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DiseaseRepository:JpaRepository<PatientDiseaseEntity,Long> {
    fun findByToken(token:String): PatientDiseaseEntity
}