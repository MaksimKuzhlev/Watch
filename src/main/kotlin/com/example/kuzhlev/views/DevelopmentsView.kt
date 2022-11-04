package com.example.kuzhlev.views

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@Route(value = "dev", layout = MainLayoutPeople::class)
@PageTitle("Developments")
@RolesAllowed("ADMIN")
class DevelopmentsView:VerticalLayout() {
    init {

    }
}