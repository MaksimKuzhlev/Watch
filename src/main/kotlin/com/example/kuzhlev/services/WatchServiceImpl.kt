package com.example.kuzhlev.services

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.DTO.Token
import com.example.kuzhlev.entitys.PositionHistoryEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.views.CreateWatchForm
import org.springframework.stereotype.Service


@Service
class WatchServiceImpl(private val watchRepository: WatchRepository,
                       private val watchEntity: WatchEntity,
                       private val positionRepository: PositionRepository,
                      ):WatchService {
    private var soss:Boolean = false
    private var tokenn = mutableSetOf("0")




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
    override fun delete(watchEntity: WatchEntity) {
        watchRepository.delete(watchEntity)

    }
//Сохранение Часов через форму
   override fun save(watchEntity:WatchEntity) {
            if(watchRepository.findByToken(watchEntity.token)==null){
                watchRepository.save(watchEntity)
            }

    }
//Создание токена

//проверка на существование токена
    override fun check(token: Token) = watchRepository.findByToken(token.token)!=null

    override fun sos(sos: Sos) {
      soss = sos.sos
      tokenn.add(sos.token )
    }

    override fun sosrq(): Boolean {
        return soss
    }

    override fun tokrq(): MutableSet<String> {
        return tokenn
    }

    override fun tokremove(token:String) {
        tokenn.remove(token)

    }




}

