package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientDiseaseEntity
import com.example.kuzhlev.entitys.PatientEntity

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

    private val addDisease =TextField()
    private val buttPlus=Button(Icon(VaadinIcon.PLUS_CIRCLE_O))
    private val setDisease = mutableSetOf<String>()
    private val layout1 = HorizontalLayout()


    init {
        //maxWidth= "430px"
        width = "657px"
        style.set("background","white").set("border-radius","16px")
        val text = H2("Другие болезни")
        text.style.set("margin-top","16px")
        text.style.set("margin-right","8px")
        text.style.set("margin-bottom","4px")
        alignItems = FlexComponent.Alignment.END
        buttPlus.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        add(text,addToolBar(),layout1)

    }

    private fun addToolBar():Component{
        addDisease.setSizeFull()
        addDisease.style.set("padding","4px")
        addDisease.placeholder = "Добавить болезнь"
        val layout = HorizontalLayout(buttPlus,addDisease)
        layout.setSizeFull()
        layout1.maxWidth = "600px"
        layout1.style.set("flex-wrap","wrap")
        setAlignSelf(FlexComponent.Alignment.END,buttPlus)


        buttPlus.addClickListener {
            val spanClose = Span(createIcon(VaadinIcon.CLOSE_SMALL))
            val spanDisease = Span(addDisease.value)
            spanDisease.style.set("padding-right","10px")
            val span = Span(spanClose,spanDisease)
            span.style.set("border","1px solid blue").set("border-radius","16px")
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


    fun save(token:String):String?{
        var disease:String?
        patientDisease.token = token
        layout1.children.forEach { setDisease.add(it.id.get()) }
        if(setDisease.isNotEmpty()) {
            disease = setDisease.toString()
            disease = disease.trimEnd(']')
            disease = disease.trimStart('[')
        }
        else disease = null
        return disease

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

        c.disease?.split(',')?.forEach {
            val spanClose = Span(createIcon(VaadinIcon.CLOSE_SMALL))
            val spanDisease = Span(it)
            spanDisease.style.set("padding-right","10px")
            val span = Span(spanClose,spanDisease)
            span.style.set("border","1px solid blue").set("border-radius","16px")
            span.setId(it)
            spanClose.addClickListener {span.element.removeFromParent()}
            layout1.add(span)
        }
    }

}