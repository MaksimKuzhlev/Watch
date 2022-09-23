package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.ActivityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityRepository:JpaRepository <ActivityEntity,Long> {
}