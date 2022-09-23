package com.example.kuzhlev.views

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("login")
@PageTitle("Login")
class Login() : VerticalLayout(),BeforeEnterObserver {
    private val login = LoginForm()
    init {
        addClassName("login-view")
        setSizeFull()
        justifyContentMode = JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
        login.action = "login"
        login.isForgotPasswordButtonVisible = false
        add(H1("Watch data"),login)

    }

    override fun beforeEnter(p0: BeforeEnterEvent) {
        if(p0.location
                .queryParameters
                .parameters
                .containsKey("error")) {
            login.isError = true;
        }
    }


}