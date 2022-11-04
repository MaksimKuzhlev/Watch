package com.example.kuzhlev.views.InfoAboutPeople

import com.example.kuzhlev.entitys.PatientMedicateEntity
import com.example.kuzhlev.repositories.MedicateRepository
import com.example.kuzhlev.services.PatientService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.theme.lumo.LumoUtility

class TakingMedication(private val patientService: PatientService, private var medicateEntity: PatientMedicateEntity,private val medicateRepository: MedicateRepository):VerticalLayout() {
    private val addButton = Button("Добавить лекарство")
    private val layout3=VerticalLayout()
    private val list = mutableListOf<Long>()
    private val mapAm = mutableMapOf<String,String>()
    private val mapFr = mutableMapOf<String,String>()
    private val mapNm = mutableMapOf<String,String>()
    private var i = 0

    init {
        //maxWidth = "700px"
        width = "700px"

        style.set("background","white").set("border-radius","16px")
        val text = H2("Принимаемые лекарства")
        text.style.set("margin-top","10px")
        alignItems = FlexComponent.Alignment.END

        add(text,layout3,confButton())
    }

    private fun confButton():Component{
        layout3.style.set("padding","0px")
        setAlignSelf(FlexComponent.Alignment.STRETCH,addButton)
        addButton.style.set("margin-top","22px")
        addButton.addClickListener {
            layout3.add(addMedicate())
            i++
        }

        return addButton
    }

    private fun addMedicate():Component{
        val amount = ComboBox<String> ("Кол-во")
        val frequensy = ComboBox<String> ("Частота")
        val nameMed = TextField("Название")
        val delButton = Button(createIcon(VaadinIcon.CLOSE_CIRCLE))

        delButton.width = "20px"
        amount.width = "25%"
        frequensy.width = "25%"
        nameMed.width = "50%"

        val content = HorizontalLayout(delButton,amount,frequensy,nameMed)
        content.setSizeFull()
        while(mapAm[i.toString()]!=null)
            i++
        content.setId(i.toString())

        amount.setItems("по 1 т.","по 2 т.","по 3 т.","по 4 т.","по 5 т.","по 1 ст.л.","по 2 ст.л.","по 3 ст.л.","по 4 ст.л.")
        frequensy.setItems("1 раз/день","2 раза/день","3 раза/день","4 раза/день")

        delButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        setAlignSelf(FlexComponent.Alignment.END,delButton)

        amount.addValueChangeListener { mapAm[content.id.get()] = it.value }
        frequensy.addValueChangeListener { mapFr[content.id.get()] = it.value }
        nameMed.addValueChangeListener { mapNm[content.id.get()] = it.value }

        delButton.addClickListener { content.element.removeFromParent() }

        editPeople(PatientMedicateEntity(0,"","","",""))
        list.add(0)
        return content
    }

    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.color = "red"
        return icon
    }

    fun save(token:String){
        var e = 0
        layout3.children.forEach {
            medicateEntity.amount = mapAm[it.id.get()].toString()
            medicateEntity.frequency = mapFr[it.id.get()].toString()
            medicateEntity.nameMed = mapNm[it.id.get()].toString()
            medicateEntity.token = token
            medicateEntity.id = list[e]
            patientService.saveMedicatePeople(medicateEntity)
            e++
        }

    }


    fun editPeople(c: PatientMedicateEntity) {
        val persisted = c.id.toInt() != 0
        if (persisted) {
            val amount = ComboBox<String> ("Кол-во")
            val frequensy = ComboBox<String> ("Частота")
            val nameMed = TextField("Название")
            val delButton = Button(createIcon(VaadinIcon.CLOSE_CIRCLE))

            amount.width = "25%"
            frequensy.width = "25%"
            nameMed.width = "50%"

            val content = HorizontalLayout(delButton,amount,frequensy,nameMed)
            content.setSizeFull()
            content.setId(c.id.toString())

            amount.setItems("по 1 т.","по 2 т.","по 3 т.","по 4 т.","по 5 т.","по 1 ст.л.","по 2 ст.л.","по 3 ст.л.","по 4 ст.л.")
            frequensy.setItems("1 раз/день","2 раза/день","3 раза/день","4 раза/день")

            delButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            setAlignSelf(FlexComponent.Alignment.END,delButton)

            amount.addValueChangeListener { mapAm[content.id.get()] = it.value }
            frequensy.addValueChangeListener { mapFr[content.id.get()] = it.value }
            nameMed.addValueChangeListener { mapNm[content.id.get()] = it.value }
            delButton.addClickListener {
                medicateEntity = medicateRepository.findById(content.id.get().toLong()).get()
                patientService.deleteMedicatePeople(medicateEntity)
            }

            amount.value = c.amount
            frequensy.value = c.frequency
            nameMed.value = c.nameMed
            mapAm[content.id.get()] = c.amount
            mapFr[content.id.get()] = c.frequency
            mapNm[content.id.get()] = c.nameMed
            list.add(c.id)

            delButton.addClickListener { content.element.removeFromParent() }

            layout3.add(content)

        }
        else {
            medicateEntity = c

        }

    }


}