package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.ActivityEntity
import com.example.kuzhlev.entitys.PatientMedicateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityRepository:JpaRepository <ActivityEntity,Long> {
    fun findByToken(token:String): List<ActivityEntity>

}