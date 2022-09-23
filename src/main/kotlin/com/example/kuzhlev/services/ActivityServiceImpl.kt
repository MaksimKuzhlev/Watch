package com.example.kuzhlev.services

import com.example.kuzhlev.repositories.ActivityRepository

class ActivityServiceImpl(private val activityRepository: ActivityRepository):ActivityService {
    override fun findActivity(): List<ActivityService> {
        TODO("Not yet implemented")
    }
}