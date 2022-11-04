package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.*
import com.example.kuzhlev.repositories.*
import com.example.kuzhlev.services.ActivityService
import com.example.kuzhlev.services.PatientService
import com.example.kuzhlev.services.WatchService
import com.example.kuzhlev.views.InfoAboutPeople.DataDisease
import com.example.kuzhlev.views.InfoAboutPeople.InfoPeopleForm
import com.example.kuzhlev.views.InfoAboutPeople.PhysicalPeopleForm
import com.example.kuzhlev.views.InfoAboutPeople.TakingMedication
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
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
    physicalEntity: PatientPhysicalEntity,
    diseaseEntity:PatientDiseaseEntity,
    medicateEntity: PatientMedicateEntity,
    private val patientRepository: PatientRepository,
    private val physicalRepository: PhysicalRepository,
    private val diseaseRepository: DiseaseRepository,
    private val medicateRepository: MedicateRepository,
    private val watchRepository: WatchRepository,
    private val watchService: WatchService,
    private var watchEntity: WatchEntity,
    private val activityRepository: ActivityRepository,
    private var activityEntity: ActivityEntity,
    private var activityService: ActivityService
    ):VerticalLayout() {

    private val infoPeople = InfoPeopleForm(patientService,patientEntity,patientRepository)
    private val physic = PhysicalPeopleForm(patientService,physicalEntity,physicalRepository)
    private val disease = DataDisease(patientService,diseaseEntity,diseaseRepository)
    private val medication = TakingMedication(patientService,medicateEntity,medicateRepository)
    private var divIP = Div()
    private var divPh = Div()
    private var divMe = Div()
    private var divAll= Div()
    private val canselButton = Button("отменить изменения")
    private val saveButton = Button("Сохранить изменения")
    private val deleteButton = Button("Удалить клиента")
    private val returnButton = Button("Список клиентов")
    private val list = listOf("12.09","13.09","14.09","15.09","16.09","17.09","18.09")

    init {
        addClassName("mainLayout")
        val title = H1("Информация о клиенте")
        title.style.set("margin-top","10px")

        if(patientService.checkrq()){
            remarks()
        }
        else{
            infoPeople.editPeople(PatientEntity(0,"","","","","","",""))
            physic.editPeople(PatientPhysicalEntity(0,"",0,0,0, LocalDate.MIN))
            disease.editPeople(PatientDiseaseEntity(0,"",false,false,false,false,null))
        }
        setSizeFull()
        alignItems = FlexComponent.Alignment.END
        add(title,getButton(),getContent())
    }

    private fun getContent(): Component {
        val layout = VerticalLayout(physic,disease)
        layout.style.set("padding","0")
        divIP.add(infoPeople)
        divPh.add(layout)
        divMe.add(medication)
        divIP.maxWidth = "430px"
        divPh.maxWidth = "430px"
        divMe.maxWidth = "700px"
        val content = HorizontalLayout(divMe,divPh,divIP)
        content.style.set("gap","var(--lumo-space-l)")
        content.setSizeFull()
        content.justifyContentMode = FlexComponent.JustifyContentMode.END
        return  content
    }

    private fun getButton():Component{

        canselButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        canselButton.addClassName("returnButton")
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        deleteButton.style.set("color","red")
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
        physic.editPeople(physicalRepository.findByToken(patientService.tokenrq()))
        disease.editPeople(diseaseRepository.findByToken(patientService.tokenrq()))
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
            physic.save(infoPeople.token())
            disease.save(infoPeople.token())
            medication.save(infoPeople.token())
            if(!patientService.checkrq()){
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


}