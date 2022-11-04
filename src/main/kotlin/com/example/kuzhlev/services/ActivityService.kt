package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.ActivityEntity

interface ActivityService {


    fun save(activityEntity: ActivityEntity)

    fun delete(activityEntity: ActivityEntity)
}