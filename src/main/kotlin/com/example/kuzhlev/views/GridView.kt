package com.example.kuzhlev.views

import com.example.kuzhlev.DTO.CheckUpdate
import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.entitys.*
import com.example.kuzhlev.repositories.*
import com.example.kuzhlev.services.PatientService
import com.example.kuzhlev.services.WatchService
import com.example.kuzhlev.views.InfoAboutPeople.DataDisease
import com.example.kuzhlev.views.InfoAboutPeople.InfoPeopleForm
import com.example.kuzhlev.views.InfoAboutPeople.PhysicalPeopleForm
import com.example.kuzhlev.views.InfoAboutPeople.TakingMedication
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Page
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import java.time.LocalDate
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
    val patientService: PatientService

):VerticalLayout() {
    val grid: Grid<WatchEntity> = Grid(WatchEntity::class.java)
    val form = CreateWatchForm(service,watchEntity, watchRepo,positionRepository)
    val notifSos = Notification()
    val text = Div()
    private var thread: ApllServ.FeederThread? = null
    private var threadGrid: ApllServ.FeederThread2? = null
    private var page: Page
    var check = 0
    init {
        setSizeFull()
        configureGrid()
        configNotification()
        add(getToolBar(),getContent())
        form.setChangeHandler(object : CreateWatchForm.ChangeHandler {
            override fun onChange() {
                form.isVisible = false
                updateList()
                notifSos.close()
            }
        })

        updateList()
        page = UI.getCurrent().page

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
                    val check=CheckUpdate(true,it.value.id,it.value.token)
                    patientService.check(check)
                    ui.ifPresent{ui -> ui.navigate("info")}
                    //form.editPeople(it.value)
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
                val check=CheckUpdate(false,0,"")
                patientService.check(check)
                addContactButton.ui.ifPresent{ui -> ui.navigate("info")}
                //form.editPeople(WatchEntity(0,"2",0.0,0.0,false, 0,false,0,0,"Дамир Раджабович"))
                //form.checkPosition.isVisible = false
                //form.checkMoving.isVisible = false
                //form.butResolved.isVisible = false
                //form.isVisible = true

            }
            val toolBar = HorizontalLayout(addContactButton)
            toolBar.addClassName("toolbar")
            return toolBar
        }


    fun updateList(){
        grid.setItems(service.findAllWatches())
    }

     private fun configNotification(){
        notifSos.position = Notification.Position.TOP_END
        notifSos.addThemeVariants(NotificationVariant.LUMO_ERROR)

        val layout = HorizontalLayout(text)
        layout.alignItems = FlexComponent.Alignment.CENTER
        notifSos.add(layout)

    }
    override fun onAttach(attachEvent: AttachEvent) {

        thread = ApllServ.FeederThread(attachEvent.ui, this, page)
        threadGrid = ApllServ.FeederThread2(attachEvent.ui,this, page)
        thread!!.start()
        threadGrid!!.start()
    }


    override fun onDetach(detachEvent: DetachEvent) {
        notifSos.close()
        threadGrid?.interrupt()
        thread?.interrupt()
        thread = null
        threadGrid = null

    }
}


