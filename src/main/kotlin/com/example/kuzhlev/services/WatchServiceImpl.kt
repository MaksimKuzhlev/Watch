package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.views.CreateWatchForm
import com.example.kuzhlev.views.GridView
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.binder.BeanValidationBinder
import org.springframework.stereotype.Service

@Service
class WatchServiceImpl(private val watchRepository: WatchRepository,private val watchEntity: WatchEntity):WatchService {
    override fun findAllWatches():List<WatchEntity> = watchRepository.findAll()

    /*override fun create(watch: WatchEntity) {
            watchRepository.save(watch)
    }*/

    override fun update(watch: WatchEntity) {
        val existingWatch = watchRepository.findByToken(watch.token)
       if( existingWatch!=null){
           existingWatch.longitude = watch.longitude
           existingWatch.latitude = watch.latitude
           existingWatch.masturbate= watch.masturbate
           existingWatch.heart_rate= watch.heart_rate
           existingWatch.has_fallen= watch.has_fallen
           existingWatch.charge_level= watch.charge_level
           existingWatch.network_level= watch.network_level
           watchRepository.save(existingWatch)



       } else throw RuntimeException ("watches not found")
    }

    override fun delete(changeHandler: CreateWatchForm.ChangeHandler?) {
        watchRepository.delete(watchEntity)
        changeHandler?.onChange()
    }

   override fun save(binder:BeanValidationBinder<WatchEntity>,changeHandler: CreateWatchForm.ChangeHandler?) {
        binder.writeBean(watchEntity)
        watchRepository.save(watchEntity)
        changeHandler?.onChange()
    }

    override fun check(token: String): Boolean {
        return watchRepository.findByToken(token)!=null
    }

}

