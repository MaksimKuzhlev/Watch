package com.example.kuzhlev.views

import com.example.kuzhlev.DTO.CheckUpdate
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.PatientService
import com.example.kuzhlev.services.WatchService
import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Page
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.LitRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.function.SerializableBiConsumer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
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
    private val watchEntity: WatchEntity,
    private val watchRepo: WatchRepository,
    positionRepository: PositionRepository,
    private val patientService: PatientService

):VerticalLayout() {
    val grid: Grid<WatchEntity> = Grid(WatchEntity::class.java,false)
    val form = CreateWatchForm(service,watchEntity, watchRepo,positionRepository)
    val notifSos = Notification()
    val text = Div()
    private var thread: ApllServ.FeederThread? = null
    private var threadGrid: ApllServ.FeederThread2? = null
    private val addContactButton = Button("Добавить клиента")
    val textField = TextField()
    private val buttonChange = Button()
    private var page: Page
    var check = 0
    private var allWatches = listOf<WatchEntity>()



    init {

        val title = H1("Список клиентов")
        title.addClassName("h1Title")
        title.style.set("margin-top","10px")
        title.style.set("margin-bottom","24px")
        style.set("background","#F5F7F8")
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        configureGrid()
        configNotification()
        add(title,getToolBar(),grid)


        updateList(textField.value)
        page = UI.getCurrent().page

    }




    private fun configureGrid(){
        grid.run{
            addClassName("contact-grid")
            setSizeFull()

            addColumn(ComponentRenderer({ Span() }) { span: Span, person: WatchEntity ->
                span.add(Icon("lumo","cross"))
                span.style.set("color","red")
                span.addClickListener { notifSos.open() }
            }).isSortable = false


            addColumn(ComponentRenderer({ Span() }) { span: Span, person: WatchEntity ->
                if(person.charge_level in 61..100) {
                    span.add(createIcon(VaadinIcon.PROGRESSBAR,2),Span(person.charge_level.toString()+"%"))
                    span.style.set("color", "green")
                }
                if(person.charge_level in 21..60) {
                    span.add(createIcon(VaadinIcon.PROGRESSBAR,2),Span(person.charge_level.toString()+"%"))
                    span.style.set("color", "yellow")
                }
                if(person.charge_level in 0..20){
                    span.add(createIcon(VaadinIcon.PROGRESSBAR,2),Span(person.charge_level.toString()+"%"))
                    span.style.set("color", "red")
                }
            }).setHeader("charge_level")


            addColumn(ComponentRenderer({ Span() }) { span: Span, person: WatchEntity ->
                span.add(createIcon(VaadinIcon.SIGNAL,3),Span(person.network_level.toString()))
            }).setHeader("network_level")


            addColumn(ComponentRenderer({ Span() }) { span: Span, person: WatchEntity ->
                    span.add(createIcon(VaadinIcon.HEART,1),Span(person.heart_rate.toString()))
                    span.style.set("justify-content","center")
                    if(person.heart_rate > 110 && patientService.tachycardiaCheck(person.token))
                        span.style.set("color","red")
                    if(person.heart_rate > 90 && !patientService.tachycardiaCheck(person.token))
                        span.style.set("color","red")
                    if(person.heart_rate in 60..90 && !patientService.tachycardiaCheck(person.token) && !patientService.bradicardiaCheck(person.token))
                        span.style.set("color","black")
                    if(person.heart_rate in 90..110 && patientService.tachycardiaCheck(person.token))
                        span.style.set("color","black")
                    if(person.heart_rate < 90 && patientService.tachycardiaCheck(person.token))
                        span.style.set("color","red")
                    if(person.heart_rate in 40..70 && patientService.bradicardiaCheck(person.token))
                        span.style.set("color","black")
                    if(person.heart_rate < 60 && !patientService.bradicardiaCheck(person.token))
                        span.style.set("color","red")
                    if(person.heart_rate < 40 && patientService.bradicardiaCheck(person.token))
                        span.style.set("color","red")
                    if(person.heart_rate > 70 && patientService.bradicardiaCheck(person.token))
                        span.style.set("color","red")

                }).setHeader("heart_rate")


            addColumn(ComponentRenderer({ Span() }){ span:Span, person:WatchEntity ->
                if (service.sosrq()) {
                    span.add("ㅤSOSㅤ")
                    span.style.set("background","red").set("border-radius","24px").set("color","white")
                    span.addClassNames("blinkTime","blink")
                }else{
                    span.removeAll()
                    span.style.set("background","white")
                }
            }).setHeader("Sos").setKey("Sos")


            addColumn(ComponentRenderer({Span()}) { span:Span, person: WatchEntity ->
                span.add(person.has_fallen.toString())
                if(person.has_fallen)
                    span.style.set("color","red")
                else
                    span.style.set("color","black")
            }).setHeader("has_fallen")


            addColumn(WatchEntity::name).setHeader("name").isSortable = false
            addColumn(WatchEntity::token).setHeader("token").isSortable = false
            addColumn(WatchEntity::id).setHeader("id").isSortable = false




            style.set("background","#F5F7F8")
            addThemeVariants(GridVariant.LUMO_NO_BORDER)


            columns.forEach{col->col.setAutoWidth(true)}
            asSingleSelect()
                .addValueChangeListener {
                    val check=CheckUpdate(true,it.value.id,it.value.token)
                    patientService.check(check)
                    ui.ifPresent{ui -> ui.navigate("info")}
                }
        }



    }

    private fun createIcon(vaadinIcon: VaadinIcon,check:Int):Component {
        val icon = vaadinIcon.create()
        if (check==1||check==2){
            icon.style["padding"] = "var(--lumo-space-xs"
        }else{
            icon.color = "black"
            icon.style["padding"] = "var(--lumo-space-xs"
        }
        return icon
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
        addContactButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        addContactButton.width = "203px"
        addContactButton.addClickListener {
            val check=CheckUpdate(false,0,"")
            patientService.check(check)
            addContactButton.ui.ifPresent{ui -> ui.navigate("info")}

        }
        textField.width = "430px"
        textField.valueChangeMode = ValueChangeMode.EAGER
        textField.suffixComponent = VaadinIcon.SEARCH.create()
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT)
        textField.addValueChangeListener {
            updateList(textField.value)
        }
        textField.placeholder = "Поиск клиента"

        val toolBar = HorizontalLayout(addContactButton,buttonChange,textField)
        toolBar.addClassName("toolbar")
        return toolBar
    }


    fun updateList(name:String){
        if (name.isEmpty())
            grid.setItems(service.findAllWatches())
        else
            grid.setItems(watchRepo.findByName(name))
    }

     private fun configNotification(){
        notifSos.position = Notification.Position.TOP_END
        notifSos.addThemeVariants(NotificationVariant.LUMO_ERROR)

        val layout = HorizontalLayout(text)
        layout.alignItems = FlexComponent.Alignment.CENTER
        notifSos.add(layout)

    }



    private fun mainContent() {
        allWatches = service.findAllWatches()
        allWatches.forEach {
            val layout =HorizontalLayout()
            layout.justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
            val content = Div()
            content.style.set("border-radius","8px").set("background","white")
            content.height = "59px"
            layout.add(textLayout(it.charge_level.toString()))
            layout.add(textLayout(it.network_level.toString()))
            layout.add(textLayout(it.heart_rate.toString()))
            layout.add(textLayout(it.has_fallen.toString()))
            layout.add(textLayout(it.name))
            layout.add(textLayout(it.token))
            layout.add(textLayout(it.id.toString()))
            layout.setSizeFull()
            content.add(layout)
            content.setWidthFull()
            add(content)
        }


    }

    private fun textLayout(textCell:String):Component{
        val text = Span(textCell)
        text.style.set("padding-left","28.75").set("padding-right","28.75").set("display","flex").set("flex-direction","row").set("align-items","center")
        return text
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


