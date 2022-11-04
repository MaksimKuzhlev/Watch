package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PatientRepository:JpaRepository<PatientEntity,Long> {
    fun findByToken(token:String):PatientEntity
}