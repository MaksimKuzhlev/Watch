package com.example.kuzhlev.controllers

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.DTO.Token
import com.example.kuzhlev.entitys.PositionHistoryEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.services.WatchService
import com.unboundid.util.json.JSONObject

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/watch")
class MainWatchController(private val watchService: WatchService,private val positionHistoryEntity: PositionHistoryEntity) {


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




}