package com.example.kuzhlev.views

import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.WatchService
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap
import com.flowingcode.vaadin.addons.googlemaps.LatLon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.server.VaadinSession
import com.vaadin.flow.server.WrappedHttpSession
import com.vaadin.flow.server.WrappedSession
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope



@SpringComponent
@UIScope
final class CreateWatchForm(private val service:WatchService,
                            private var watchEntity:WatchEntity,
                            private val watchRepo:WatchRepository,
                            private val positionRepository: PositionRepository):FormLayout() {
    private val token = TextField("Token")
    private var save = Button("Save")
    private var delete = Button("Delete")
    private var close = Button("Close")
    private val closeButton = Button(Icon("lumo", "cross"))
    private val text = Div(Text("Failed to create watch"))
    var checkPosition = Button("Check Position on Map")
    var checkMoving = Button("Check Moving on Map")
    private val dialog = Dialog()
    private val binder = BeanValidationBinder(WatchEntity::class.java)
    private var changeHandler: ChangeHandler?=null
    private val notifError = Notification()
    private val gmaps = GoogleMap("AIzaSyC7Q3-PD4pYBdAlDx9O_gxFAEUewPVGOeE", null,null)
    val butResolved=Button("Resolved Problem")
    private val sos: Sos = Sos(token = "0",sos = false)
    interface ChangeHandler {
        fun onChange(){}
    }

    init{
        addClassName("contact-form")
        createNotification()
        configDialog()
        configMap()
        add(
            token,
            createButtonsLayout(),
            checkPosition,
            checkMoving,
            butResolved
        )
        binder.bindInstanceFields(this)

    }

    private fun createButtonsLayout(): Component {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        checkPosition.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        butResolved.addThemeVariants(ButtonVariant.LUMO_ERROR)

        save.addClickShortcut(Key.ENTER)
        close.addClickShortcut(Key.ESCAPE)
        checkPosition.addClickShortcut(Key.ESCAPE)

        save.addClickListener {
               binder.writeBean(watchEntity)
               service.save(watchEntity,changeHandler)

        }
        delete.addClickListener {  service.delete(watchEntity,changeHandler) }
        close.addClickListener { isVisible=false }
        butResolved.addClickListener {
            service.sos(sos)
            service.tokremove(watchEntity.token)
            changeHandler?.onChange()
        }

        checkPosition.addClickListener { gmaps.center = LatLon(binder.bean.latitude,binder.bean.longitude)
            gmaps.addMarker("Center", LatLon(binder.bean.latitude,binder.bean.longitude), false, "")
            dialog.open() }

        checkMoving.addClickListener {
            val positions = positionRepository.findByToken(binder.bean.token)
            positions.subList(0,5)
            gmaps.center = LatLon(binder.bean.latitude,binder.bean.longitude)
            positions.forEach{
                gmaps.addMarker("Center", LatLon(it.lat,it.lon), false, "")
            }
            dialog.open()
        }



        checkPosition.isVisible = false
        checkMoving.isVisible = false
        butResolved.isVisible=false
        isVisible = false


        return HorizontalLayout(save, delete, close)
    }


    fun editPeople(c: WatchEntity?) {
        if (c == null) {
            isVisible = false
            return
        }

        val persisted = c.id.toInt() != 0
        if (persisted) {
            watchEntity =  watchRepo.findById(c.id).get()
            checkPosition.isVisible = true
            checkMoving.isVisible = true
        }
        else {
            watchEntity = c
        }

        binder.bean = watchEntity
        isVisible = true

        token.focus()
    }


    fun setChangeHandler(h: ChangeHandler){
        changeHandler = h
    }

    private fun configDialog(){
        dialog.setSizeFull()
        dialog.headerTitle = "Map"
        val dialogLayout = configMap()
        dialog.add(dialogLayout)
        val cancelButton = Button("Cancel") {
            dialog.close()
            gmaps.element.removeAllChildren()
        }
        dialog.footer.add(cancelButton)
    }

    private fun configMap():Component{
        gmaps.mapType = GoogleMap.MapType.ROADMAP
        gmaps.setSizeFull()
        return gmaps
    }

    private fun  createNotification(){
        notifError.addThemeVariants(NotificationVariant.LUMO_ERROR)
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)

        closeButton.element.setAttribute("aria-label", "Close")

        closeButton.addClickListener { notifError.close() }

        val layout = HorizontalLayout(text, closeButton)
        layout.alignItems = FlexComponent.Alignment.CENTER
        notifError.add(layout)

    }

}

