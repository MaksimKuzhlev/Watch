package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name="patient")
@Component
class PatientEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,
    var fio:String,
    var city: String,
    var address:String,
    var number: String,
    var email: String,
    var numberConfidant: String,
    var token: String,
    )