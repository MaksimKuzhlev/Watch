package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.*
import com.example.kuzhlev.repositories.*
import com.example.kuzhlev.security.SecurityService
import com.example.kuzhlev.services.ActivityService
import com.example.kuzhlev.services.PatientService
import com.example.kuzhlev.services.WatchService
import com.example.kuzhlev.views.InfoAboutPeople.DataDisease
import com.example.kuzhlev.views.InfoAboutPeople.InfoPeopleForm

import com.example.kuzhlev.views.InfoAboutPeople.TakingMedication
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import org.vaddon.CustomMediaQuery
import java.time.LocalDate
import javax.annotation.security.RolesAllowed


@Route(value = "info", layout = MainLayoutPeople::class)
@PageTitle("Info")
@RolesAllowed("ADMIN")
@CssImport(
    value = "./themes/mytheme/styles.css"
)
class Inform(
    private val patientService: PatientService,
    patientEntity: PatientEntity,
    medicateEntity: PatientMedicateEntity,
    private val patientRepository: PatientRepository,
    private val medicateRepository: MedicateRepository,
    private val watchRepository: WatchRepository,
    private val watchService: WatchService,
    private var watchEntity: WatchEntity,
    private val activityRepository: ActivityRepository,
    private var activityEntity: ActivityEntity,
    private var activityService: ActivityService,

    ):VerticalLayout() {

    private val infoPeople = InfoPeopleForm(patientService,patientEntity,patientRepository)
    private val medication = TakingMedication(patientService,medicateEntity,medicateRepository)
    private var divIP = Div()
    private var divMe = Div()
    private val canselButton = Button("отменить изменения")
    private val saveButton = Button("Сохранить изменения")
    private val deleteButton = Button("Удалить клиента")
    private val spanForReturn = Span(Text("Список клиентов"),createIcon(VaadinIcon.ARROW_RIGHT))
    private val returnButton = Button(spanForReturn)
    private val list = listOf("12.09","13.09","14.09","15.09","16.09","17.09","18.09")

    init {


        addClassName("mainLayout")
        val title = H1("Личные данные")
        title.addClassName("h1Title")
        title.style.set("margin-top","10px")
        title.style.set("margin-bottom","24px")
        if(patientService.checkrq()){
            remarks()
        }
        else{
            infoPeople.editPeople(PatientEntity(0,"","","","","","","",0,0,0, LocalDate.MIN))
        }
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        add(title,getButton(),getContent())
    }



    private fun getContent(): Component {
        divIP.add(infoPeople)
        divMe.add(medication)
        divIP.maxWidth = "657px"
        divMe.maxWidth = "884px"
        val content = HorizontalLayout(divMe,divIP)
        content.style.set("gap","var(--lumo-space-l)")
        content.setSizeFull()
        return  content
    }

    private fun getButton():Component{
        spanForReturn.addClassNames("spanLayout")
        canselButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        canselButton.addClassNames("returnButton","textButton")
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        saveButton.style.set("border-radius","8px")
        saveButton.addClassNames("textButton")
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        deleteButton.addClassNames("deleteButton","textButton")
        returnButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        returnButton.addClassNames("returnButton","textButton")

        canselButton.addClickShortcut(Key.ESCAPE)

        deleteButton.addClickListener { createDelDialog() }
        returnButton.addClickListener { returnButton.ui.ifPresent { ui -> ui.navigate("") } }
        canselButton.addClickListener { canselButton.ui.ifPresent { ui -> ui.page.reload() } }
        saveButton.addClickListener { createConfDialog() }

        val buttons = HorizontalLayout(deleteButton,canselButton,saveButton,returnButton)
        buttons.alignItems = FlexComponent.Alignment.END
        return buttons
    }

    private fun remarks(){
        infoPeople.editPeople(patientRepository.findById(patientService.idrq()).get())
        medicateRepository.findByToken(patientService.tokenrq()).forEach {medication.editPeople(it)}
    }


    private fun createDelDialog(){
        val dialog= Dialog()
        val layout = HorizontalLayout()
        val canselButton = Button("Нет, не удалять")
        val confirmButton = Button("Да, удалить")
        canselButton.setWidthFull()
        confirmButton.setWidthFull()
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        canselButton.addClickListener { dialog.close() }
        confirmButton.addClickListener {
            patientService.deletePeople(patientService.tokenrq())
            activityRepository.findByToken(patientService.tokenrq()).forEach { activityService.delete(it) }
            watchRepository.findByToken(patientService.tokenrq())?.let { it1 -> watchService.delete(it1) }
            deleteButton.ui.ifPresent{ui->ui.navigate("")}
        }
        dialog.headerTitle = "Удалить профиль пользователя?"
        layout.add(canselButton,confirmButton)
        dialog.add(layout)
        dialog.open()
    }

    private fun createConfDialog(){
        val dialog= Dialog()
        val layout = HorizontalLayout()
        val canselButton = Button("Нет, не сохранять")
        val confirmButton = Button("Да, сохранить")
        canselButton.setWidthFull()
        confirmButton.setWidthFull()
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        canselButton.addClickListener { dialog.close() }
        confirmButton.addClickListener {
            infoPeople.save()
            medication.save(infoPeople.token())
            if(!patientService.checkrq()){
                patientService.saveDiseasePeople(PatientDiseaseEntity(0,infoPeople.token(),false,false,false,false,null))
                watchEntity.id = 0
                watchEntity.name = infoPeople.name()
                watchEntity.token = infoPeople.token()
                watchEntity.charge_level = 0
                watchEntity.latitude = 0.0
                watchEntity.longitude = 0.0
                watchEntity.has_fallen = false
                watchEntity.heart_rate = 0
                watchEntity.masturbate = false
                watchEntity.network_level = 0
                watchService.save(watchEntity)
                for(i in 0..6){
                    activityEntity.id = 0
                    activityEntity.date = list[i]
                    activityEntity.token = infoPeople.token()
                    activityEntity.kcal = 0
                    activityEntity.pulse = 0
                    activityEntity.night_rise = 0
                    activityEntity.steps = 0
                    activityService.save(activityEntity)
                }
            }
            dialog.close()
            saveButton.ui.ifPresent{ui->ui.navigate("")}
        }
        dialog.headerTitle = "Сохранить изменения перед выходом?"
        layout.add(canselButton,confirmButton)
        dialog.add(layout)
        dialog.open()
    }

    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.addClassNames("leftStr")
        icon.color = "black"
        icon.style["padding"] = "var(--lumo-space-xs"
        return icon
    }
}