package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.ActivityEntity
import com.example.kuzhlev.repositories.ActivityRepository
import org.springframework.stereotype.Service

@Service
class ActivityServiceImpl(private val activityRepository: ActivityRepository):ActivityService {


    override fun save(activityEntity: ActivityEntity) {
        activityRepository.save(activityEntity)
    }

    override fun delete(activityEntity: ActivityEntity) {
        activityRepository.delete(activityEntity)
    }
}