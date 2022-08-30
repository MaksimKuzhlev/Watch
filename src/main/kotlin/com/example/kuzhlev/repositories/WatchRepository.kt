package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.WatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchRepository:JpaRepository<WatchEntity,Long>  {
    fun findByToken(token:String):WatchEntity?

}