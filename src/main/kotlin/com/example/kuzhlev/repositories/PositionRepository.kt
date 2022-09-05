package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.PositionHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PositionRepository:JpaRepository<PositionHistoryEntity,Long> {
    fun findByToken(token:String):List<PositionHistoryEntity>
}