package com.example.kuzhlev.views

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@Route(value = "disease", layout = MainLayoutPeople::class)
@PageTitle("Diseases")
@RolesAllowed("ADMIN")
class DiseaseView:VerticalLayout() {
    init {

    }
}