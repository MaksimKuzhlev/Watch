package com.example.kuzhlev.entitys

import com.vaadin.flow.function.SerializableFunction
import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name = "watch")
@Component
class WatchEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var token: String,
    var longitude: Double,
    var latitude: Double,
    var masturbate:Boolean,
    var heart_rate: Int,
    var has_fallen:Boolean,
    var charge_level:Int,
    var network_level:Int,
    var name:String,

)
