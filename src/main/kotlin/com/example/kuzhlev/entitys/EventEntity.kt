package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(name="event")
@Component
class EventEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var lat:Double,
    var lon:Double,
    var type: String,
    var time: String,
    var date: LocalDate,


        )