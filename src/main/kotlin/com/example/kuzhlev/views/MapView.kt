package com.example.kuzhlev.views

import com.example.kuzhlev.repositories.PatientRepository
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.services.PatientService
import com.flowingcode.addons.ycalendar.InlineDatePicker
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap
import com.flowingcode.vaadin.addons.googlemaps.LatLon
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
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
import java.time.DayOfWeek

@Route(value = "map", layout = MainLayoutPeople::class)
@PageTitle("Map")
@RolesAllowed("ADMIN")
@CssImport(
    themeFor = "google-map",
    value = "./themes/mytheme/components/google-map.css"

)
class MapView(private val patientService: PatientService,private val patientRepository: PatientRepository,private val positionRepository: PositionRepository):VerticalLayout() {
    private val spanForReturn = Span(Text("Список клиентов"),createIcon(VaadinIcon.ARROW_RIGHT))
    private val returnButton = Button(spanForReturn)
    private val gmaps = GoogleMap("AIzaSyC7Q3-PD4pYBdAlDx9O_gxFAEUewPVGOeE", null,null)
    private val fio = Text("")
    private  var avatarBasic = Avatar()
    private val calendar = InlineDatePicker()


    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        addClassName("mainLayout")
        val title = H1("Карта")
        title.addClassName("h1Title")
        title.style.set("margin-top","5px").set("margin-bottom","10px")
        add(title,toolBar(),mainArea())

    }


    private fun toolBar():Component{
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
        gmaps.mapType = GoogleMap.MapType.ROADMAP
        gmaps.width = "1100px"
        gmaps.height = "700px"

        mainArea.add(calendarLayout(),gmaps)




        return mainArea
    }

    private fun calendarLayout():Component{
        calendar.addValueChangeListener {
            gmaps.element.removeAllChildren()
            val positions = positionRepository.findByToken(patientService.tokenrq())
            if(positions.isNotEmpty()) {
                if (positions.lastIndex > 5)
                    positions.subList(0, 5)
                positions.forEach {
                    if(it.date==calendar.value){
                        gmaps.addMarker("Center", LatLon(it.lat, it.lon), false, "")
                        gmaps.center = LatLon(positions.last().lat, positions.last().lon)
                    }
                }
            }
        }
        calendar.setSizeFull()

        val divCal=Div()
        divCal.add(calendar)
        divCal.setSizeFull()
        divCal.addClassName("objectBack")

        val layout = VerticalLayout()

        val title = H2("Дата")
        title.addClassNames("h2Title")
        title.style.set("margin-top","0").set("margin-bottom","4px")

        layout.add(title,divCal)
        layout.style.set("padding-top","0")
        layout.alignItems = FlexComponent.Alignment.END
        layout.width = "430px"
        layout.height = "372px"


        return layout
    }


    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.addClassNames("leftStr")
        icon.color = "black"
        icon.style["padding"] = "var(--lumo-space-xs"
        return icon
    }
}