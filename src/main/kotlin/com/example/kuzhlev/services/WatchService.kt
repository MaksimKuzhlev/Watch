package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.views.CreateWatchForm
import com.vaadin.flow.data.binder.BeanValidationBinder

interface WatchService {
    fun findAllWatches():List<WatchEntity>
    //fun create(watch: WatchEntity)
    fun update(watch:WatchEntity)
    fun save(binder: BeanValidationBinder<WatchEntity>, changeHandler: CreateWatchForm.ChangeHandler?)
    fun delete(changeHandler: CreateWatchForm.ChangeHandler?)
    fun check(token:String):Boolean

}