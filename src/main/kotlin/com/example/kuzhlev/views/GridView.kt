package com.example.kuzhlev.views

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.WatchService
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import javax.annotation.security.RolesAllowed


@Route(value = "", layout = MainLayout::class)
@PageTitle("Watch")
@RolesAllowed("ADMIN")
@CssImport(
    themeFor = "vaadin-grid",
    value = "./themes/mytheme/components/dynamic-grid-cell-background-color.css"
)
class GridView(
     val service: WatchService,
    watchEntity: WatchEntity,
    watchRepo: WatchRepository,
    positionRepository: PositionRepository,
):VerticalLayout() {
    val grid: Grid<WatchEntity> = Grid(WatchEntity::class.java)
    private val form = CreateWatchForm(service,watchEntity, watchRepo,positionRepository)
    val notifSos = Notification()
    val text = Div()
    val closeButton: Button = Button()
    private val sos:Sos =Sos(token = "0",sos = false)
    private var thread: ApllServ.FeederThread? = null


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

    }




    private fun configureGrid(){
        grid.run{
            addClassName("contact-grid")
            setSizeFull()
            setColumns("id",
                        "token",
                        "longitude",
                        "latitude",
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
                form.editPeople(WatchEntity(0,"",0.0,0.0,false, 0,false,0,0))
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
            grid.setClassNameGenerator { null }
        }

        val layout = VerticalLayout(text, closeButton)
        layout.alignItems = FlexComponent.Alignment.CENTER
        notifSos.add(layout)

    }
    override fun onAttach(attachEvent: AttachEvent) {
        thread = ApllServ.FeederThread(attachEvent.ui, this)
        thread!!.start()
    }

    override fun onDetach(detachEvent: DetachEvent) {
        thread?.interrupt()
        thread = null

    }

}


@Push
class ApllServ():SpringBootServletInitializer(),AppShellConfigurator {

     class FeederThread(private val ui: UI, private val view: GridView) : Thread() {
        private val l = 0
        override fun run() {
            while (l != 1) {
                println("Scan")
                if (view.service.sosrq()) {
                    println("Попал")
                    ui.access {
                        println("Выполняю")
                        view.text.text = "SOS token ${view.service.tokrq()}"
                        view.closeButton.text = "Resolved problem"
                        view.notifSos.open()
                        view.grid.setClassNameGenerator {
                            if (it.token == view.service.tokrq())
                                "warn"
                            else
                                null
                        }
                    }


                }
                sleep(5000)
            }

        }

    }
}