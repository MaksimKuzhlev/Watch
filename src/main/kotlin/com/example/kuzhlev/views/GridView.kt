package com.example.kuzhlev.views

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.WatchService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
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
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.SpringComponent
import javax.annotation.security.RolesAllowed

@Route(value = "watchData", layout = MainLayout::class)
@PageTitle("Watch")
@RolesAllowed("ADMIN")
class GridView(private val service: WatchService,
                watchEntity: WatchEntity,
                watchRepo:WatchRepository,
                positionRepository: PositionRepository):VerticalLayout(){
    private val grid: Grid<WatchEntity> = Grid(WatchEntity::class.java)
    private val form = CreateWatchForm(service,watchEntity, watchRepo,positionRepository)
    private val notifSos = Notification()
    private val text = Div()
    private val closeButton: Button = Button()
    private val sos:Sos =Sos(token = "0",sos = false)

    init {
        setSizeFull()
        configureGrid()
        configNotification()
        add(getToolBar(),getContent())
        form.setChangeHandler(object : CreateWatchForm.ChangeHandler {
            override fun onChange() {
                form.isVisible = false
                updateList()
            }
        })
        updateList()
        if (service.sosrq()){
            text.text = "SOS token ${service.tokrq()}"
            closeButton.text = "Resolved problem"
            notifSos.open()
        }

    }



    private fun configureGrid(){
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
                        "network_level"

                )
            columns.forEach{col->col.setAutoWidth(true)}
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
                form.checkPosition.isVisible = false
                form.checkMoving.isVisible = false
                form.isVisible = true

            }
            val toolBar = HorizontalLayout(addContactButton)
            toolBar.addClassName("toolbar")
            return toolBar
        }


    private  fun updateList(){
        grid.setItems(service.findAllWatches())
    }

    private fun configNotification(){
        notifSos.position = Notification.Position.TOP_END
        notifSos.addThemeVariants(NotificationVariant.LUMO_ERROR)
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)

        closeButton.element.setAttribute("aria-label", "Close")


        closeButton.addClickListener { notifSos.close()
            service.sos(sos)
        }

        val layout = VerticalLayout(text, closeButton)
        layout.alignItems = FlexComponent.Alignment.CENTER
        notifSos.add(layout)

    }





}