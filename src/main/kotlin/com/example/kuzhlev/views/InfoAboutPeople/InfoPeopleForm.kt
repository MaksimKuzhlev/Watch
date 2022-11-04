package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PatientRepository
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
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.theme.lumo.LumoUtility
import org.hibernate.annotations.Formula


class InfoPeopleForm(private val patientService: PatientService,private var patientEntity: PatientEntity,private val patientRepository:PatientRepository):VerticalLayout(){
    private  var avatarBasic = Avatar()
    private val fio = TextField("Фамилия Имя Отчество")
    private val city = TextField("Город")
    private val address = TextField("Адрес")
    private val number = TextField("Телефон")
    private val email = TextField("Почта")
    private val numberConfidant = TextField("Телефон доверенного лица")

    private val binder = BeanValidationBinder(PatientEntity::class.java)

    init {
        width = "430px"
        style.set("background","white").set("border-radius","16px")

        val text = H2("Контактные данные")
        text.style.set("margin-top","10px")

        alignItems = FlexComponent.Alignment.END
        configMainLayout()
        add(text,avatarBasic,fio,city,address,number,email,numberConfidant)
        binder.bindInstanceFields(this)
    }

    private fun configMainLayout(){
        if(patientService.tokenrq()!="")
        {
            val imageResource = StreamResource("${patientService.tokenrq()}.png",
                InputStreamFactory {
                    javaClass.getResourceAsStream("/images/${patientService.tokenrq()}.png")
                })
            avatarBasic.imageResource = imageResource
        }
        avatarBasic.addThemeVariants(AvatarVariant.LUMO_XLARGE)
        fio.setWidthFull()
        city.setWidthFull()
        address.setWidthFull()
        number.setWidthFull()
        email.setWidthFull()
        numberConfidant.setWidthFull()
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