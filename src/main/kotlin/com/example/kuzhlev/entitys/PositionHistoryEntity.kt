package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name = "history")
@Component
class PositionHistoryEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,
    var token:String,
    var lat:Double,
    var lon:Double
    )