package com.example.kuzhlev.services

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.DTO.Token
import com.example.kuzhlev.entitys.PositionHistoryEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.views.CreateWatchForm
import com.example.kuzhlev.views.GridView
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service



@Service
class WatchServiceImpl(private val watchRepository: WatchRepository,
                       private val watchEntity: WatchEntity,
                       private var positionHistoryEntity: PositionHistoryEntity,
                       private val positionRepository: PositionRepository):WatchService {
    private var soss:Boolean = false
    private var tokenn:String = "0"

    override fun findAllWatches():List<WatchEntity> = watchRepository.findAll()

    override fun create(positionHistoryEntity: PositionHistoryEntity) {
        positionRepository.save(positionHistoryEntity)
    }

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

    override fun check(token: Token) = watchRepository.findByToken(token.token)!=null

    override fun sos(sos: Sos) {
      soss = sos.sos
      tokenn = sos.token
    }

    override fun sosrq(): Boolean {
        return soss
    }

    override fun tokrq(): String {
        return tokenn
    }




}

