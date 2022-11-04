package com.example.kuzhlev.views

import com.example.kuzhlev.repositories.PatientRepository
import com.example.kuzhlev.services.PatientService
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import javax.annotation.security.RolesAllowed

@Route(value = "map", layout = MainLayoutPeople::class)
@PageTitle("Map")
@RolesAllowed("ADMIN")
class MapView(private val patientService: PatientService,private val patientRepository: PatientRepository):VerticalLayout() {
    private val returnButton = Button("Список клиентов")
    private val gmaps = GoogleMap("AIzaSyC7Q3-PD4pYBdAlDx9O_gxFAEUewPVGOeE", null,null)
    private val fio = Text("")
    private  var avatarBasic = Avatar()

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        val title = H1("Карта")
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
        fio.text = patientRepository.findByToken(patientService.tokenrq()).fio

        toolBar.add(fio,returnButton)
        return toolBar
    }

    private fun mainArea():Component{
        val mainArea= HorizontalLayout()

        gmaps.mapType = GoogleMap.MapType.ROADMAP
        gmaps.width = "1100px"
        gmaps.height = "700px"

        mainArea.add(gmaps)


        return mainArea
    }

}