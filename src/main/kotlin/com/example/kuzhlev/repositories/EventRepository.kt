package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.EventEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository:JpaRepository<EventEntity,Long> {
}