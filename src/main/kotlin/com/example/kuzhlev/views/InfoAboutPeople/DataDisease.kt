package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientDiseaseEntity
import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.entitys.PatientPhysicalEntity
import com.example.kuzhlev.repositories.DiseaseRepository
import com.example.kuzhlev.services.PatientService
import com.vaadin.flow.component.Component
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
import com.vaadin.flow.data.binder.BeanValidationBinder


class DataDisease(private val patientService: PatientService,private var patientDisease: PatientDiseaseEntity,private val diseaseRepository: DiseaseRepository):VerticalLayout() {
    private val diseaseGroup = CheckboxGroup<String>()
    private val addDisease =TextField("Добавить болезнь")
    private val buttPlus=Button(Icon(VaadinIcon.PLUS_CIRCLE_O))
    private val setDisease = mutableSetOf<String>()
    private val layout1 = HorizontalLayout()


    init {
        //maxWidth= "430px"
        width = "430px"
        style.set("background","white").set("border-radius","16px")
        val text = H2("Данные о болезнях")
        text.style.set("margin-top","0px")

        //setWidthFull()
        alignItems = FlexComponent.Alignment.END
        buttPlus.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        add(text,diseaseGroup,addToolBar(),layout1)

    }

    private fun addToolBar():Component{
        addDisease.setSizeFull()
        val layout = HorizontalLayout(buttPlus,addDisease)
        layout.setSizeFull()
        layout1.maxWidth = "200px"
        layout1.style.set("flex-wrap","wrap")
        diseaseGroup.setItems("Инсульт","Аритмия","Тахикордия","Брахикордия")
        diseaseGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL)
        //diseaseGroup.setWidthFull()
        setAlignSelf(FlexComponent.Alignment.END,buttPlus)


        buttPlus.addClickListener {
            val spanClose = Span(createIcon(VaadinIcon.CLOSE_SMALL))
            val span = Span(spanClose,Span(addDisease.value))
            span.setId(addDisease.value)
            spanClose.addClickListener { span.element.removeFromParent() }

            layout1.add(span)
            addDisease.clear()
        }

        return layout
    }

    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.color = "blue"
        icon.style["padding"] = "var(--lumo-space-xs"
        return icon
    }


    fun save(token:String){
        var disease:String?
        patientDisease.token = token
        layout1.children.forEach { setDisease.add(it.id.get()) }
        if(setDisease.isNotEmpty()) {
            disease = setDisease.toString()
            disease = disease.trimEnd(']')
            disease = disease.trimStart('[')
        }
        else disease = null
        patientDisease.disease = disease
        patientDisease.arrhythmia = diseaseGroup.value.contains("Аритмия")
        patientDisease.tachycardia = diseaseGroup.value.contains("Тахикордия")
        patientDisease.bradycardia = diseaseGroup.value.contains("Брахикордия")
        patientDisease.stroke = diseaseGroup.value.contains("Инсульт")
        patientService.saveDiseasePeople(patientDisease)
    }


    fun editPeople(c: PatientDiseaseEntity?) {
        if (c == null) {
            return
        }

        val persisted = c.id.toInt() != 0
        if (persisted) {
            patientDisease = diseaseRepository.findByToken(c.token)
        }
        else {
            patientDisease = c
        }
        val list = mutableSetOf<String>()
        if(c.arrhythmia)
            list.add("Аритмия")
        if(c.tachycardia)
            list.add("Тахикордия")
        if(c.bradycardia)
            list.add("Брахикордия")
        if(c.stroke)
            list.add("Инсульт")

        diseaseGroup.select(list)
        list.clear()
        c.disease?.split(',')?.forEach {
            val spanClose = Span(createIcon(VaadinIcon.CLOSE_SMALL))
            val span = Span(spanClose,Span(it))
            span.setId(it)
            spanClose.addClickListener {span.element.removeFromParent()}
            layout1.add(span)
        }
    }

}