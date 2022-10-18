package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.services.PatientService
import com.example.kuzhlev.views.InfoAboutPeople.DataDisease
import com.example.kuzhlev.views.InfoAboutPeople.InfoPeopleForm
import com.example.kuzhlev.views.InfoAboutPeople.PhysicalPeopleForm
import com.example.kuzhlev.views.InfoAboutPeople.TakingMedication
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@Route(value = "info")
@PageTitle("Info")
@RolesAllowed("ADMIN")
@CssImport(
    value = "./themes/mytheme/styles.css"
)
class Inform(private val patientService: PatientService,private val patientEntity: PatientEntity):VerticalLayout() {

    private val infoPeople = InfoPeopleForm(patientService,patientEntity)
    private val physic = PhysicalPeopleForm()
    private val disease = DataDisease()
    private val medication = TakingMedication()
    private var divIP = Div()
    private var divPh = Div()
    private var divMe = Div()
    private val canselButton = Button("отменить изменения")
    private val saveButton = Button("Сохранить изменения")
    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        add(H1("Информация о клиенте"),getContent())
    }

    private fun getContent(): Component {
        val layout = VerticalLayout(physic,disease)
        layout.setFlexGrow(1.0,physic)
        layout.setFlexGrow(2.0,disease)
        layout.style.set("padding","0")
        divIP.add(infoPeople)
        divPh.add(layout)
        divMe.add(medication,getButton())


        val content = HorizontalLayout(divMe,divPh,divIP)
        content.setFlexGrow(2.0,divMe)
        content.setFlexGrow(1.0,divPh)
        content.setFlexGrow(1.0,divIP)
        content.setSizeFull()
        return  content
    }

    private fun getButton():Component{
        canselButton.setWidthFull()
        saveButton.setWidthFull()
        canselButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        canselButton.addClickShortcut(Key.ESCAPE)

        canselButton.addClickListener { canselButton.ui.ifPresent{ui -> ui.navigate("user")} }
        saveButton.addClickListener {
            infoPeople.save()
            saveButton.ui.ifPresent{ui->ui.navigate("")}
        }

        val buttons = HorizontalLayout(canselButton,saveButton)
        buttons.setWidthFull()
        return buttons
    }
}