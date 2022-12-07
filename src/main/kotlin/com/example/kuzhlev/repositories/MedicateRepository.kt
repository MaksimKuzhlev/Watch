package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.PatientMedicateEntity

import org.springframework.data.jpa.repository.JpaRepository

interface MedicateRepository:JpaRepository<PatientMedicateEntity,Long> {
    fun findByToken(token:String): List<PatientMedicateEntity>

}