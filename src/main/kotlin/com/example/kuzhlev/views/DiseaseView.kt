package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.PatientDiseaseEntity
import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.repositories.DiseaseRepository
import com.example.kuzhlev.services.PatientService
import com.example.kuzhlev.views.InfoAboutPeople.DataDisease
import com.example.kuzhlev.views.InfoAboutPeople.DataMajorDisease
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.time.LocalDate
import javax.annotation.security.RolesAllowed

@Route(value = "disease", layout = MainLayoutPeople::class)
@PageTitle("Diseases")
@RolesAllowed("ADMIN")
class DiseaseView(private val patientService: PatientService, private val diseaseEntity: PatientDiseaseEntity,private val diseaseRepository: DiseaseRepository):VerticalLayout() {
    private val spanForReturn = Span(Text("Список клиентов"),createIcon(VaadinIcon.ARROW_RIGHT))
    private val returnButton = Button(spanForReturn)
    private val saveButton = Button("Сохранить изменения")
    private  var avatarBasic = Avatar()
    private val otherDisease = DataDisease(patientService,diseaseEntity,diseaseRepository)
    private val mainDisease = DataMajorDisease(patientService,diseaseEntity,diseaseRepository)
    private var divOD = Div()
    private val divMD = Div()
    init {
        if(patientService.checkrq()){
            otherDisease.editPeople(diseaseRepository.findByToken(patientService.tokenrq()))
            mainDisease.editPeople(diseaseRepository.findByToken(patientService.tokenrq()))
        }
        addClassName("mainLayout")

        val text = H1("Болезни")
        text.addClassName("h1Title")
        text.style.set("margin-top","10px")
        text.style.set("margin-bottom","8px")

        setSizeFull()
        alignItems = FlexComponent.Alignment.END

        add(text,toolBar(),mainArea())
    }

    private fun toolBar():Component{
        spanForReturn.addClassNames("spanLayout")

        if(patientService.tokenrq()!="")
        {
            val imageResource = StreamResource("${patientService.tokenrq()}.png",
                InputStreamFactory {
                    javaClass.getResourceAsStream("/images/${patientService.tokenrq()}.png")
                })
            avatarBasic.imageResource = imageResource
        }

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        saveButton.style.set("border-radius","8px")
        saveButton.addClassNames("textButton")
        saveButton.addClickListener { createConfDialog() }


        returnButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        returnButton.addClassNames("returnButton","textButton")
        returnButton.addClickListener { returnButton.ui.ifPresent { ui -> ui.navigate("") } }

        val fio = Text(patientService.namerq(patientService.tokenrq()))
        val layout = HorizontalLayout()
        layout.add(fio,avatarBasic,saveButton,returnButton)
        layout.alignItems = FlexComponent.Alignment.CENTER

        return layout
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
            if(patientService.tokenrq()!=""){
                diseaseEntity.id = diseaseRepository.findByToken(patientService.tokenrq())!!.id
                diseaseEntity.token = patientService.tokenrq()
                diseaseEntity.disease = otherDisease.save(diseaseEntity.token)
                diseaseEntity.stroke = mainDisease.save()["Инсульт"] == true
                diseaseEntity.bradycardia = mainDisease.save()["Брахикордия"] == true
                diseaseEntity.tachycardia = mainDisease.save()["Тахикордия"] == true
                diseaseEntity.arrhythmia = mainDisease.save()["Аритмия"] == true
                patientService.saveDiseasePeople(diseaseEntity)
                dialog.close()
                saveButton.ui.ifPresent { ui -> ui.page.reload()}
            }
            else{
                TODO("Notification")
            }
        }
        dialog.headerTitle = "Сохранить изменения перед выходом?"
        layout.add(canselButton,confirmButton)
        dialog.add(layout)
        dialog.open()
    }


    private fun mainArea():Component{
        divOD.add(otherDisease)
        divMD.add(mainDisease)
        val layout = HorizontalLayout(divOD,divMD)

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