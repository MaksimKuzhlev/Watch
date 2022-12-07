package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.repositories.PatientRepository
import com.example.kuzhlev.services.PatientService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource


class InfoPeopleForm(private val patientService: PatientService,private var patientEntity: PatientEntity,private val patientRepository:PatientRepository):VerticalLayout(){
    private  var avatarBasic = Avatar()
    private val fio = TextField("Фамилия Имя Отчество")
    private val city = TextField("Город")
    private val address = TextField("Адрес")
    private val number = TextField("Телефон")
    private val email = TextField("Почта")
    private val numberConfidant = TextField("Телефон доверенного лица")
    private val weight = TextField("Вес, кг")
    private val growth = TextField("Рост, см")
    private val old = TextField("Возраст")
    private val birthday= DatePicker("Дата Рождения")

    private val binder = BeanValidationBinder(PatientEntity::class.java)

    init {
        width = "657px"
        style.set("background","white").set("border-radius","16px")

        val text = H2("Данные о клиенте")
        text.style.set("margin-top","10px")
        text.style.set("margin-bottom","8px")
        alignItems = FlexComponent.Alignment.END
        configMainLayout()
        add(text,mainLayout())
        binder.bindInstanceFields(this)
    }


    private fun mainLayout():Component{
        val hlayout1 = HorizontalLayout(weight,growth)
        val hlayout2 = HorizontalLayout(old,birthday)
        val layout = VerticalLayout(fio,city,address,number,email,numberConfidant,hlayout1,hlayout2)
        layout.style.set("padding-top","0")
        val mainLayout = HorizontalLayout(layout,avatarBasic)
        mainLayout.setWidthFull()
        return mainLayout
    }


    private fun configMainLayout(){
        if(patientService.tokenrq()!="")
        {
            val imageResource = StreamResource("${patientService.tokenrq()}.png", InputStreamFactory {
                    javaClass.getResourceAsStream("/images/${patientService.tokenrq()}.png")
                }
            )
                avatarBasic.imageResource = imageResource

        }
        avatarBasic.width = "171px"
        avatarBasic.height = "171px"
        fio.setWidthFull()
        fio.style.set("padding-bottom","0").set("padding-top","0")
        city.setWidthFull()
        city.style.set("padding-bottom","0").set("padding-top","0")
        address.setWidthFull()
        address.style.set("padding-bottom","0").set("padding-top","0")
        number.setWidthFull()
        number.style.set("padding-bottom","0").set("padding-top","0")
        email.setWidthFull()
        email.style.set("padding-bottom","0").set("padding-top","0")
        numberConfidant.setWidthFull()
        numberConfidant.style.set("padding-bottom","0").set("padding-top","0")
        weight.setWidthFull()
        weight.style.set("padding-bottom","0").set("padding-top","0")
        growth.setWidthFull()
        growth.style.set("padding-bottom","0").set("padding-top","0")
        old.setWidthFull()
        old.style.set("padding-bottom","0").set("padding-top","0")
        birthday.setWidthFull()
        birthday.style.set("padding-bottom","0").set("padding-top","0")
    }


    fun save(){
        if(!patientService.checkrq())
            patientEntity.token = patientService.createToken()
        binder.writeBean(patientEntity)
        patientService.saveInfoPeople(patientEntity)
    }

    fun name():String{
        return fio.value
    }

    fun token():String{
        return patientEntity.token
    }

    fun editPeople(c: PatientEntity?) {
        if (c == null) {
            isVisible = false
            return
        }

        val persisted = c.id.toInt() != 0
        if (persisted) {
            patientEntity =  patientRepository.findById(c.id).get()
        }
        else {
            patientEntity = c
        }

        binder.bean = patientEntity
        isVisible = true
    }


}