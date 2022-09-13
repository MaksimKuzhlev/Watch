package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.UserRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.views.CreateUserForm
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.data.binder.BeanValidationBinder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserServiceImpl(private val userRepository: UserRepository,private val watchRepository: WatchRepository, @Autowired private val passwordEncoder: PasswordEncoder):UserService {
    override fun findAllUsers(): List<UserEntity> = userRepository.findAll()


    override fun findAllTokens(): List<String> = watchRepository.findAll().map{it.token}

    override fun delete(userEntity: UserEntity,changeHandler:CreateUserForm.ChangeHandler?) {
        userRepository.delete(userEntity)
        changeHandler?.onChange()
    }

    override fun save(binder:BeanValidationBinder<UserEntity>, userEntity: UserEntity, notification: Notification, changeHandler: CreateUserForm.ChangeHandler?,check:Int) {
        binder.writeBean(userEntity)
        if(userRepository.findByUsername(binder.bean.username)!=null&&check!=1){
            notification.open()
        } else {
            userEntity.password = passwordEncoder.encode(userEntity.password)
            userRepository.save(userEntity)
            changeHandler?.onChange()
        }
    }


}