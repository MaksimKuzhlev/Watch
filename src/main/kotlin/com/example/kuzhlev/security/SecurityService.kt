package com.example.kuzhlev.security


import com.vaadin.flow.component.UI
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinSession
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Component

@Component
class SecurityService() {



    fun logout() {
        UI.getCurrent().page.setLocation(LOGOUT_SUCCESS_URL)
        val logoutHandler = SecurityContextLogoutHandler()
        logoutHandler.logout(
            VaadinServletRequest.getCurrent().httpServletRequest, null,
            null
        )
        val session = VaadinSession.getCurrent()

    }

    companion object {
        private const val LOGOUT_SUCCESS_URL = "/login"
    }
}