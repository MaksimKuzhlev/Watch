package com.example.kuzhlev.repositories

import com.example.kuzhlev.entitys.WatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface WatchRepository:JpaRepository<WatchEntity,Long>  {
    fun findByToken(token:String):WatchEntity?

    @Query("FROM WatchEntity w WHERE w.name like concat(:name,'%')")
    fun findByName(@Param("name") name:String):List<WatchEntity>

}