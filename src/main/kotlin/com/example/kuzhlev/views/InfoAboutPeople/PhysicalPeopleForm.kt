package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.entitys.PatientPhysicalEntity
import com.example.kuzhlev.repositories.PhysicalRepository
import com.example.kuzhlev.services.PatientService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.theme.lumo.LumoUtility.Padding.Vertical


class PhysicalPeopleForm(private val patientService: PatientService, private var physicalEntity: PatientPhysicalEntity,private val physicalRepository: PhysicalRepository):VerticalLayout() {

    private val weight = TextField("Вес, кг")
    private val growth = TextField("Рост, см")
    private val old = TextField("Возраст")
    private val birthday= DatePicker("Дата Рождения")
    private val binder = BeanValidationBinder(PatientPhysicalEntity::class.java)
    init{
        style.set("background","white").set("border-radius","16px")
        val text = H2("Физические данные")
        text.style.set("margin-top","10px")
        //maxWidth= "430px"
        width = "430px"

        alignItems = FlexComponent.Alignment.END
        val layout1 =  HorizontalLayout(weight,growth)
        val layout2 =  HorizontalLayout(old,birthday)

        weight.setWidthFull()
        growth.setWidthFull()
        old.setWidthFull()
        birthday.setWidthFull()
        layout2.setSizeFull()
        layout1.setSizeFull()


        add(text, layout1, layout2)
        binder.bindInstanceFields(this)
    }

    fun save(token:String){
        physicalEntity.token = token
        binder.writeBean(physicalEntity)
        patientService.savePhysicPeople(physicalEntity)
    }


    fun editPeople(c: PatientPhysicalEntity?) {
        if (c == null) {
            return
        }

        val persisted = c.id.toInt() != 0
        if (persisted) {
            physicalEntity = physicalRepository.findByToken(c.token)
        }
        else {
            physicalEntity = c

        }

        binder.bean = physicalEntity

    }


}