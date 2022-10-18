package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.services.PatientService
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.avatar.AvatarVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.theme.lumo.LumoUtility


class InfoPeopleForm(private val patientService: PatientService,private val patientEntity: PatientEntity):VerticalLayout(){
    private  var avatarBasic = Avatar()
    private val fio = TextField("Фамилия Имя Отчество")
    private val city = TextField("Город")
    private val address = TextField("Адрес")
    private val number = TextField("Телефон")
    private val email = TextField("Почта")
    private val numberConfidant = TextField("Телефон доверенного лица")

    private val binder = BeanValidationBinder(PatientEntity::class.java)

    init {
        setSizeFull()
        avatarBasic.addThemeVariants(AvatarVariant.LUMO_XLARGE)
        fio.setSizeFull()
        city.setSizeFull()
        address.setSizeFull()
        number.setSizeFull()
        email.setSizeFull()
        numberConfidant.setSizeFull()
        alignItems = FlexComponent.Alignment.END
        add(H2("Контактные данные"),avatarBasic,fio,city,address,number,email,numberConfidant)
        binder.bindInstanceFields(this)
    }


    fun save(){
        binder.writeBean(patientEntity)
        patientService.save(patientEntity)

    }
}