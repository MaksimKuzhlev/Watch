package com.example.kuzhlev.views.InfoAboutPeople

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
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


class DataDisease:VerticalLayout() {
    private val diseaseGroup = CheckboxGroup<String>()
    private val addDisease =TextField("Добавить болезнь")
    private val buttPlus=Button(Icon(VaadinIcon.PLUS_CIRCLE_O))
    private val setDisease = mutableSetOf<String>()
    private val layout1 = HorizontalLayout()


    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        buttPlus.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        add(H2("Данные о болезнях"),diseaseGroup,addToolBar(),layout1)
    }

    private fun addToolBar():Component{


        diseaseGroup.setItems("Был инсульт","Аритмия","Тахикордия","Брахикордия")
        diseaseGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL)
        addDisease.setSizeFull()
        setAlignSelf(FlexComponent.Alignment.END,buttPlus)


        buttPlus.addClickListener {
            setDisease.add(addDisease.value)
            val span = Span(Button(createIcon(VaadinIcon.CLOSE_SMALL)),Span(addDisease.value))
            layout1.add(span)

        }

        return HorizontalLayout(buttPlus,addDisease)
    }

    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.color = "blue"
        icon.style["padding"] = "var(--lumo-space-xs"
        return icon
    }



}