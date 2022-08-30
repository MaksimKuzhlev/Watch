package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.WatchService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope

@SpringComponent
@UIScope
final class CreateWatchForm(private val service:WatchService):FormLayout() {
    private val token = TextField("Token")
    var save = Button("Save")
    var delete = Button("Delete")
    var close = Button("Close")
    private val binder = BeanValidationBinder(WatchEntity::class.java)
    private var changeHandler: ChangeHandler?=null
    interface ChangeHandler {
        fun onChange(){}
    }

    init{
        addClassName("contact-form")
        add(
            token,
            createButtonsLayout()
        )
        binder.bindInstanceFields(this)

    }

    private fun createButtonsLayout(): Component {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        save.addClickShortcut(Key.ENTER)
        close.addClickShortcut(Key.ESCAPE)

        save.addClickListener { service.save(binder,changeHandler) }
        delete.addClickListener {  service.delete(changeHandler) }
        close.addClickListener {  isVisible=false }
        isVisible = false

        return HorizontalLayout(save, delete, close)
    }


    fun setChangeHandler(h: ChangeHandler){
        changeHandler = h
    }

}

