package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDate.now
import javax.persistence.*

@Entity
@Table(name = "history")
@Component
class PositionHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long,
    var token:String,
    var lat:Double,
    var lon:Double,
    var date:LocalDate,
    )