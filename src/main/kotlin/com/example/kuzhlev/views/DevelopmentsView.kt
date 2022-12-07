package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.PositionHistoryEntity
import com.example.kuzhlev.services.PatientService
import com.flowingcode.addons.ycalendar.InlineDatePicker
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import javax.annotation.security.RolesAllowed


@CssImport(
    themeFor = "vaadin-month-calendar",
    value = "./themes/mytheme/components/vaadin-month-calendar.css"

)
@Route(value = "dev", layout = MainLayoutPeople::class)
@PageTitle("Developments")
@RolesAllowed("ADMIN")
class DevelopmentsView(private val patientService: PatientService,private val positionHistoryEntity: PositionHistoryEntity): VerticalLayout() {

    private val spanForReturn = Span(Text("Список клиентов"),createIcon(VaadinIcon.ARROW_RIGHT))
    private val returnButton = Button(spanForReturn)
    private  var avatarBasic = Avatar()
    private val calendar = InlineDatePicker()
    private val fio = Text("")
    private val grid:Grid<PositionHistoryEntity> = Grid(PositionHistoryEntity::class.java)

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        addClassName("mainLayout")
        val title = H1("События")
        title.addClassName("h1Title")
        title.style.set("margin-top","5px").set("margin-bottom","10px")
        add(title,toolBar(),mainArea())
    }


    private fun toolBar(): Component {
        if(patientService.tokenrq()!="")
        {
            val imageResource = StreamResource("${patientService.tokenrq()}.png",
                InputStreamFactory {
                    javaClass.getResourceAsStream("/images/${patientService.tokenrq()}.png")
                })
            avatarBasic.imageResource = imageResource
        }
        val toolBar = HorizontalLayout()
        toolBar.alignItems = FlexComponent.Alignment.CENTER
        returnButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        returnButton.addClassNames("returnButton","textButton")
        returnButton.addClickListener { returnButton.ui.ifPresent { ui -> ui.navigate("") } }
        fio.text = patientService.namerq(patientService.tokenrq())

        toolBar.add(fio,avatarBasic,returnButton)
        return toolBar
    }


    private fun mainArea():Component{
        val mainArea= HorizontalLayout()
        mainArea.add(calendarLayout(),grid)

        return mainArea
    }


    private fun calendarLayout():Component{
        val layoutChoosing = VerticalLayout()
        val buttonCharge = Button("Низкий уровень заряда")
        val buttonFall = Button("Падение")
        val buttonSos = Button("SOS")
        val buttonSignal = Button("Низкий уровень сигнала")
        val buttonPulse = Button("Отклонения пульса")

        layoutChoosing.alignItems = FlexComponent.Alignment.END

        buttonCharge.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        buttonFall.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        buttonSos.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        buttonSignal.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        buttonPulse.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        buttonCharge.addClickListener {
            buttonCharge.removeThemeVariants(ButtonVariant.LUMO_TERTIARY)
            buttonCharge.addThemeVariants(ButtonVariant.LUMO_PRIMARY) }
        buttonFall.addClickListener { buttonFall.addClassName("choose") }
        buttonSos.addClickListener { buttonSos.addClassName("choose") }
        buttonSignal.addClickListener { buttonSignal.addClassName("choose") }
        buttonPulse.addClickListener {  buttonPulse.addClassName("choose") }

        val layoutButton1 = HorizontalLayout(buttonCharge,buttonFall,buttonSos)
        val layoutButton2 = HorizontalLayout(buttonSignal,buttonPulse)

        layoutButton1.setWidthFull()
        layoutButton2.setWidthFull()

        calendar.addValueChangeListener {}
        calendar.setSizeFull()

        val divCal=Div()
        divCal.add(calendar)
        divCal.setSizeFull()
        divCal.addClassName("objectBack")

        val layoutCal = VerticalLayout()

        val titleCal = H2("Дата")
        titleCal.addClassNames("h2Title")
        titleCal.style.set("margin-top","0").set("margin-bottom","4px")

        val titleBut = H2("Тип События")
        titleBut.addClassNames("h2Title")
        titleBut.style.set("margin-top","16px").set("margin-bottom","12px")

        layoutCal.add(titleCal,divCal)
        layoutCal.style.set("padding-top","0")
        layoutCal.alignItems = FlexComponent.Alignment.END
        layoutCal.width = "430px"
        layoutCal.height = "372px"

        layoutChoosing.add(layoutCal,titleBut,layoutButton1,layoutButton2)


        return layoutChoosing
    }



    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.addClassNames("leftStr")
        icon.color = "black"
        icon.style["padding"] = "var(--lumo-space-xs"
        return icon
    }

}