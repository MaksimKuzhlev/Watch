package com.example.kuzhlev.services

import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.views.CreateUserForm
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.data.binder.BeanValidationBinder



interface UserService {
    fun findAllUsers():List<UserEntity>
    fun findAllTokens():List<String>
    fun delete(userEntity: UserEntity,changeHandler: CreateUserForm.ChangeHandler?)
    fun save(binder: BeanValidationBinder<UserEntity>,userEntity: UserEntity,notification:Notification,changeHandler: CreateUserForm.ChangeHandler?,check:Int)
}