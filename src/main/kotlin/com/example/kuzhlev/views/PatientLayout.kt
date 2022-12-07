package com.example.kuzhlev.views

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.RouterLink

class PatientLayout() : VerticalLayout() {

    init{

        setHeightFull()
        width = "267px"
        style.set("background","white").set("padding-left","0")
        add(getTabs())
    }




    private fun getTabs(): Tabs {
        val tabs = Tabs()
        tabs.add(
            createTab(VaadinIcon.USER, "Личные данные",GridView::class.java),
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
        link.addClassNames("tabsBox")
        return Tab(link)
    }
}