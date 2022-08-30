package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.repositories.UserRepository
import com.example.kuzhlev.services.UserService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder


class CreateUserForm( private val watchEntity:List<String>,private val userRepository: UserRepository,private var userEntity: UserEntity,private val service:UserService):FormLayout() {
    private val firstName = TextField("First name")
    private val secondName = TextField ("Second name")
    private val username = TextField ("username")
    private val password = TextField ("password")
    private val role = ComboBox<String>("role")
    private val active = ComboBox<Boolean>("active")
    private val token:ComboBox<String> = ComboBox("Token")
    private val notification = Notification()
    private val text = Div(Text("Failed to create user"))
    private val closeButton: Button = Button(Icon("lumo", "cross"))
    private var check = 0

    private val save = Button ("save")
    private val delete = Button ("delete")
    private val close = Button ("cansel")
    private val binder = BeanValidationBinder(UserEntity::class.java)
    private var changeHandler: ChangeHandler?=null
    interface ChangeHandler {
        fun onChange(){}
    }

    init{

        createForma()
        createNotification()
        addClassName("User-form")
        add(
            firstName,
            secondName,
            token,
            username,
            password,
            role,
            active,
            createButtonsLayout()
        )
            binder.bindInstanceFields(this)

    }

    private fun createForma(){
        token.setItems(watchEntity)
        role.setItems("USER","ADMIN")
        active.setItems(true, false)

    }

   private fun  createNotification(){
       notification.addThemeVariants(NotificationVariant.LUMO_ERROR)
       closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)

       closeButton.element.setAttribute("aria-label", "Close")

       closeButton.addClickListener { notification.close() }

       val layout = HorizontalLayout(text, closeButton)
       layout.alignItems = FlexComponent.Alignment.CENTER
       notification.add(layout)

   }

    private fun createButtonsLayout(): Component {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY)


        save.addClickShortcut(Key.ENTER)
        close.addClickShortcut(Key.ESCAPE)

        save.addClickListener {
            service.save(binder,userEntity,notification,changeHandler,check)
            check = 0
            println(check)
        }
        delete.addClickListener { service.delete(userEntity,changeHandler) }
        close.addClickListener {  isVisible=false }


        isVisible = false
        return HorizontalLayout(save, delete, close)
    }

    fun editPeople(c: UserEntity?) {
        if (c == null) {
            isVisible = false
            return
        }

        val persisted = c.id.toInt() != 0
        if (persisted) {
            userEntity =  userRepository.findById(c.id).get()
           check = 1
            println(check)
        }
        else {
            userEntity = c
            check = 0
        }

        binder.bean = userEntity
        isVisible = true

        firstName.focus()
    }

    fun setChangeHandler(h: ChangeHandler){
        changeHandler = h
    }

}