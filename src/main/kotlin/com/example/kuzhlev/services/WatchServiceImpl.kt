package com.example.kuzhlev.services

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.DTO.Token
import com.example.kuzhlev.entitys.PositionHistoryEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository

import com.example.kuzhlev.views.CreateWatchForm
import com.example.kuzhlev.views.GridView
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.server.Command
import com.vaadin.flow.spring.SpringVaadinSession
import org.springframework.stereotype.Service


@Service
class WatchServiceImpl(private val watchRepository: WatchRepository,
                       private val watchEntity: WatchEntity,
                       private val positionRepository: PositionRepository,
                      ):WatchService {
    private var soss:Boolean = false
    private var tokenn:String = "0"




    override fun findAllWatches():List<WatchEntity> = watchRepository.findAll()
//сохранение истории запросов часов
    override fun create(positionHistoryEntity: PositionHistoryEntity) {
        positionRepository.save(positionHistoryEntity)
    }
// сохранение данных от часов
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
//удаление часов через форму
    override fun delete(watchEntity: WatchEntity,changeHandler: CreateWatchForm.ChangeHandler?) {
        watchRepository.delete(watchEntity)
        changeHandler?.onChange()
    }
//Сохранение Часов через форму
   override fun save(watchEntity:WatchEntity,changeHandler: CreateWatchForm.ChangeHandler?) {
        if(watchRepository.findByToken(watchEntity.token)==null){
            watchRepository.save(watchEntity)
            changeHandler?.onChange()
        }
    }
//проверка на существование токена
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

