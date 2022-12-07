package com.example.kuzhlev.views

import com.example.kuzhlev.entitys.WatchEntity
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Page
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.server.AppShellSettings
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

// автообновление потока
@Push
class ApllServ(): SpringBootServletInitializer(), AppShellConfigurator {

    class FeederThread(private val ui: UI,
                       private val view: GridView,
                        private val page:Page): Thread() {
        override fun run() {
            try{
                while (ui.page == page) {
                    if (view.service.sosrq()) {
                        ui.access {
                            view.text.text = "SOS token ${view.service.tokrq().last()}"
                            view.notifSos.open()
                            view.form.butResolved.isVisible = true
                            view.updateList(view.textField.value)
                            view.grid.setClassNameGenerator {

                                if (view.service.tokrq().contains(it.token))
                                    "warn"
                                else
                                    null
                            }
                        }
                    }
                    sleep(5000)
                }

            } catch (e:InterruptedException){
                println(e)
            }
        }

    }



    class FeederThread2(private val ui: UI, private val view: GridView,private val page:Page): Thread(){

        override fun run() {
            try{
                while (ui.page == page){
                    sleep(30000)
                    ui.access{
                        view.updateList(view.textField.value)
                    }
                }
            } catch(e:InterruptedException){
                println(e)
            }
        }

    }

    class FeederThreadUser(private val ui: UI, private val view: UserView,private val page:Page) : Thread() {

        override fun run() {
            try{
                while (ui.page == page) {
                    if (view.serv.sosrq()) {
                        ui.access {
                            view.text.text = "SOS token ${view.serv.tokrq().last()}"
                            view.notification.open()
                        }
                    }
                    sleep(5000)
                }
            } catch (e:InterruptedException){
                println(e)
            }
        }

    }

    override fun configurePage(settings: AppShellSettings) {
        settings.addFavIcon("icon","src/main/resources/META-INF/resources/icons/icon.png","192x192")
    }
}