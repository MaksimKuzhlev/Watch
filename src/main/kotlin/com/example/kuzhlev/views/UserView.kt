package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.repositories.UserRepository
import com.example.kuzhlev.services.UserService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed


@Route("user",layout = MainLayout::class)
@PageTitle("USER")
@RolesAllowed("ADMIN")
class UserView(private val service:UserService,userEntity: UserEntity,userRepository: UserRepository):VerticalLayout() {
    private val grid:Grid<UserEntity> = Grid(UserEntity::class.java)
    private val form = CreateUserForm(service.findAllTokens(),userRepository,userEntity,service)
    init{
        setSizeFull()
        confGrid()
        add(getToolBar(),getContent())
        form.setChangeHandler(object : CreateUserForm.ChangeHandler {
            override fun onChange() {
                form.isVisible = false
                updateList()
            }
        })
        updateList()
    }

    fun confGrid(){
        grid.run{
            setSizeFull()
            addClassName("user-grid")
            setColumns(
                "id",
                "firstName",
                "secondName",
                "token",
                "username",
                "password",
                "role",
                "active"
            )
            asSingleSelect()
                .addValueChangeListener {
                    form.editPeople(it.value)
                }
        }
    }





    private fun getContent(): Component {
        val content = HorizontalLayout(grid, form)
        content.setFlexGrow(2.0, grid)
        content.setFlexGrow(1.0, form)
        content.addClassNames("content")
        content.setSizeFull()
        return content
    }


    private fun getToolBar():HorizontalLayout{

        val addContactButton = Button("Add contact")
        addContactButton.addClickListener {form.isVisible = true
                form.editPeople(UserEntity(0,"","","","","","",true))
            }
        val toolBar = HorizontalLayout(addContactButton)
        toolBar.addClassName("toolbar")
        return toolBar
    }

    fun updateList(){
        grid.setItems(service.findAllUsers())
    }
}