package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.PositionRepository
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.WatchService
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap
import com.flowingcode.vaadin.addons.googlemaps.LatLon
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope


@SpringComponent
@UIScope
final class CreateWatchForm(private val service:WatchService,
                            private var watchEntity:WatchEntity,
                            private val watchRepo:WatchRepository,
                            private val positionRepository: PositionRepository):FormLayout() {
    private val token = TextField("Token")
    var save = Button("Save")
    var delete = Button("Delete")
    var close = Button("Close")
    var checkPosition = Button("Check Position on Map")
    var checkMoving = Button("Check Moving on Map")
    val dialog = Dialog()
    private val binder = BeanValidationBinder(WatchEntity::class.java)
    private var changeHandler: ChangeHandler?=null
    private val gmaps = GoogleMap("AIzaSyC7Q3-PD4pYBdAlDx9O_gxFAEUewPVGOeE", null,null)
    interface ChangeHandler {
        fun onChange(){}
    }

    init{
        addClassName("contact-form")
        configDialog()
        configMap()
        add(
            token,
            createButtonsLayout(),
            checkPosition,
            checkMoving
        )
        binder.bindInstanceFields(this)

    }

    private fun createButtonsLayout(): Component {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        checkPosition.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        save.addClickShortcut(Key.ENTER)
        close.addClickShortcut(Key.ESCAPE)
        checkPosition.addClickShortcut(Key.ESCAPE)

        save.addClickListener {
           if(!token.isEmpty)
               service.save(binder,changeHandler)
           TODO("Notification")

        }
        delete.addClickListener {  service.delete(changeHandler) }
        close.addClickListener { isVisible=false }

        checkPosition.addClickListener { gmaps.center = LatLon(binder.bean.latitude,binder.bean.longitude)
            gmaps.addMarker("Center", LatLon(binder.bean.latitude,binder.bean.longitude), false, "")
            dialog.open() }

        checkMoving.addClickListener {
            val positions = positionRepository.findByToken(binder.bean.token)
            gmaps.center = LatLon(binder.bean.latitude,binder.bean.longitude)
            positions.forEach{
                gmaps.addMarker("Center", LatLon(it.lat,it.lon), false, "")
            }
            dialog.open()
        }


        checkPosition.isVisible = false
        checkMoving.isVisible = false
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

    fun configDialog(){
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

}

