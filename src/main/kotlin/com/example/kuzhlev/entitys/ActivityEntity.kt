package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name="activity")
@Component
class ActivityEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long,
    var steps:Int,
    var pulse: Int,
    var kcal: Int,
    var night_rise:Int,
    var token:String,
    var date:String,
    )