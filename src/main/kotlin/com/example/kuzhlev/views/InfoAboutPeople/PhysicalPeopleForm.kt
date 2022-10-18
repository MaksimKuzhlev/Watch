package com.example.kuzhlev.views.InfoAboutPeople

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField


class PhysicalPeopleForm:VerticalLayout() {

    private val weight = TextField("Вес, кг")
    private val growth = TextField("Рост, см")
    private val old = TextField("Возраст")
    private val datePicker= DatePicker("Дата Рождения")
    init{
        setSizeFull()
        alignItems = FlexComponent.Alignment.END

        val layout1 =  HorizontalLayout(weight,growth)
        val layout2 =  HorizontalLayout(old,datePicker)

        weight.setWidthFull()
        growth.setWidthFull()
        old.setWidthFull()
        datePicker.setWidthFull()
        layout2.setSizeFull()
        layout1.setSizeFull()


        add(

                H2("Физические данные"),
                layout1,
                layout2

        )

    }



}