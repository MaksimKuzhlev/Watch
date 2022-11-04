package com.example.kuzhlev.views

import com.example.kuzhlev.repositories.ActivityRepository
import com.example.kuzhlev.services.PatientService
import com.github.appreciated.apexcharts.ApexCharts
import com.github.appreciated.apexcharts.ApexChartsBuilder
import com.github.appreciated.apexcharts.config.builder.*
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder
import com.github.appreciated.apexcharts.config.datalables.builder.StyleBuilder
import com.github.appreciated.apexcharts.config.stroke.Curve
import com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder
import com.github.appreciated.apexcharts.helper.Series
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
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
import javax.annotation.security.RolesAllowed


@Route(value = "stat", layout = MainLayoutPeople::class)
@PageTitle("Statistics")
@RolesAllowed("ADMIN")
@CssImport(
    value = "./themes/mytheme/styles.css"
)
class StatsView(private val patientService: PatientService,private val activityRepository: ActivityRepository):VerticalLayout() {
    private val returnButton = Button("Список клиентов")
    private val mapSteps = mutableMapOf<String,Int>()
    private val mapKK = mutableMapOf<String,Int>()
    private val mapPulse = mutableMapOf<String,Int>()
    private val mapNight = mutableMapOf<String,Int>()
    private val list = mutableListOf<String>()
    private  var avatarBasic = Avatar()
    init {
        addClassName("mainLayout")
        setSizeFull()
        alignItems = FlexComponent.Alignment.END

        val title = H1("Статистика")
        title.style.set("margin-top","5px").set("margin-bottom","10px")

        val layout1 = HorizontalLayout(stepsCharts(),pulseChart())
        val layout2 = HorizontalLayout(kkCharts(),nightCharts())
        layout1.setSizeFull()
        layout2.setSizeFull()
        layout1.justifyContentMode = FlexComponent.JustifyContentMode.END
        layout2.justifyContentMode = FlexComponent.JustifyContentMode.END

        add(title,toolBar(),layout1,layout2)

    }

    private fun stepsCharts():Component{
        var res = 0
        if(patientService.tokenrq() == ""){
            for(i in 0..6)
                list.add("0")
                mapSteps["0"] = 0
        }else {
            activityRepository.findByToken(patientService.tokenrq()).forEach {
                mapSteps[it.date] = it.steps
                res += it.steps
                list.add(it.date)
            }
        }
        val layout = VerticalLayout()
        layout.setSizeFull()
        layout.add(title(1,res),createChart(mapSteps))
        layout.addClassName("objectBack")
        return layout
    }

    private fun kkCharts():Component{
        var res = 0
        if(patientService.tokenrq() == ""){
            mapKK["0"] = 0
        }else {
            activityRepository.findByToken(patientService.tokenrq()).forEach {
                mapKK[it.date] = it.kcal
                res += it.kcal
            }
        }
        val layout = VerticalLayout()
        layout.setSizeFull()
        layout.add(title(2,res),createChart(mapKK))
        layout.addClassName("objectBack")
        return layout
    }


    private fun pulseChart():Component{
        var res = 0
        if(patientService.tokenrq() == ""){
            mapPulse["0"] = 0
        }else {
            activityRepository.findByToken(patientService.tokenrq()).forEach {
                mapPulse[it.date] = it.pulse
                res += it.pulse
            }
        }
        val layout = VerticalLayout()
        layout.setSizeFull()
        layout.add(title(3,res),createChart(mapPulse))
        layout.addClassName("objectBack")
        return layout
    }

    private fun nightCharts():Component{
        var res = 0
        if(patientService.tokenrq() == ""){
            mapNight["0"] = 0
        }else {
            activityRepository.findByToken(patientService.tokenrq()).forEach {
                mapNight[it.date] = it.night_rise
                res += it.night_rise
            }
        }
        val layout = VerticalLayout()
        layout.setSizeFull()
        layout.add(title(4,res),createChart(mapNight))
        layout.addClassName("objectBack")
        return layout
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
        returnButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        returnButton.addClassNames("returnButton","textButton")
        returnButton.addClickListener { returnButton.ui.ifPresent { ui -> ui.navigate("") } }
        val fio = Text(patientService.namerq(patientService.tokenrq()))
        val layout3 = HorizontalLayout()
        layout3.add(fio,avatarBasic,returnButton)
        layout3.alignItems = FlexComponent.Alignment.CENTER
        return layout3
    }



    private fun title(check:Int,res:Int):Component{
        val layout = HorizontalLayout()
        layout.setWidthFull()
        if(check == 1){
            val title = Span("Ср. значение:${res/7} шага/день")
            val subTitle = Span(createIcon(VaadinIcon.QUESTION_CIRCLE_O),Text(" Шагов пройдено"))
            layout.add(title,subTitle)
        }
        if(check == 2){
            val title = Span("Ср. значение:${res/7} Ккал/день")
            val subTitle = Span(createIcon(VaadinIcon.QUESTION_CIRCLE_O),Text(" Ккал сожжено"))
            layout.add(title,subTitle)
        }
        if(check == 3){
            val title = Span("Ср. значение:${res/7} ударов/мин")
            val subTitle = Span(createIcon(VaadinIcon.QUESTION_CIRCLE_O),Text(" Показатель пульса"))
            layout.add(title,subTitle)
        }
        if(check == 4){
            val title = Span("Ср. значение:${res/7} раза/ночь")
            val subTitle = Span(createIcon(VaadinIcon.QUESTION_CIRCLE_O),Text(" Количество ночных пробуждений"))
            layout.add(title,subTitle)
        }



        layout.style.set("color","blue")
        layout.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        layout.justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        return layout
    }

    private fun createIcon(vaadinIcon: VaadinIcon):Component {
        val icon = vaadinIcon.create()
        icon.color = "blue"
        icon.style["padding"] = "var(--lumo-space-xs"
        return icon
    }




    private fun createChart(map:Map<String,Int>): ApexCharts {
        val lineChart = ApexChartsBuilder.get()
            .withChart(
                ChartBuilder.get()
                    .withType(Type.AREA)
                    .withZoom(
                        ZoomBuilder.get()
                            .withEnabled(false)
                            .build()
                    )
                    .build()
            )
            .withGrid(
                GridBuilder.get()
                    .withShow(false)
                    .build()
            )
            .withYaxis(
                YAxisBuilder.get()
                    .withShow(false)
                    .build()
            )
            .withStroke(
                StrokeBuilder.get()
                    .withCurve(Curve.SMOOTH)
                    .withWidth(2.0)
                    .withColors("Blue")
                    .build()
            )
            .withDataLabels(
                DataLabelsBuilder.get()
                    .withEnabled(true)
                    .withOffsetY(-10.0)
                    .withStyle(
                        StyleBuilder.get()
                            .withColors(listOf("blue"))
                            .build()
                    )
                    .build()
            )
            .withMarkers(
                MarkersBuilder.get()
                    .withSize(5.0,5.0)
                    .withColors(listOf("White"))
                    .withStrokeColor("Blue")
                    .build()
            )
            .withXaxis(
                XAxisBuilder.get()
                    .withCategories(list[0],list[1],list[2],list[3],list[4],list[5],list[6])
                    .withLabels(
                        LabelsBuilder.get()
                            .withStyle(
                                com.github.appreciated.apexcharts.config.xaxis.labels.builder.StyleBuilder.get()
                                    .withColors(listOf("#919191","#919191","#919191","#919191","#919191","#919191","#919191"))
                                    .build()

                            )
                            .build()
                    )
                    .build()
            )
            .withSeries(Series(map[list[0]],map[list[1]],map[list[2]],map[list[3]],map[list[4]],map[list[5]],map[list[6]]))
            .build()
        lineChart.setSizeFull()
        return lineChart
    }

}







