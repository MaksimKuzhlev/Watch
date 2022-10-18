package com.example.kuzhlev.views.InfoAboutPeople

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

class TakingMedication:VerticalLayout() {
    private val addButton = Button("Добавить лекарство")
    private val layout3=VerticalLayout()
    init {
        setWidthFull()
        alignItems = FlexComponent.Alignment.END

        add(H2("Принимаемые лекарства"),layout3,confButton())
    }

    private fun confButton():Component{
        layout3.style.set("padding","0px")
        setAlignSelf(FlexComponent.Alignment.STRETCH,addButton)
        addButton.addClickListener {
            layout3.add(addMedicate())
        }

        return addButton
    }

    private fun addMedicate():Component{
        val amount = ComboBox<String> ("Кол-во")
        val frequensy = ComboBox<String> ("Частота")
        val name = TextField("Название")
        val delButton = Button(createIcon(VaadinIcon.CLOSE_CIRCLE))
        val content = HorizontalLayout(delButton,amount,frequensy,name)
        amount.setSizeFull()
        frequensy.setSizeFull()
        name.setSizeFull()

        amount.setItems("по 1 т","по 2 т","по 3 т","по 4 т","по 5 т","по 1 ст.л.","по 2 ст.л.","по 3 ст.л.","по 4 ст.л.")
        frequensy.setItems("1 раз/день","2 раза/день","3 раза/день","4 раза/день")

        delButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        setAlignSelf(FlexComponent.Alignment.END,delButton)


        delButton.addClickListener { remove(HorizontalLayout(delButton,amount,frequensy,name)) }
        content.setSizeFull()
        return content
    }

    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.color = "red"
        return icon
    }

}