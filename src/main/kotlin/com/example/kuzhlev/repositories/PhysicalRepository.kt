package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.PatientPhysicalEntity
import com.example.kuzhlev.entitys.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PhysicalRepository: JpaRepository<PatientPhysicalEntity, Long> {
    fun findByToken(token:String): PatientPhysicalEntity
}