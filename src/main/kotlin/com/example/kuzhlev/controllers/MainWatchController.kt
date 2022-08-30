package com.example.kuzhlev.controllers

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.services.WatchService
import org.springframework.web.bind.annotation.*
import javax.annotation.security.PermitAll

@RestController
@RequestMapping("/watch")
class MainWatchController(private val watchService: WatchService) {


    @PostMapping()
    fun update(@RequestBody watch:WatchEntity) = watchService.update(watch)

    @GetMapping("/{token}")
    fun check(@PathVariable token:String) = watchService.check(token)



}