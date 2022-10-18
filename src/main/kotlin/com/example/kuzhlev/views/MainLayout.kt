package com.example.kuzhlev.views

import com.example.kuzhlev.security.SecurityService
import com.example.kuzhlev.security.WebSecurityConfig
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.reflect

class MainLayout(@Autowired private val securityService: SecurityService, private val webSecurityConfig: WebSecurityConfig) : AppLayout() {
    private val auth: Authentication = SecurityContextHolder.getContext().authentication

    init {
        createHeader()
        createDraw()

    }

    private fun createHeader() {
        val logo = H2("Watch Data")
        logo.addClassNames("text-l", "m-m")


        val logout = Button("Log out")
        logout.isAutofocus = true
        logout.addClickListener { securityService.logout() }

        val  header = HorizontalLayout(DrawerToggle(),logo, logout)

        header.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        header.expand(logo)
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
}