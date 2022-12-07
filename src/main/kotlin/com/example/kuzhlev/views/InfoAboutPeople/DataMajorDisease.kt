package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientDiseaseEntity
import com.example.kuzhlev.repositories.DiseaseRepository
import com.example.kuzhlev.services.PatientService
import com.vaadin.componentfactory.ToggleButton
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

class DataMajorDisease(private val patientService: PatientService, private var patientDisease: PatientDiseaseEntity, private val diseaseRepository: DiseaseRepository):VerticalLayout() {

    private val list = listOf("Инсульт","Аритмия","Тахикордия","Брахикордия")
    private val map = mutableMapOf<String,Boolean>()
    private val list1 = mutableSetOf<Boolean>()
    init {

        width = "430px"
        style.set("background","white").set("border-radius","16px")
        val text = H2("Основные болезни")
        text.style.set("margin-top","16px")
        text.style.set("margin-right","8px")
        text.style.set("margin-bottom","4px")

        if(patientService.tokenrq()!=""){
            val patDis = diseaseRepository.findByToken(patientService.tokenrq())

            if (patDis != null) {
                list1.add(patDis.stroke)

                map["Инсульт"] = patDis.stroke

                list1.add(patDis.arrhythmia)
                map["Аритмия"] = patDis.arrhythmia

                list1.add(patDis.tachycardia)
                map["Тахикордия"] = patDis.tachycardia

                list1.add(patDis.bradycardia)
                map["Брахикордия"] = patDis.bradycardia
            }
        }else{
            map["Инсульт"] = false
            map["Аритмия"] = false
            map["Тахикордия"] = false
            map["Брахикордия"] = false
        }

        alignItems = FlexComponent.Alignment.END

        add(text)

        for(i in 0..3){
            diseaseMainContent(list[i])
        }

    }

    private fun diseaseMainContent(text:String){
        val toggleButton = ToggleButton()
        val textLabel= Text(text)
        toggleButton.setId(textLabel.toString())
        val layout = HorizontalLayout()
        layout.add(toggleButton,textLabel)
        layout.justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        layout.alignItems = FlexComponent.Alignment.CENTER
        layout.style.set("padding-left","4px")
        layout.style.set("padding-right","8px")
        layout.setWidthFull()
        toggleButton.value = map[text]
        toggleButton.addValueChangeListener {
            map[text] = it.value
        }
        add(layout)
    }




    fun save():MutableMap<String,Boolean>{
        return map
    }


    fun editPeople(c: PatientDiseaseEntity?) {
        if (c == null) {
            return
        }

        val persisted = c.id.toInt() != 0
        if (persisted) {
            patientDisease = diseaseRepository.findByToken(c.token)!!
        }
        else {
            patientDisease = c
        }


    }
}