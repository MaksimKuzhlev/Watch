package com.example.kuzhlev.views

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@Route(value = "",layout = MainLayout::class)
@PageTitle("Personal cabinet")
@RolesAllowed("USER","ADMIN")
class PersonalView :VerticalLayout(){
    init{

        add(H1("It's your Personal cabinet"))
    }
}