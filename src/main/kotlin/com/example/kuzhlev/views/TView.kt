package com.example.kuzhlev.views

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.polymertemplate.Id

import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@Route(value= "map")
@PageTitle("map")
@RolesAllowed("ADMIN")
class TView:VerticalLayout(){

    init{
        val map = GoogleMap()
        val layout = Div()
        layout.add(map)
    }
}