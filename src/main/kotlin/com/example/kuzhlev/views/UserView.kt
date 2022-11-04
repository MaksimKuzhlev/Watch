package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.UserEntity
import com.example.kuzhlev.repositories.UserRepository
import com.example.kuzhlev.services.UserService
import com.example.kuzhlev.services.WatchService
import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Page
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession
import java.awt.SystemColor.text
import javax.annotation.security.RolesAllowed


@Route("user",layout = MainLayout::class)
@PageTitle("USER")
@RolesAllowed("ADMIN")
class UserView(private val service:UserService,userEntity: UserEntity,userRepository: UserRepository, val serv:WatchService):VerticalLayout() {
    private val grid:Grid<UserEntity> = Grid(UserEntity::class.java)
    private val form = CreateUserForm(service.findAllTokens(),userRepository,userEntity,service)
    private var thread: ApllServ.FeederThreadUser? = null
    private var page: Page
    val notification = Notification()
    val text = Div()
    init{
        setSizeFull()
        configNotification()
        confGrid()
        add(getToolBar(),getContent())
        form.setChangeHandler(object : CreateUserForm.ChangeHandler {
            override fun onChange() {
                form.isVisible = false
                updateList()
            }
        })
        updateList()
        page = UI.getCurrent().page
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
                "number",
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
        addContactButton.addClickListener {
            form.editPeople(UserEntity(0,"","","","","","","",true))
            form.isVisible = true
        }
        val toolBar = HorizontalLayout(addContactButton)
        toolBar.addClassName("toolbar")
        return toolBar
    }

    fun updateList(){
        grid.setItems(service.findAllUsers())
    }

    private fun configNotification(){
        notification.position = Notification.Position.TOP_END
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR)


        val layout = HorizontalLayout(text)
        layout.alignItems = FlexComponent.Alignment.CENTER
        notification.add(layout)

    }

    override fun onAttach(attachEvent: AttachEvent) {
        thread = ApllServ.FeederThreadUser(attachEvent.ui, this, page)
        thread!!.start()

    }


    override fun onDetach(detachEvent: DetachEvent) {
        notification.close()
        thread?.interrupt()
        thread = null

    }
}