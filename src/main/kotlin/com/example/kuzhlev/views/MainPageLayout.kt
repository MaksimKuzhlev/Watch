package com.example.kuzhlev.views

import com.example.kuzhlev.security.SecurityService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.router.RouterLink
import org.springframework.beans.factory.annotation.Autowired

@CssImport(
    themeFor = "vaadin-tab",
    value = "./themes/mytheme/components/vaadin-tab.css"
)
class MainPageLayout(@Autowired private val securityService: SecurityService):VerticalLayout() {

    init{
        val logout = Button("Log out")
        logout.width = "150px"
        logout.addClickListener { securityService.logout() }
        setHeightFull()
        width = "267px"
        style.set("background","white")
        add(getTabs(),logout)

    }


    private fun getTabs(): Tabs {
        val tabs = Tabs()

        tabs.add(
            createTab(VaadinIcon.GROUP, "Patients",GridView::class.java),
            createTab(VaadinIcon.USER_STAR, "Admins",UserView::class.java),
        )
        tabs.orientation = Tabs.Orientation.VERTICAL
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL)
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