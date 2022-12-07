package com.example.kuzhlev.views

import com.example.kuzhlev.security.SecurityService
import com.example.kuzhlev.security.WebSecurityConfig
import com.example.kuzhlev.services.PatientService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class MainLayoutPeople(@Autowired private val securityService: SecurityService, private val patientService: PatientService) : AppLayout() {


    init {

        style.set("background","white")
        val logout = Button("Log out")
        logout.width = "150px"
        logout.addClickListener { securityService.logout() }
        val toggle = DrawerToggle()
        val title = H1("Watch Data")
        title.style.set("font-size", "var(--lumo-font-size-l)")["margin"] = "0"
        title.setWidthFull()
        //addToNavbar(toggle,title,logout)
        addToDrawer(getTabs())

    }


    private fun getTabs(): Tabs {
        val tabs = Tabs()
        tabs.add(
            createTab(VaadinIcon.USER, "Личные данные",Inform::class.java),
            createTab(VaadinIcon.BAR_CHART_H, "Статистика",StatsView::class.java),
            createTab(VaadinIcon.DOCTOR_BRIEFCASE, "Болезни",DiseaseView::class.java),
            createTab(VaadinIcon.MAP_MARKER, "Карта",MapView::class.java),
            createTab(VaadinIcon.CLIPBOARD, "События",DevelopmentsView::class.java),
        )
        tabs.orientation = Tabs.Orientation.VERTICAL
        return tabs

    }

    private fun createTab(viewIcon: VaadinIcon, viewName: String, href:Any): Tab {
        val icon = viewIcon.create()
        icon.style.set("box-sizing", "border-box")
            .set("margin-inline-end", "var(--lumo-space-m)")
            .set("margin-inline-start", "var(--lumo-space-xs)")["padding"] = "var(--lumo-space-xs)"
        val link = RouterLink()
        link.add(icon, Span(viewName))
        link.setRoute(href as Class<out Component>?)
        link.tabIndex = -1
        return Tab(link)
    }
}