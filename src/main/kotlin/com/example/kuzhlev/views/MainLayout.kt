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


class MainLayout(@Autowired private val securityService: SecurityService, private val webSecurityConfig: WebSecurityConfig, private val patientService: PatientService) : AppLayout() {
    private val auth: Authentication = SecurityContextHolder.getContext().authentication


    init {
        val logout = Button("Log out")
        logout.width = "150px"
        logout.addClickListener { securityService.logout() }
        val toggle = DrawerToggle()
        val title = H1("Watch Data")
        title.style.set("font-size", "var(--lumo-font-size-l)")["margin"] = "0"
        title.setWidthFull()
        addToNavbar(toggle,title,logout)
        addToDrawer(getTabs())

    }

    private fun createHeader() {
        val logo = H2("Watch Data")
        val logout = Button("Log out")
        logout.isAutofocus = true
        logout.addClickListener { securityService.logout() }

        val  header = HorizontalLayout(DrawerToggle(),logo, logout)

        header.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        //header.expand(logo)
        header.width = "100%"
        header.addClassNames("py-0", "px-m")

        addToNavbar(header)
    }

    private fun createDraw() {
        if(webSecurityConfig.userDetailsServiceBean().loadUserByUsername(auth.name).authorities.toString() == "[ROLE_ADMIN]"){
            val listLink = RouterLink("Watches Data", GridView::class.java)
            listLink.highlightCondition = HighlightConditions.sameLocation()
            addToDrawer(
                VerticalLayout(
                    listLink,
                    RouterLink("Users", UserView::class.java),

                )
            )
        }
    }

    private fun getTabs(): Tabs {
        val tabs = Tabs()

           tabs.add(
                createTab(VaadinIcon.GROUP, "Patients",GridView::class.java),
                createTab(VaadinIcon.USER_STAR, "Admins",UserView::class.java),
            )
            tabs.orientation = Tabs.Orientation.VERTICAL
            return tabs

    }

    private fun createTab(viewIcon: VaadinIcon, viewName: String,href:Any): Tab {
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