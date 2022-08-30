package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.WatchEntity
import com.example.kuzhlev.repositories.WatchRepository
import com.example.kuzhlev.services.WatchService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@Route(value = "grid", layout = MainLayout::class)
@PageTitle("Grid")
@RolesAllowed("ADMIN")
class GridView(private val service: WatchService):VerticalLayout(){
    private val grid: Grid<WatchEntity> = Grid(WatchEntity::class.java)
    private val form = CreateWatchForm(service)
    init {
        setSizeFull()
        configureGrid()
        add(getToolBar(),getContent())
        form.setChangeHandler(object : CreateWatchForm.ChangeHandler {
            override fun onChange() {
                form.isVisible = false
                updateList()
            }
        })
        updateList()

    }



    private fun configureGrid(){
        grid.run{
            addClassName("contact-grid")
            setSizeFull()
            setColumns("id",
                        "token",
                        "longitude",
                        "latitude",
                        "masturbate",
                        "heart_rate",
                        "has_fallen",
                        "charge_level",
                        "network_level")
            columns.forEach{col->col.setAutoWidth(true)}
        }
    }

    private fun getContent(): Component {
        val content = HorizontalLayout(grid, form)
        content.setFlexGrow(2.0, grid)
        content.setFlexGrow(1.0, form)
        content.addClassNames("content")
        content.setSizeFull()
        return content
    }


    private fun getToolBar():HorizontalLayout{

            val addContactButton = Button("Add contact")
            addContactButton.addClickListener {form.isVisible = true }
            val toolBar = HorizontalLayout(addContactButton)
            toolBar.addClassName("toolbar")
            return toolBar
        }


     fun updateList(){
        grid.setItems(service.findAllWatches())
    }




}