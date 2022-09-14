package com.example.kuzhlev.views

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Page
import com.vaadin.flow.component.page.Push
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
// автообновление потока
@Push
class ApllServ(): SpringBootServletInitializer(), AppShellConfigurator {

    class FeederThread(private val ui: UI, private val view: GridView,private val page:Page) : Thread() {
        private val l = 0
        override fun run() {
            try{
                while (ui.page == page) {
                    if (view.service.sosrq()) {
                        ui.access {
                            view.text.text = "SOS token ${view.service.tokrq()}"
                            view.closeButton.text = "Resolved problem"
                            view.notifSos.open()
                            view.grid.setClassNameGenerator {
                                if (it.token == view.service.tokrq())
                                    "warn"
                                else
                                    null
                            }
                        }
                    }
                    sleep(5000)
                }
            } catch (e:InterruptedException){
                ui.access { view.notifSos.close() }
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
                        view.updateList()
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
                            view.text.text = "SOS token ${view.serv.tokrq()}"
                            view.notification.open()
                        }
                    }
                    sleep(5000)
                }
            } catch (e:InterruptedException){
                ui.access { view.notification.close() }
                println(e)
            }
        }

    }
}