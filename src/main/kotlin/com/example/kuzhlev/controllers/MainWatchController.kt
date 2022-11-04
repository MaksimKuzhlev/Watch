package com.example.kuzhlev.controllers

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.DTO.Token
import com.example.kuzhlev.entitys.ActivityEntity
import com.example.kuzhlev.entitys.PositionHistoryEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.ActivityRepository
import com.example.kuzhlev.services.ActivityService
import com.example.kuzhlev.services.WatchService


import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/watch")
class MainWatchController(private val watchService: WatchService,
                          private val positionHistoryEntity: PositionHistoryEntity,
                          private val activityService: ActivityService,
                          private val activityRepository: ActivityRepository) {

    @PostMapping()
    fun update(@RequestBody watch:WatchEntity)  {
        watchService.update(watch)
        positionHistoryEntity.token=watch.token
        positionHistoryEntity.lat=watch.latitude
        positionHistoryEntity.lon=watch.longitude
        watchService.create(positionHistoryEntity)
    }

    @PostMapping(value = ["/auth"],produces=["application/json"])
    fun check(@RequestBody token:Token) = """{"is_authorized":${watchService.check(token)}}"""

    @PostMapping("/sos")
    fun sos(@RequestBody sos:Sos) = watchService.sos(sos)

    @PostMapping("/activity")
    fun activity(@RequestBody activity: ActivityEntity){
        val list = activityRepository.findByToken(activity.token)
        activityService.delete(list.first())
        activityService.save(activity)
    }




}