package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.UserRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.security.WebSecurityConfig
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import javax.annotation.security.RolesAllowed

@Route(value = "personalCab",layout = MainLayout::class)
@PageTitle("Personal cabinet")
@RolesAllowed("USER","ADMIN")
class PersonalView (private val webSecurityConfig: WebSecurityConfig,private val userRepository: UserRepository,private val watchRepository: WatchRepository):VerticalLayout(){
    private val grid = Grid(WatchEntity::class.java)
    private val auth: Authentication = SecurityContextHolder.getContext().authentication
    private val username = webSecurityConfig.userDetailsServiceBean().loadUserByUsername(auth.name).username
    private val token = userRepository.findByUsername(username)?.token
    init{
        setSizeFull()
        configGrid()
        add(H1("It's your Personal cabinet"),grid)
        if(token!=null){
        updateList()
        }
    }

    private fun configGrid(){
        grid.run{
            addClassName("contact-grid")
            setSizeFull()
            setColumns("id",
                "token",
                "longitude",
                "latitude",
                "masturbate",
                "heart_rate",
                "has_fallen",
                "charge_level",
                "network_level")
        }
    }

    private fun updateList(){
        grid.setItems(watchRepository.findByToken(token!!))
    }
}